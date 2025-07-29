package demoproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.Requirement;
import demoproject.models.TestResult;
import demoproject.models.ValidationReport;

/**
 * Service for validating test results and suggesting improvements
 */
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    private final AgentConfig config;
    private final LlmService llmService;

    public Validator(AgentConfig config) {
        this.config = config;
        this.llmService = new LlmService(config);
    }

    public LlmService getLlmService() {
        return llmService;
    }

    /**
     * Validate test results and generate improvement suggestions
     */
    public ValidationReport validateResults(Requirement requirement, GeneratedTest test,
            ProductionCode productionCode, TestResult testResult) {
        logger.debug("Validating results for requirement: {}", requirement.getId());

        ValidationReport report = new ValidationReport();
        report.setRequirementId(requirement.getId());
        report.setTestId(test.getId());

        try {
            // Step 1: Analyze test coverage
            analyzeTestCoverage(requirement, test, report);

            // Step 2: Analyze test quality
            analyzeTestQuality(test, report);

            // Step 3: Analyze production code quality
            analyzeProductionCodeQuality(productionCode, report);

            // Step 4: Analyze test results
            analyzeTestResults(testResult, report);

            // Step 5: Generate improvement suggestions
            generateImprovementSuggestions(requirement, test, productionCode, testResult, report);

            report.setValidationCompleted(true);

        } catch (Exception e) {
            logger.error("Failed to validate results for requirement: {}", requirement.getId(), e);
            report.setErrorMessage("Validation failed: " + e.getMessage());
        }

        return report;
    }

    /**
     * Analyze test coverage
     */
    private void analyzeTestCoverage(Requirement requirement, GeneratedTest test, ValidationReport report) {
        String testCode = test.getTestCode();
        if (testCode == null || testCode.isEmpty()) {
            report.addIssue("No test code generated");
            return;
        }

        // Check for basic test coverage patterns
        List<String> coverageIssues = new ArrayList<>();

        // Check if tests cover positive scenarios
        if (!testCode.contains("assertTrue") && !testCode.contains("assertEquals")) {
            coverageIssues.add("Missing positive test scenarios");
        }

        // Check if tests cover negative scenarios
        if (!testCode.contains("assertFalse") && !testCode.contains("assertThrows")) {
            coverageIssues.add("Missing negative test scenarios");
        }

        // Check if tests cover edge cases
        if (!testCode.contains("null") && !testCode.contains("empty") && !testCode.contains("edge")) {
            coverageIssues.add("Missing edge case tests");
        }

        // Check if tests cover acceptance criteria
        for (String criterion : requirement.getAcceptanceCriteria()) {
            if (!testCode.toLowerCase().contains(criterion.toLowerCase())) {
                coverageIssues.add("Test may not cover acceptance criterion: " + criterion);
            }
        }

        // Check for parameterized tests
        if (!testCode.contains("@ParameterizedTest") && !testCode.contains("@ValueSource")) {
            coverageIssues.add("Consider adding parameterized tests for better coverage");
        }

        report.addCoverageIssues(coverageIssues);
    }

    /**
     * Analyze test quality
     */
    private void analyzeTestQuality(GeneratedTest test, ValidationReport report) {
        String testCode = test.getTestCode();
        if (testCode == null || testCode.isEmpty()) {
            return;
        }

        List<String> qualityIssues = new ArrayList<>();

        // Check for descriptive test names
        Pattern testMethodPattern = Pattern.compile("@Test\\s+[^\\{]*void\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
        Matcher matcher = testMethodPattern.matcher(testCode);

        while (matcher.find()) {
            String testMethodName = matcher.group(1);
            if (testMethodName.length() < 10) {
                qualityIssues.add("Test method name too short: " + testMethodName);
            }
            if (testMethodName.startsWith("test")) {
                qualityIssues.add("Consider using descriptive test method name instead of: " + testMethodName);
            }
        }

        // Check for proper test structure (Given-When-Then)
        if (!testCode.contains("// Given") && !testCode.contains("// When") && !testCode.contains("// Then")) {
            qualityIssues.add("Consider using Given-When-Then structure for better test readability");
        }

        // Check for proper assertions
        if (testCode.contains("assertTrue(true")) {
            qualityIssues.add("Found placeholder assertions - implement actual test logic");
        }

        // Check for proper error handling tests
        if (!testCode.contains("assertThrows") && !testCode.contains("Exception")) {
            qualityIssues.add("Consider adding tests for exception scenarios");
        }

        report.addQualityIssues(qualityIssues);
    }

    /**
     * Analyze production code quality
     */
    private void analyzeProductionCodeQuality(ProductionCode productionCode, ValidationReport report) {
        if (productionCode == null || productionCode.getCode() == null) {
            report.addIssue("No production code generated");
            return;
        }

        String code = productionCode.getCode();
        List<String> codeIssues = new ArrayList<>();

        // Check for TODO comments
        if (code.contains("TODO")) {
            codeIssues.add("Production code contains TODO comments - implement missing functionality");
        }

        // Check for proper error handling
        if (!code.contains("try") && !code.contains("catch") && !code.contains("throws")) {
            codeIssues.add("Production code lacks proper error handling");
        }

        // Check for documentation
        if (!code.contains("/**") && !code.contains("/*")) {
            codeIssues.add("Production code lacks documentation");
        }

        // Check for proper naming conventions
        Pattern methodPattern = Pattern.compile("public\\s+\\w+\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
        Matcher matcher = methodPattern.matcher(code);

        while (matcher.find()) {
            String methodName = matcher.group(1);
            if (methodName.length() < 3) {
                codeIssues.add("Method name too short: " + methodName);
            }
        }

        report.addCodeIssues(codeIssues);
    }

    /**
     * Analyze test results
     */
    private void analyzeTestResults(TestResult testResult, ValidationReport report) {
        if (testResult == null) {
            report.addIssue("No test results available");
            return;
        }

        List<String> resultIssues = new ArrayList<>();

        // Check for compilation errors
        if (testResult.hasCompilationErrors()) {
            resultIssues.add("Compilation errors found: " + testResult.getCompilationErrorCount());
        }

        // Check for test failures
        if (testResult.hasFailures()) {
            resultIssues.add("Test failures found: " + testResult.getFailedTestCount());
        }

        // Check test success rate
        if (testResult.getSuccessRate() < 100.0) {
            resultIssues.add("Test success rate below 100%: " + String.format("%.1f%%", testResult.getSuccessRate()));
        }

        // Check test execution time
        if (testResult.getDurationMs() > 5000) {
            resultIssues.add("Test execution time is high: " + testResult.getDurationMs() + "ms");
        }

        report.addResultIssues(resultIssues);
    }

    /**
     * Generate improvement suggestions using LLM
     */
    private void generateImprovementSuggestions(Requirement requirement, GeneratedTest test,
            ProductionCode productionCode, TestResult testResult,
            ValidationReport report) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following TDD implementation and suggest improvements:\n\n");

        prompt.append("Requirement:\n");
        prompt.append(requirement.getTestGenerationSummary()).append("\n\n");

        prompt.append("Test Code:\n");
        prompt.append(test.getTestCode()).append("\n\n");

        if (productionCode != null) {
            prompt.append("Production Code:\n");
            prompt.append(productionCode.getCode()).append("\n\n");
        }

        prompt.append("Test Results:\n");
        prompt.append(testResult.getSummary()).append("\n\n");

        prompt.append("Validation Issues Found:\n");
        for (String issue : report.getAllIssues()) {
            prompt.append("- ").append(issue).append("\n");
        }

        prompt.append("\nPlease provide specific, actionable suggestions to improve:\n");
        prompt.append("1. Test coverage\n");
        prompt.append("2. Test quality\n");
        prompt.append("3. Production code quality\n");
        prompt.append("4. Overall TDD implementation\n\n");
        prompt.append("Focus on practical improvements that can be implemented.");

        try {
            String suggestions = llmService.generateSuggestions(prompt.toString());
            report.setImprovementSuggestions(suggestions);
        } catch (Exception e) {
            logger.error("Failed to generate improvement suggestions", e);
            report.setImprovementSuggestions("Failed to generate suggestions: " + e.getMessage());
        }
    }

    /**
     * Validate multiple requirements
     */
    public List<ValidationReport> validateMultipleRequirements(List<Requirement> requirements,
            List<GeneratedTest> tests,
            List<ProductionCode> productionCodes,
            List<TestResult> testResults) {
        logger.info("Validating results for {} requirements", requirements.size());

        List<ValidationReport> reports = new ArrayList<>();

        for (Requirement requirement : requirements) {
            GeneratedTest test = tests.stream()
                    .filter(t -> t.getRequirementId().equals(requirement.getId()))
                    .findFirst()
                    .orElse(null);

            ProductionCode productionCode = productionCodes.stream()
                    .filter(p -> p.getRequirementId().equals(requirement.getId()))
                    .findFirst()
                    .orElse(null);

            TestResult testResult = testResults.stream()
                    .filter(r -> r.getRequirementId().equals(requirement.getId()))
                    .findFirst()
                    .orElse(null);

            ValidationReport report = validateResults(requirement, test, productionCode, testResult);
            reports.add(report);
        }

        return reports;
    }

    /**
     * Generate overall validation summary
     */
    public String generateValidationSummary(List<ValidationReport> reports) {
        StringBuilder summary = new StringBuilder();
        summary.append("Validation Summary:\n");
        summary.append("Total Requirements: ").append(reports.size()).append("\n");

        long passedCount = reports.stream().filter(r -> r.getIssueCount() == 0).count();
        long failedCount = reports.size() - passedCount;

        summary.append("Requirements with Issues: ").append(failedCount).append("\n");
        summary.append("Requirements without Issues: ").append(passedCount).append("\n");
        summary.append("Success Rate: ").append(String.format("%.1f%%", (double) passedCount / reports.size() * 100)).append("\n\n");

        if (failedCount > 0) {
            summary.append("Requirements with Issues:\n");
            reports.stream()
                    .filter(r -> r.getIssueCount() > 0)
                    .forEach(r -> {
                        summary.append("- ").append(r.getRequirementId()).append(": ").append(r.getIssueCount()).append(" issues\n");
                    });
        }

        return summary.toString();
    }
}
