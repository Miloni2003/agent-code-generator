package demoproject.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a software requirement that will be used to generate test cases
 */
public class Requirement {

    private String id;
    private String title;
    private String description;
    private String priority;
    private String type;
    private String status;
    private List<String> acceptanceCriteria;
    private List<String> dependencies;
    private String module;
    private String assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sourceFile;
    private int lineNumber;

    public Requirement() {
        this.acceptanceCriteria = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Requirement(String id, String title, String description) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(List<String> acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Add acceptance criterion
     */
    public void addAcceptanceCriterion(String criterion) {
        if (this.acceptanceCriteria == null) {
            this.acceptanceCriteria = new ArrayList<>();
        }
        this.acceptanceCriteria.add(criterion);
    }

    /**
     * Add dependency
     */
    public void addDependency(String dependency) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }
        this.dependencies.add(dependency);
    }

    /**
     * Get a summary of the requirement for test generation
     */
    public String getTestGenerationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Requirement ID: ").append(id).append("\n");
        summary.append("Title: ").append(title).append("\n");
        summary.append("Description: ").append(description).append("\n");

        if (priority != null && !priority.isEmpty()) {
            summary.append("Priority: ").append(priority).append("\n");
        }

        if (type != null && !type.isEmpty()) {
            summary.append("Type: ").append(type).append("\n");
        }

        if (!acceptanceCriteria.isEmpty()) {
            summary.append("Acceptance Criteria:\n");
            for (String criterion : acceptanceCriteria) {
                summary.append("  - ").append(criterion).append("\n");
            }
        }

        if (!dependencies.isEmpty()) {
            summary.append("Dependencies:\n");
            for (String dependency : dependencies) {
                summary.append("  - ").append(dependency).append("\n");
            }
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "Requirement{"
                + "id='" + id + '\''
                + ", title='" + title + '\''
                + ", priority='" + priority + '\''
                + ", type='" + type + '\''
                + ", status='" + status + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requirement that = (Requirement) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
