package demoproject.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generated test case
 */
public class GeneratedTest {

    private String id;
    private String requirementId;
    private String testName;
    private String testClassName;
    private String testContent;
    private String fileName;
    private String packageName;
    private String testFramework;
    private String language;
    private List<TestCase> testCases;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private TestStatus status;
    private String errorMessage;

    public enum TestStatus {
        GENERATED,
        COMPILED,
        EXECUTED,
        FAILED,
        PASSED
    }

    public GeneratedTest() {
        this.testCases = new ArrayList<>();
        this.generatedAt = LocalDateTime.now();
        this.status = TestStatus.GENERATED;
    }

    public GeneratedTest(String id, String requirementId, String testName) {
        this();
        this.id = id;
        this.requirementId = requirementId;
        this.testName = testName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getTestContent() {
        return testContent;
    }

    public void setTestContent(String testContent) {
        this.testContent = testContent;
    }

    /**
     * Get test code (alias for getTestContent for compatibility)
     */
    public String getTestCode() {
        return testContent;
    }

    /**
     * Set test code (alias for setTestContent for compatibility)
     */
    public void setTestCode(String testCode) {
        this.testContent = testCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTestFramework() {
        return testFramework;
    }

    public void setTestFramework(String testFramework) {
        this.testFramework = testFramework;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Add a test case
     */
    public void addTestCase(TestCase testCase) {
        if (this.testCases == null) {
            this.testCases = new ArrayList<>();
        }
        this.testCases.add(testCase);
    }

    /**
     * Get the full file path for this test
     */
    public String getFullFilePath() {
        if (packageName != null && !packageName.isEmpty()) {
            return packageName.replace('.', '/') + "/" + fileName;
        }
        return fileName;
    }

    /**
     * Get the number of test cases
     */
    public int getTestCaseCount() {
        return testCases != null ? testCases.size() : 0;
    }

    /**
     * Get a summary of the generated test
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Test ID: ").append(id).append("\n");
        summary.append("Requirement ID: ").append(requirementId).append("\n");
        summary.append("Test Name: ").append(testName).append("\n");
        summary.append("Test Class: ").append(testClassName).append("\n");
        summary.append("File: ").append(fileName).append("\n");
        summary.append("Framework: ").append(testFramework).append("\n");
        summary.append("Language: ").append(language).append("\n");
        summary.append("Status: ").append(status).append("\n");
        summary.append("Test Cases: ").append(getTestCaseCount()).append("\n");
        summary.append("Generated At: ").append(generatedAt).append("\n");

        if (errorMessage != null && !errorMessage.isEmpty()) {
            summary.append("Error: ").append(errorMessage).append("\n");
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "GeneratedTest{"
                + "id='" + id + '\''
                + ", requirementId='" + requirementId + '\''
                + ", testName='" + testName + '\''
                + ", fileName='" + fileName + '\''
                + ", status=" + status
                + '}';
    }
}
