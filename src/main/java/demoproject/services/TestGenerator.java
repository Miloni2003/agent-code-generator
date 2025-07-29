package demoproject.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.Requirement;
import demoproject.models.TestCase;

/**
 * Service for generating test cases from requirements
 */
public class TestGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TestGenerator.class);

    private final AgentConfig config;
    private final LlmService llmService;
    private final TestTemplateService templateService;

    public TestGenerator(AgentConfig config) {
        this.config = config;
        this.llmService = new LlmService(config);
        this.templateService = new TestTemplateService(config);
    }

    /**
     * Generate test cases for a requirement
     */
    public GeneratedTest generateTests(Requirement requirement) {
        logger.debug("Generating tests for requirement: {}", requirement.getId());

        try {
            // Step 1: Analyze the requirement
            String requirementAnalysis = analyzeRequirement(requirement);

            // Step 2: Generate test cases using LLM
            String generatedTestCode = generateTestCode(requirement, requirementAnalysis);

            // Step 3: Parse the generated code into test cases
            List<TestCase> testCases = parseGeneratedTestCases(generatedTestCode);

            // Step 4: Create the generated test object
            GeneratedTest generatedTest = createGeneratedTest(requirement, testCases, generatedTestCode);

            logger.debug("Generated {} test cases for requirement: {}", testCases.size(), requirement.getId());
            return generatedTest;

        } catch (Exception e) {
            logger.error("Failed to generate tests for requirement: {}", requirement.getId(), e);

            // Return a minimal test with error information
            GeneratedTest errorTest = new GeneratedTest();
            errorTest.setId("ERROR-" + requirement.getId());
            errorTest.setRequirementId(requirement.getId());
            errorTest.setTestName("ErrorTest_" + requirement.getId());
            errorTest.setTestClassName("ErrorTest" + requirement.getId());
            errorTest.setFileName("ErrorTest" + requirement.getId() + ".java");
            errorTest.setErrorMessage("Failed to generate tests: " + e.getMessage());
            errorTest.setStatus(GeneratedTest.TestStatus.FAILED);

            return errorTest;
        }
    }

    /**
     * Analyze a requirement to extract key information for test generation
     */
    private String analyzeRequirement(Requirement requirement) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("Requirement Analysis:\n");
        analysis.append("ID: ").append(requirement.getId()).append("\n");
        analysis.append("Title: ").append(requirement.getTitle()).append("\n");
        analysis.append("Description: ").append(requirement.getDescription()).append("\n");
        analysis.append("Priority: ").append(requirement.getPriority()).append("\n");
        analysis.append("Type: ").append(requirement.getType()).append("\n");

        if (!requirement.getAcceptanceCriteria().isEmpty()) {
            analysis.append("Acceptance Criteria:\n");
            for (String criterion : requirement.getAcceptanceCriteria()) {
                analysis.append("  - ").append(criterion).append("\n");
            }
        }

        if (!requirement.getDependencies().isEmpty()) {
            analysis.append("Dependencies:\n");
            for (String dependency : requirement.getDependencies()) {
                analysis.append("  - ").append(dependency).append("\n");
            }
        }

        return analysis.toString();
    }

    /**
     * Generate test code using LLM service
     */
    private String generateTestCode(Requirement requirement, String requirementAnalysis) {
        String prompt = buildTestGenerationPrompt(requirement, requirementAnalysis);

        try {
            logger.info("Calling LLM service for test generation...");
            String result = llmService.generateTestCode(prompt);
            logger.info("LLM service returned test code of length: {}", result.length());
            return result;
        } catch (Exception e) {
            logger.error("LLM service failed", e);
            throw new RuntimeException("Failed to generate test code: " + e.getMessage(), e);
        }
    }

    /**
     * Build the prompt for test generation
     */
    private String buildTestGenerationPrompt(Requirement requirement, String requirementAnalysis) {
        String className = "Test" + requirement.getId().replaceAll("[^a-zA-Z0-9]", "");
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a Java test developer. Generate COMPREHENSIVE JUnit test cases.");
        prompt.append("\n\nREQUIREMENT: ").append(requirement.getTitle());
        prompt.append("\nDESCRIPTION: ").append(requirement.getDescription());
        prompt.append("\n\nACCEPTANCE CRITERIA:");
        for (String criterion : requirement.getAcceptanceCriteria()) {
            prompt.append("\n- ").append(criterion);
        }
        prompt.append("\n\nMANDATORY REQUIREMENTS:");
        prompt.append("\n- Class name: ").append(className);
        prompt.append("\n- Package: generated.tests");
        prompt.append("\n- Include positive test cases for ALL valid scenarios");
        prompt.append("\n- Include negative test cases for ALL invalid inputs and edge cases");
        prompt.append("\n- Include boundary condition test cases");
        prompt.append("\n- Use JUnit 5 annotations (@Test, @BeforeEach, @DisplayName, etc.)");
        prompt.append("\n- Use descriptive test method names");
        prompt.append("\n- Include comprehensive assertions (assertTrue, assertEquals, assertThrows, etc.)");
        prompt.append("\n- Test EVERY SINGLE acceptance criterion");
        prompt.append("\n- Include all necessary imports");
        prompt.append("\n- Return ONLY the complete Java test class, no explanations");
        prompt.append("\n\nGenerate the COMPLETE test class implementation:");
        return prompt.toString();
    }

    /**
     * Generate fallback test code when LLM service is unavailable
     */
    private String generateFallbackTestCode(Requirement requirement) {
        logger.error("LLM service failed - cannot generate tests without AI");
        throw new RuntimeException("LLM service failed - cannot generate tests without AI assistance");
    }

    /**
     * Parse generated test code into test case objects
     */
    private List<TestCase> parseGeneratedTestCases(String testCode) {
        List<TestCase> testCases = new ArrayList<>();

        // Parse @Test methods
        Pattern testPattern = Pattern.compile("@Test\\s+void\\s+(\\w+)\\s*\\([^)]*\\)\\s*\\{([^}]+)\\}", Pattern.DOTALL);
        Matcher testMatcher = testPattern.matcher(testCode);

        while (testMatcher.find()) {
            String methodName = testMatcher.group(1);
            String methodBody = testMatcher.group(2);

            TestCase testCase = new TestCase();
            testCase.setName(methodName);
            testCase.setTestMethodName(methodName);
            testCase.setDescription("Generated test case: " + methodName);
            testCase.setType(TestCase.TestType.UNIT);
            testCase.setPriority("Medium");

            // Extract test steps from method body
            parseTestSteps(methodBody, testCase);

            testCases.add(testCase);
        }

        // If no @Test methods found, create a default test case
        if (testCases.isEmpty()) {
            TestCase defaultCase = new TestCase();
            defaultCase.setName("defaultTest");
            defaultCase.setTestMethodName("defaultTest");
            defaultCase.setDescription("Default test case");
            defaultCase.setType(TestCase.TestType.UNIT);
            defaultCase.setPriority("Medium");
            testCases.add(defaultCase);
        }

        return testCases;
    }

    /**
     * Parse test steps from method body
     */
    private void parseTestSteps(String methodBody, TestCase testCase) {
        String[] lines = methodBody.split("\n");

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("//")) {
                // Comment - treat as step description
                String comment = line.substring(2).trim();
                if (!comment.isEmpty()) {
                    testCase.addStep(comment);
                }
            } else if (line.contains("assert") || line.contains("fail")) {
                // Assertion - treat as expected result
                testCase.addExpectedResult(line);
            } else if (line.contains("=") && !line.contains("==")) {
                // Assignment - treat as test data setup
                testCase.addTestData(line);
            }
        }
    }

    /**
     * Create a GeneratedTest object from the requirement and generated code
     */
    private GeneratedTest createGeneratedTest(Requirement requirement, List<TestCase> testCases, String testCode) {
        String testClassName = "Test" + requirement.getId().replaceAll("[^a-zA-Z0-9]", "");
        String fileName = testClassName + ".java";

        GeneratedTest generatedTest = new GeneratedTest();
        generatedTest.setId("TEST-" + requirement.getId());
        generatedTest.setRequirementId(requirement.getId());
        generatedTest.setTestName(requirement.getTitle());
        generatedTest.setTestClassName(testClassName);
        generatedTest.setTestContent(testCode);
        generatedTest.setFileName(fileName);
        generatedTest.setPackageName("generated.tests");
        generatedTest.setTestFramework(config.getTestFramework());
        generatedTest.setLanguage(config.getLanguage());
        generatedTest.setGeneratedBy("TestGenerationAgent");
        generatedTest.setStatus(GeneratedTest.TestStatus.GENERATED);

        for (TestCase testCase : testCases) {
            generatedTest.addTestCase(testCase);
        }

        return generatedTest;
    }

    /**
     * Write a generated test file to the output directory
     */
    public void writeTestFile(GeneratedTest generatedTest, Path outputPath) throws IOException {
        Path packagePath = outputPath;

        // Create package directory structure
        if (generatedTest.getPackageName() != null && !generatedTest.getPackageName().isEmpty()) {
            String[] packageParts = generatedTest.getPackageName().split("\\.");
            for (String packagePart : packageParts) {
                packagePath = packagePath.resolve(packagePart);
            }
        }

        // Create directories if they don't exist
        Files.createDirectories(packagePath);

        // Write the test file
        Path testFilePath = packagePath.resolve(generatedTest.getFileName());
        Files.write(testFilePath, generatedTest.getTestContent().getBytes());

        logger.debug("Written test file: {}", testFilePath);
    }

    /**
     * Validate generated test code
     */
    public boolean validateTestCode(String testCode) {
        // Basic validation - check for required elements
        return testCode.contains("@Test")
                && testCode.contains("import")
                && testCode.contains("class")
                && testCode.contains("void");
    }
}
