package demoproject.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a validation report with issues and improvement suggestions
 */
public class ValidationReport {

    private String requirementId;
    private String testId;
    private LocalDateTime validatedAt;
    private boolean validationCompleted;
    private String errorMessage;
    private List<String> coverageIssues;
    private List<String> qualityIssues;
    private List<String> codeIssues;
    private List<String> resultIssues;
    private String improvementSuggestions;

    public ValidationReport() {
        this.validatedAt = LocalDateTime.now();
        this.coverageIssues = new ArrayList<>();
        this.qualityIssues = new ArrayList<>();
        this.codeIssues = new ArrayList<>();
        this.resultIssues = new ArrayList<>();
    }

    public ValidationReport(String requirementId, String testId) {
        this();
        this.requirementId = requirementId;
        this.testId = testId;
    }

    // Getters and Setters
    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public boolean isValidationCompleted() {
        return validationCompleted;
    }

    public void setValidationCompleted(boolean validationCompleted) {
        this.validationCompleted = validationCompleted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getCoverageIssues() {
        return coverageIssues;
    }

    public void setCoverageIssues(List<String> coverageIssues) {
        this.coverageIssues = coverageIssues;
    }

    public List<String> getQualityIssues() {
        return qualityIssues;
    }

    public void setQualityIssues(List<String> qualityIssues) {
        this.qualityIssues = qualityIssues;
    }

    public List<String> getCodeIssues() {
        return codeIssues;
    }

    public void setCodeIssues(List<String> codeIssues) {
        this.codeIssues = codeIssues;
    }

    public List<String> getResultIssues() {
        return resultIssues;
    }

    public void setResultIssues(List<String> resultIssues) {
        this.resultIssues = resultIssues;
    }

    public String getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(String improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    /**
     * Add a coverage issue
     */
    public void addCoverageIssue(String issue) {
        this.coverageIssues.add(issue);
    }

    /**
     * Add multiple coverage issues
     */
    public void addCoverageIssues(List<String> issues) {
        this.coverageIssues.addAll(issues);
    }

    /**
     * Add a quality issue
     */
    public void addQualityIssue(String issue) {
        this.qualityIssues.add(issue);
    }

    /**
     * Add multiple quality issues
     */
    public void addQualityIssues(List<String> issues) {
        this.qualityIssues.addAll(issues);
    }

    /**
     * Add a code issue
     */
    public void addCodeIssue(String issue) {
        this.codeIssues.add(issue);
    }

    /**
     * Add multiple code issues
     */
    public void addCodeIssues(List<String> issues) {
        this.codeIssues.addAll(issues);
    }

    /**
     * Add a result issue
     */
    public void addResultIssue(String issue) {
        this.resultIssues.add(issue);
    }

    /**
     * Add multiple result issues
     */
    public void addResultIssues(List<String> issues) {
        this.resultIssues.addAll(issues);
    }

    /**
     * Add a general issue (to any category)
     */
    public void addIssue(String issue) {
        this.coverageIssues.add(issue);
    }

    /**
     * Get all issues from all categories
     */
    public List<String> getAllIssues() {
        List<String> allIssues = new ArrayList<>();
        allIssues.addAll(coverageIssues);
        allIssues.addAll(qualityIssues);
        allIssues.addAll(codeIssues);
        allIssues.addAll(resultIssues);
        return allIssues;
    }

    /**
     * Get total number of issues
     */
    public int getIssueCount() {
        return coverageIssues.size() + qualityIssues.size() + codeIssues.size() + resultIssues.size();
    }

    /**
     * Check if validation has any issues
     */
    public boolean hasIssues() {
        return getIssueCount() > 0;
    }

    /**
     * Check if validation has coverage issues
     */
    public boolean hasCoverageIssues() {
        return !coverageIssues.isEmpty();
    }

    /**
     * Check if validation has quality issues
     */
    public boolean hasQualityIssues() {
        return !qualityIssues.isEmpty();
    }

    /**
     * Check if validation has code issues
     */
    public boolean hasCodeIssues() {
        return !codeIssues.isEmpty();
    }

    /**
     * Check if validation has result issues
     */
    public boolean hasResultIssues() {
        return !resultIssues.isEmpty();
    }

    /**
     * Get validation status
     */
    public ValidationStatus getStatus() {
        if (!validationCompleted) {
            return ValidationStatus.FAILED;
        }
        if (hasIssues()) {
            return ValidationStatus.ISSUES_FOUND;
        }
        return ValidationStatus.PASSED;
    }

    /**
     * Get a summary of the validation report
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Validation Report Summary:\n");
        summary.append("Requirement ID: ").append(requirementId).append("\n");
        summary.append("Test ID: ").append(testId).append("\n");
        summary.append("Validated At: ").append(validatedAt).append("\n");
        summary.append("Status: ").append(getStatus()).append("\n");
        summary.append("Total Issues: ").append(getIssueCount()).append("\n");
        summary.append("Coverage Issues: ").append(coverageIssues.size()).append("\n");
        summary.append("Quality Issues: ").append(qualityIssues.size()).append("\n");
        summary.append("Code Issues: ").append(codeIssues.size()).append("\n");
        summary.append("Result Issues: ").append(resultIssues.size()).append("\n");
        
        if (hasCoverageIssues()) {
            summary.append("\nCoverage Issues:\n");
            for (String issue : coverageIssues) {
                summary.append("  - ").append(issue).append("\n");
            }
        }
        
        if (hasQualityIssues()) {
            summary.append("\nQuality Issues:\n");
            for (String issue : qualityIssues) {
                summary.append("  - ").append(issue).append("\n");
            }
        }
        
        if (hasCodeIssues()) {
            summary.append("\nCode Issues:\n");
            for (String issue : codeIssues) {
                summary.append("  - ").append(issue).append("\n");
            }
        }
        
        if (hasResultIssues()) {
            summary.append("\nResult Issues:\n");
            for (String issue : resultIssues) {
                summary.append("  - ").append(issue).append("\n");
            }
        }
        
        if (improvementSuggestions != null && !improvementSuggestions.isEmpty()) {
            summary.append("\nImprovement Suggestions:\n");
            summary.append(improvementSuggestions).append("\n");
        }
        
        if (errorMessage != null && !errorMessage.isEmpty()) {
            summary.append("\nError Message: ").append(errorMessage).append("\n");
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "ValidationReport{"
                + "requirementId='" + requirementId + '\''
                + ", testId='" + testId + '\''
                + ", status=" + getStatus()
                + ", issueCount=" + getIssueCount()
                + ", validationCompleted=" + validationCompleted
                + '}';
    }

    /**
     * Validation status enum
     */
    public enum ValidationStatus {
        PASSED,
        ISSUES_FOUND,
        FAILED
    }
} 