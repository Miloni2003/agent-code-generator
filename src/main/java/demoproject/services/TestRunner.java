package demoproject.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.TestResult;

/**
 * Service for running tests using Maven and collecting test results
 */
public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private final AgentConfig config;
    private final ExecutorService executorService;

    public TestRunner(AgentConfig config) {
        this.config = config;
        this.executorService = Executors.newFixedThreadPool(config.getMaxConcurrentTasks());
    }

    /**
     * Run tests for a specific requirement
     */
    public TestResult runTests(GeneratedTest test, ProductionCode productionCode) {
        logger.debug("Running tests for requirement: {}", test.getRequirementId());

        TestResult result = new TestResult();
        result.setTestId(test.getId());
        result.setRequirementId(test.getRequirementId());
        result.setStartedAt(LocalDateTime.now());

        try {
            // Step 1: Ensure test and production code are in the right place
            setupTestEnvironment(test, productionCode);

            // Step 2: Run Maven test command
            ProcessResult processResult = runMavenTest();

            // Step 3: Parse test results
            parseTestResults(processResult, result);

            result.setCompletedAt(LocalDateTime.now());
            result.setSuccess(processResult.exitCode == 0 && result.getFailedTests().isEmpty());

            logger.debug("Test execution completed for requirement: {} - Success: {}", 
                        test.getRequirementId(), result.isSuccess());

        } catch (Exception e) {
            logger.error("Failed to run tests for requirement: {}", test.getRequirementId(), e);
            result.setErrorMessage("Test execution failed: " + e.getMessage());
            result.setSuccess(false);
            result.setCompletedAt(LocalDateTime.now());
        }

        return result;
    }

    /**
     * Setup test environment by creating necessary directories and files
     */
    private void setupTestEnvironment(GeneratedTest test, ProductionCode productionCode) throws IOException {
        // Determine the project root for this specific requirement
        Path projectRoot;
        if (config.getOutputFolder() != null && !config.getOutputFolder().trim().isEmpty()) {
            // The output folder is the base directory containing all generated projects
            Path outputBase = Paths.get(config.getOutputFolder());
            // Each requirement gets its own project folder
            String projectName = test.getRequirementId().replaceAll("[^a-zA-Z0-9-]", "-").toLowerCase();
            projectRoot = outputBase.resolve(projectName);
        } else {
            // Fallback to current directory if no output folder configured
            projectRoot = Paths.get(".");
        }
        
        // Create source directories
        Path srcMainJava = projectRoot.resolve("src/main/java");
        Path srcTestJava = projectRoot.resolve("src/test/java");
        Files.createDirectories(srcMainJava);
        Files.createDirectories(srcTestJava);

        // Write production code
        if (productionCode != null && productionCode.getCode() != null) {
            Path productionPath = srcMainJava.resolve("generated/code");
            Files.createDirectories(productionPath);
            Files.write(productionPath.resolve(productionCode.getFileName()), 
                       productionCode.getCode().getBytes());
        }

        // Write test code
        if (test.getTestCode() != null) {
            Path testPath = srcTestJava.resolve("generated/tests");
            Files.createDirectories(testPath);
            Files.write(testPath.resolve(test.getFileName()), 
                       test.getTestCode().getBytes());
        }
    }

    /**
     * Run Maven test command
     */
    private ProcessResult runMavenTest() throws IOException, InterruptedException {
        // Determine the project root for this specific requirement
        Path projectRoot;
        if (config.getOutputFolder() != null && !config.getOutputFolder().trim().isEmpty()) {
            // The output folder is the base directory containing all generated projects
            Path outputBase = Paths.get(config.getOutputFolder());
            // Each requirement gets its own project folder - we need to determine which one
            // For now, we'll use the first project found, but ideally we should pass the requirement ID
            try {
                // List all project directories and use the first one
                Path[] projectDirs = Files.list(outputBase)
                    .filter(Files::isDirectory)
                    .toArray(Path[]::new);
                if (projectDirs.length > 0) {
                    projectRoot = projectDirs[0];
                } else {
                    projectRoot = Paths.get(".");
                }
            } catch (IOException e) {
                logger.warn("Could not list output directory, using current directory", e);
                projectRoot = Paths.get(".");
            }
        } else {
            // Fallback to current directory if no output folder configured
            projectRoot = Paths.get(".");
        }
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectRoot.toFile());
        
        // Determine the Maven command based on OS
        String mavenCommand = isWindows() ? "mvn.cmd" : "mvn";
        processBuilder.command(mavenCommand, "test", "-q");
        
        logger.debug("Running Maven test command: {} in directory: {}", String.join(" ", processBuilder.command()), projectRoot);
        
        Process process = processBuilder.start();
        
        // Capture output
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();
        
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            
            // Read output in parallel
            CompletableFuture<Void> outputFuture = CompletableFuture.runAsync(() -> {
                try {
                    String line;
                    while ((line = stdInput.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (IOException e) {
                    logger.error("Error reading process output", e);
                }
            }, executorService);
            
            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try {
                    String line;
                    while ((line = stdError.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                } catch (IOException e) {
                    logger.error("Error reading process error", e);
                }
            }, executorService);
            
            // Wait for process to complete
            int exitCode = process.waitFor();
            
            // Wait for output reading to complete
            outputFuture.join();
            errorFuture.join();
            
            return new ProcessResult(exitCode, output.toString(), error.toString());
            
        } finally {
            process.destroy();
        }
    }

    /**
     * Parse test results from Maven output
     */
    private void parseTestResults(ProcessResult processResult, TestResult result) {
        String output = processResult.output;
        String error = processResult.error;
        
        // Parse test statistics
        parseTestStatistics(output, result);
        
        // Parse failed tests
        parseFailedTests(output, result);
        
        // Parse compilation errors
        parseCompilationErrors(error, result);
        
        // Set overall result
        result.setTotalTests(result.getPassedTests() + result.getFailedTests().size());
        result.setSuccess(result.getFailedTests().isEmpty() && result.getCompilationErrors().isEmpty());
    }

    /**
     * Parse test statistics from Maven output
     */
    private void parseTestStatistics(String output, TestResult result) {
        // Look for patterns like "Tests run: 5, Failures: 0, Errors: 0, Skipped: 0"
        Pattern statsPattern = Pattern.compile("Tests run: (\\d+), Failures: (\\d+), Errors: (\\d+), Skipped: (\\d+)");
        Matcher matcher = statsPattern.matcher(output);
        
        if (matcher.find()) {
            result.setTotalTests(Integer.parseInt(matcher.group(1)));
            int failures = Integer.parseInt(matcher.group(2));
            int errors = Integer.parseInt(matcher.group(3));
            result.setPassedTests(result.getTotalTests() - failures - errors);
        }
    }

    /**
     * Parse failed tests from Maven output
     */
    private void parseFailedTests(String output, TestResult result) {
        // Look for test failure patterns
        Pattern failurePattern = Pattern.compile("\\[ERROR\\] (\\w+)\\(([^)]+)\\)");
        Matcher matcher = failurePattern.matcher(output);
        
        while (matcher.find()) {
            String testName = matcher.group(1);
            String className = matcher.group(2);
            result.addFailedTest(testName + " in " + className);
        }
    }

    /**
     * Parse compilation errors from Maven output
     */
    private void parseCompilationErrors(String error, TestResult result) {
        // Look for compilation error patterns
        Pattern compilePattern = Pattern.compile("\\[ERROR\\] COMPILATION ERROR");
        if (compilePattern.matcher(error).find()) {
            // Extract compilation errors
            String[] lines = error.split("\n");
            for (String line : lines) {
                if (line.contains("[ERROR]") && !line.contains("COMPILATION ERROR")) {
                    result.addCompilationError(line.trim());
                }
            }
        }
    }

    /**
     * Run tests for multiple requirements
     */
    public List<TestResult> runTestsForRequirements(List<GeneratedTest> tests, List<ProductionCode> productionCodes) {
        logger.info("Running tests for {} requirements", tests.size());
        
        List<CompletableFuture<TestResult>> futures = tests.stream()
                .map(test -> CompletableFuture.<TestResult>supplyAsync(() -> {
                    ProductionCode productionCode = productionCodes.stream()
                            .filter(code -> code.getRequirementId().equals(test.getRequirementId()))
                            .findFirst()
                            .orElse(null);
                    
                    return runTests(test, productionCode);
                }, executorService))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    /**
     * Check if running on Windows
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * Shutdown the test runner
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * Process result holder
     */
    private static class ProcessResult {
        final int exitCode;
        final String output;
        final String error;

        ProcessResult(int exitCode, String output, String error) {
            this.exitCode = exitCode;
            this.output = output;
            this.error = error;
        }
    }
} 