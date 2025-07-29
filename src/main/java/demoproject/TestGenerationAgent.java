package demoproject;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.Requirement;
import demoproject.services.RequirementsReader;
import demoproject.services.TestGenerator;

/**
 * Main agent class that orchestrates the test generation process. Reads
 * requirements from a folder, generates test cases, and pushes to git
 * repository.
 */
public class TestGenerationAgent {

    private static final Logger logger = LoggerFactory.getLogger(TestGenerationAgent.class);

    private final RequirementsReader requirementsReader;
    private final TestGenerator testGenerator;
    private final ExecutorService executorService;

    public TestGenerationAgent(AgentConfig config) {
        this.requirementsReader = new RequirementsReader(config);
        this.testGenerator = new TestGenerator(config);
        this.executorService = Executors.newFixedThreadPool(config.getMaxConcurrentTasks());
    }

    /**
     * Main method to run the test generation agent
     */
    public static void main(String[] args) {
        try {
            AgentConfig config = AgentConfig.fromArgs(args);
            TestGenerationAgent agent = new TestGenerationAgent(config);
            agent.run();
        } catch (Exception e) {
            logger.error("Failed to run test generation agent", e);
            System.exit(1);
        }
    }

    /**
     * Main execution method that orchestrates the entire process
     */
    public void run() {
        logger.info("Starting Test Generation Agent");
        // logger.info("Requirements folder: {}", config.getRequirementsFolder());
        // logger.info("Output folder: {}", config.getOutputFolder());

        try {
            // Step 1: Read requirements from the specified folder
            List<Requirement> requirements = requirementsReader.readRequirements();
            logger.info("Found {} requirements to process", requirements.size());

            if (requirements.isEmpty()) {
                logger.warn("No requirements found. Exiting.");
                return;
            }

            // Step 2: Generate test cases for each requirement
            List<GeneratedTest> generatedTests = generateTests(requirements);
            logger.info("Generated {} test files", generatedTests.size());

            // Step 3: Write test files to output directory
            writeTestFiles(generatedTests);

            logger.info("Test generation completed successfully");

        } catch (Exception e) {
            logger.error("Error during test generation process", e);
            throw new RuntimeException("Test generation failed", e);
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Generate test cases for all requirements
     */
    private List<GeneratedTest> generateTests(List<Requirement> requirements) {
        logger.info("Starting test generation for {} requirements", requirements.size());

        List<CompletableFuture<GeneratedTest>> futures = requirements.stream()
                .map(req -> CompletableFuture.<GeneratedTest>supplyAsync(() -> {
            try {
                logger.debug("Generating tests for requirement: {}", req.getId());
                return testGenerator.generateTests(req);
            } catch (Exception e) {
                logger.error("Failed to generate tests for requirement: {}", req.getId(), e);
                throw new RuntimeException(e);
            }
        }, executorService))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    /**
     * Write generated test files to the output directory
     */
    private void writeTestFiles(List<GeneratedTest> generatedTests) {
        logger.info("Writing {} test files to output directory", generatedTests.size());

        Path outputPath = Paths.get("generated-tests");

        for (GeneratedTest test : generatedTests) {
            try {
                testGenerator.writeTestFile(test, outputPath);
                logger.debug("Written test file: {}", test.getFileName());
            } catch (Exception e) {
                logger.error("Failed to write test file: {}", test.getFileName(), e);
            }
        }
    }

    /**
     * Shutdown the agent and cleanup resources
     */
    public void shutdown() {
        logger.info("Shutting down Test Generation Agent");
        executorService.shutdown();
    }
}
