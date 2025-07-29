package demoproject.services;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.Requirement;
import demoproject.models.TestCase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SimpleTestGenerator {
    
    private final AgentConfig config;
    private final SimpleLlmService llmService;
    
    public SimpleTestGenerator(AgentConfig config) {
        this.config = config;
        this.llmService = new SimpleLlmService(config);
    }
    
    public GeneratedTest generateTests(Requirement requirement) {
        System.out.println("Generating tests for requirement: " + requirement.getId());
        
        try {
            // Step 1: Create a comprehensive prompt for test generation
            String prompt = createTestGenerationPrompt(requirement);
            
            // Step 2: Generate test code using LLM
            String generatedTestCode = llmService.generateTestCode(prompt);
            
            // Step 3: Create the generated test object
            GeneratedTest generatedTest = createGeneratedTest(requirement, generatedTestCode);
            
            // Step 4: Write the test file
            writeTestFile(generatedTest);
            
            System.out.println("Generated test file: " + generatedTest.getFileName());
            return generatedTest;
            
        } catch (Exception e) {
            System.err.println("Failed to generate tests for requirement: " + requirement.getId() + " - " + e.getMessage());
            
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
    
    private String createTestGenerationPrompt(Requirement requirement) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a comprehensive JUnit 5 test class for the following requirement:\n\n");
        prompt.append("Requirement ID: ").append(requirement.getId()).append("\n");
        prompt.append("Title: ").append(requirement.getTitle()).append("\n");
        prompt.append("Description: ").append(requirement.getDescription()).append("\n");
        
        if (!requirement.getAcceptanceCriteria().isEmpty()) {
            prompt.append("Acceptance Criteria:\n");
            for (String criterion : requirement.getAcceptanceCriteria()) {
                prompt.append("- ").append(criterion).append("\n");
            }
        }
        
        prompt.append("\nPlease generate a complete JUnit 5 test class that includes:\n");
        prompt.append("1. Positive test cases for all acceptance criteria\n");
        prompt.append("2. Negative test cases for edge cases and error conditions\n");
        prompt.append("3. Proper test method names using @DisplayName annotations\n");
        prompt.append("4. Comprehensive assertions using JUnit 5 assertions\n");
        prompt.append("5. Mock objects where appropriate\n");
        prompt.append("6. Test setup and teardown methods\n\n");
        prompt.append("Return only the complete Java test class code, no explanations.");
        
        return prompt.toString();
    }
    
    private GeneratedTest createGeneratedTest(Requirement requirement, String testCode) {
        GeneratedTest test = new GeneratedTest();
        test.setId("TEST-" + requirement.getId());
        test.setRequirementId(requirement.getId());
        test.setTestName("Test" + requirement.getId());
        test.setTestClassName("Test" + requirement.getId());
        test.setFileName("Test" + requirement.getId() + ".java");
        test.setTestCode(testCode);
        test.setStatus(GeneratedTest.TestStatus.GENERATED);
        return test;
    }
    
    private void writeTestFile(GeneratedTest test) throws IOException {
        Path outputPath = Paths.get("generated-tests", test.getRequirementId(), "src", "test", "java");
        Files.createDirectories(outputPath);
        
        Path testFile = outputPath.resolve(test.getFileName());
        Files.write(testFile, test.getTestCode().getBytes());
        
        System.out.println("Written test file: " + testFile.toAbsolutePath());
    }
} 