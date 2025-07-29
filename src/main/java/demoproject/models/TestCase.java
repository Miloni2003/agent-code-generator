package demoproject.models;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an individual test case within a generated test file
 */
public class TestCase {

    private String name;
    private String description;
    private String testMethodName;
    private List<String> steps;
    private List<String> expectedResults;
    private List<String> testData;
    private String priority;
    private TestType type;
    private String category;
    private boolean isParameterized;
    private List<String> parameters;

    public enum TestType {
        UNIT,
        INTEGRATION,
        FUNCTIONAL,
        REGRESSION,
        SMOKE,
        PERFORMANCE,
        SECURITY
    }

    public TestCase() {
        this.steps = new ArrayList<>();
        this.expectedResults = new ArrayList<>();
        this.testData = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.type = TestType.UNIT;
        this.isParameterized = false;
    }

    public TestCase(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(List<String> expectedResults) {
        this.expectedResults = expectedResults;
    }

    public List<String> getTestData() {
        return testData;
    }

    public void setTestData(List<String> testData) {
        this.testData = testData;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isParameterized() {
        return isParameterized;
    }

    public void setParameterized(boolean parameterized) {
        isParameterized = parameterized;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Add a test step
     */
    public void addStep(String step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
    }

    /**
     * Add expected result
     */
    public void addExpectedResult(String result) {
        if (this.expectedResults == null) {
            this.expectedResults = new ArrayList<>();
        }
        this.expectedResults.add(result);
    }

    /**
     * Add test data
     */
    public void addTestData(String data) {
        if (this.testData == null) {
            this.testData = new ArrayList<>();
        }
        this.testData.add(data);
    }

    /**
     * Add parameter for parameterized tests
     */
    public void addParameter(String parameter) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parameter);
    }

    /**
     * Generate a valid Java method name from the test case name
     */
    public String generateMethodName() {
        if (testMethodName != null && !testMethodName.isEmpty()) {
            return testMethodName;
        }

        String methodName = name != null ? name : "testCase";

        // Remove special characters and convert to camelCase
        methodName = methodName.replaceAll("[^a-zA-Z0-9\\s]", "");
        methodName = methodName.replaceAll("\\s+", " ");
        methodName = methodName.trim();

        if (methodName.isEmpty()) {
            methodName = "testCase";
        }

        // Convert to camelCase
        String[] words = methodName.split("\\s+");
        StringBuilder camelCase = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                camelCase.append(word);
            } else {
                if (word.length() > 0) {
                    camelCase.append(Character.toUpperCase(word.charAt(0)));
                    if (word.length() > 1) {
                        camelCase.append(word.substring(1));
                    }
                }
            }
        }

        // Ensure it starts with a letter
        if (!Character.isLetter(camelCase.charAt(0))) {
            camelCase.insert(0, "test");
        }

        return camelCase.toString();
    }

    /**
     * Get a summary of the test case
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Test Case: ").append(name).append("\n");
        summary.append("Description: ").append(description).append("\n");
        summary.append("Method: ").append(generateMethodName()).append("\n");
        summary.append("Type: ").append(type).append("\n");
        summary.append("Priority: ").append(priority).append("\n");
        summary.append("Category: ").append(category).append("\n");
        summary.append("Parameterized: ").append(isParameterized).append("\n");

        if (!steps.isEmpty()) {
            summary.append("Steps:\n");
            for (int i = 0; i < steps.size(); i++) {
                summary.append("  ").append(i + 1).append(". ").append(steps.get(i)).append("\n");
            }
        }

        if (!expectedResults.isEmpty()) {
            summary.append("Expected Results:\n");
            for (int i = 0; i < expectedResults.size(); i++) {
                summary.append("  ").append(i + 1).append(". ").append(expectedResults.get(i)).append("\n");
            }
        }

        if (!testData.isEmpty()) {
            summary.append("Test Data:\n");
            for (String data : testData) {
                summary.append("  - ").append(data).append("\n");
            }
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "TestCase{"
                + "name='" + name + '\''
                + ", type=" + type
                + ", priority='" + priority + '\''
                + ", isParameterized=" + isParameterized
                + '}';
    }
}
