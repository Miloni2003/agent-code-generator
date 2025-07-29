package demoproject.models;

import java.time.LocalDateTime;

/**
 * Represents generated production code for a requirement
 */
public class ProductionCode {

    private String id;
    private String requirementId;
    private String className;
    private String fileName;
    private String code;
    private String errorMessage;
    private CodeStatus status;
    private LocalDateTime generatedAt;
    private LocalDateTime compiledAt;
    private boolean compilationSuccess;
    private String compilationErrors;

    public ProductionCode() {
        this.generatedAt = LocalDateTime.now();
        this.status = CodeStatus.PENDING;
    }

    public ProductionCode(String id, String requirementId, String className) {
        this();
        this.id = id;
        this.requirementId = requirementId;
        this.className = className;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CodeStatus getStatus() {
        return status;
    }

    public void setStatus(CodeStatus status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getCompiledAt() {
        return compiledAt;
    }

    public void setCompiledAt(LocalDateTime compiledAt) {
        this.compiledAt = compiledAt;
    }

    public boolean isCompilationSuccess() {
        return compilationSuccess;
    }

    public void setCompilationSuccess(boolean compilationSuccess) {
        this.compilationSuccess = compilationSuccess;
    }

    public String getCompilationErrors() {
        return compilationErrors;
    }

    public void setCompilationErrors(String compilationErrors) {
        this.compilationErrors = compilationErrors;
    }

    /**
     * Check if the code is ready for testing
     */
    public boolean isReadyForTesting() {
        return status == CodeStatus.GENERATED && compilationSuccess;
    }

    /**
     * Check if the code has compilation errors
     */
    public boolean hasCompilationErrors() {
        return compilationErrors != null && !compilationErrors.trim().isEmpty();
    }

    /**
     * Get a summary of the production code
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Production Code Summary:\n");
        summary.append("ID: ").append(id).append("\n");
        summary.append("Requirement ID: ").append(requirementId).append("\n");
        summary.append("Class Name: ").append(className).append("\n");
        summary.append("File Name: ").append(fileName).append("\n");
        summary.append("Status: ").append(status).append("\n");
        summary.append("Generated At: ").append(generatedAt).append("\n");
        
        if (compiledAt != null) {
            summary.append("Compiled At: ").append(compiledAt).append("\n");
            summary.append("Compilation Success: ").append(compilationSuccess).append("\n");
        }
        
        if (hasCompilationErrors()) {
            summary.append("Compilation Errors: ").append(compilationErrors).append("\n");
        }
        
        if (errorMessage != null && !errorMessage.isEmpty()) {
            summary.append("Error Message: ").append(errorMessage).append("\n");
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "ProductionCode{"
                + "id='" + id + '\''
                + ", requirementId='" + requirementId + '\''
                + ", className='" + className + '\''
                + ", status=" + status
                + ", compilationSuccess=" + compilationSuccess
                + '}';
    }

    /**
     * Status of the production code
     */
    public enum CodeStatus {
        PENDING,
        GENERATED,
        COMPILED,
        FAILED
    }
} 