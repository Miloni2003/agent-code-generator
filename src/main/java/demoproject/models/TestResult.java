package demoproject.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of test execution
 */
public class TestResult {

    private String testId;
    private String requirementId;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private boolean success;
    private String errorMessage;
    private int totalTests;
    private int passedTests;
    private List<String> failedTests;
    private List<String> compilationErrors;
    private String mavenOutput;
    private String mavenError;

    public TestResult() {
        this.failedTests = new ArrayList<>();
        this.compilationErrors = new ArrayList<>();
        this.startedAt = LocalDateTime.now();
    }

    public TestResult(String testId, String requirementId) {
        this();
        this.testId = testId;
        this.requirementId = requirementId;
    }

    // Getters and Setters
    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public List<String> getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(List<String> failedTests) {
        this.failedTests = failedTests;
    }

    public List<String> getCompilationErrors() {
        return compilationErrors;
    }

    public void setCompilationErrors(List<String> compilationErrors) {
        this.compilationErrors = compilationErrors;
    }

    public String getMavenOutput() {
        return mavenOutput;
    }

    public void setMavenOutput(String mavenOutput) {
        this.mavenOutput = mavenOutput;
    }

    public String getMavenError() {
        return mavenError;
    }

    public void setMavenError(String mavenError) {
        this.mavenError = mavenError;
    }

    /**
     * Add a failed test
     */
    public void addFailedTest(String testName) {
        this.failedTests.add(testName);
    }

    /**
     * Add a compilation error
     */
    public void addCompilationError(String error) {
        this.compilationErrors.add(error);
    }

    /**
     * Get the number of failed tests
     */
    public int getFailedTestCount() {
        return failedTests.size();
    }

    /**
     * Get the number of compilation errors
     */
    public int getCompilationErrorCount() {
        return compilationErrors.size();
    }

    /**
     * Get test execution duration in milliseconds
     */
    public long getDurationMs() {
        if (startedAt != null && completedAt != null) {
            return java.time.Duration.between(startedAt, completedAt).toMillis();
        }
        return 0;
    }

    /**
     * Check if tests have compilation errors
     */
    public boolean hasCompilationErrors() {
        return !compilationErrors.isEmpty();
    }

    /**
     * Check if tests have failures
     */
    public boolean hasFailures() {
        return !failedTests.isEmpty();
    }

    /**
     * Get test success rate as a percentage
     */
    public double getSuccessRate() {
        if (totalTests == 0) {
            return 0.0;
        }
        return (double) passedTests / totalTests * 100.0;
    }

    /**
     * Get a summary of the test result
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Test Result Summary:\n");
        summary.append("Test ID: ").append(testId).append("\n");
        summary.append("Requirement ID: ").append(requirementId).append("\n");
        summary.append("Status: ").append(success ? "PASSED" : "FAILED").append("\n");
        summary.append("Started At: ").append(startedAt).append("\n");
        summary.append("Completed At: ").append(completedAt).append("\n");
        summary.append("Duration: ").append(getDurationMs()).append("ms\n");
        summary.append("Total Tests: ").append(totalTests).append("\n");
        summary.append("Passed Tests: ").append(passedTests).append("\n");
        summary.append("Failed Tests: ").append(getFailedTestCount()).append("\n");
        summary.append("Success Rate: ").append(String.format("%.1f%%", getSuccessRate())).append("\n");
        
        if (hasCompilationErrors()) {
            summary.append("Compilation Errors: ").append(getCompilationErrorCount()).append("\n");
        }
        
        if (hasFailures()) {
            summary.append("Failed Tests:\n");
            for (String failedTest : failedTests) {
                summary.append("  - ").append(failedTest).append("\n");
            }
        }
        
        if (hasCompilationErrors()) {
            summary.append("Compilation Errors:\n");
            for (String error : compilationErrors) {
                summary.append("  - ").append(error).append("\n");
            }
        }
        
        if (errorMessage != null && !errorMessage.isEmpty()) {
            summary.append("Error Message: ").append(errorMessage).append("\n");
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "TestResult{"
                + "testId='" + testId + '\''
                + ", requirementId='" + requirementId + '\''
                + ", success=" + success
                + ", totalTests=" + totalTests
                + ", passedTests=" + passedTests
                + ", failedTests=" + failedTests.size()
                + ", compilationErrors=" + compilationErrors.size()
                + '}';
    }
} 