package demoproject.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.Requirement;

/**
 * Service for generating production code based on requirements and test cases
 * following Test-Driven Development principles
 */
public class CodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    private final AgentConfig config;
    private final LlmService llmService;
    private final CodeTemplateService templateService;

    public CodeGenerator(AgentConfig config) {
        this.config = config;
        this.llmService = new LlmService(config);
        this.templateService = new CodeTemplateService(config);
    }

    /**
     * Generate production code for a requirement based on its test cases
     */
    public ProductionCode generateCode(Requirement requirement, GeneratedTest test) {
        logger.debug("Generating production code for requirement: {}", requirement.getId());

        try {
            // Step 1: Analyze test cases to understand what code needs to be implemented
            String testAnalysis = analyzeTestCases(test);

            // Step 2: Generate production code using LLM
            String generatedCode = generateProductionCode(requirement, testAnalysis);

            // Step 3: Create the production code object
            ProductionCode productionCode = createProductionCode(requirement, generatedCode);

            logger.debug("Generated production code for requirement: {}", requirement.getId());
            return productionCode;

        } catch (Exception e) {
            logger.error("Failed to generate production code for requirement: {}", requirement.getId(), e);

            // Return minimal production code with error information
            ProductionCode errorCode = new ProductionCode();
            errorCode.setId("ERROR-" + requirement.getId());
            errorCode.setRequirementId(requirement.getId());
            errorCode.setClassName("ErrorClass" + requirement.getId());
            errorCode.setFileName("ErrorClass" + requirement.getId() + ".java");
            errorCode.setErrorMessage("Failed to generate production code: " + e.getMessage());
            errorCode.setStatus(ProductionCode.CodeStatus.FAILED);

            return errorCode;
        }
    }

    /**
     * Analyze test cases to extract requirements for production code
     */
    private String analyzeTestCases(GeneratedTest test) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("Test Analysis for Production Code Generation:\n");
        analysis.append("Test Class: ").append(test.getTestClassName()).append("\n");
        analysis.append("Test Name: ").append(test.getTestName()).append("\n");

        // Extract method signatures and assertions from test code
        String testCode = test.getTestCode();
        if (testCode != null && !testCode.isEmpty()) {
            analysis.append("Required Methods:\n");
            
            // Look for test method names to infer required methods
            Pattern testMethodPattern = Pattern.compile("@Test\\s+[^\\{]*void\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
            Matcher matcher = testMethodPattern.matcher(testCode);
            
            while (matcher.find()) {
                String testMethodName = matcher.group(1);
                String requiredMethodName = inferRequiredMethodName(testMethodName);
                analysis.append("  - ").append(requiredMethodName).append("\n");
            }

            // Look for assertions to understand expected behavior
            analysis.append("Expected Behavior:\n");
            if (testCode.contains("assertTrue")) {
                analysis.append("  - Method should return true for valid conditions\n");
            }
            if (testCode.contains("assertFalse")) {
                analysis.append("  - Method should return false for invalid conditions\n");
            }
            if (testCode.contains("assertEquals")) {
                analysis.append("  - Method should return expected values\n");
            }
            if (testCode.contains("assertNotNull")) {
                analysis.append("  - Method should not return null\n");
            }
            if (testCode.contains("assertNull")) {
                analysis.append("  - Method should return null for certain conditions\n");
            }
        }

        return analysis.toString();
    }

    /**
     * Infer the required method name from test method name
     */
    private String inferRequiredMethodName(String testMethodName) {
        // Remove common test prefixes
        String methodName = testMethodName;
        
        if (methodName.startsWith("test")) {
            methodName = methodName.substring(4);
        }
        if (methodName.startsWith("should")) {
            methodName = methodName.substring(6);
        }
        if (methodName.startsWith("when")) {
            methodName = methodName.substring(4);
        }
        
        // Convert to camelCase if needed
        if (methodName.length() > 0) {
            methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        }
        
        return methodName.isEmpty() ? "process" : methodName;
    }

    /**
     * Generate production code using LLM service
     */
    private String generateProductionCode(Requirement requirement, String testAnalysis) {
        String prompt = buildCodeGenerationPrompt(requirement, testAnalysis);

        try {
            logger.info("Calling LLM service for code generation...");
            String result = llmService.generateCode(prompt);
            logger.info("LLM service returned code of length: {}", result.length());
            return result;
        } catch (Exception e) {
            logger.error("LLM service failed", e);
            throw new RuntimeException("Failed to generate code: " + e.getMessage(), e);
        }
    }

    /**
     * Build the prompt for code generation
     */
    private String buildCodeGenerationPrompt(Requirement requirement, String testAnalysis) {
        String className = requirement.getId().replaceAll("[^a-zA-Z0-9]", "") + "Service";
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a Java developer. Generate a COMPLETE, WORKING Java class implementation.");
        prompt.append("\n\nREQUIREMENT: ").append(requirement.getTitle());
        prompt.append("\nDESCRIPTION: ").append(requirement.getDescription());
        prompt.append("\n\nACCEPTANCE CRITERIA:");
        for (String ac : requirement.getAcceptanceCriteria()) {
            prompt.append("\n- ").append(ac);
        }
        prompt.append("\n\nTEST ANALYSIS: ").append(testAnalysis);
        prompt.append("\n\nMANDATORY REQUIREMENTS:");
        prompt.append("\n- Class name: ").append(className);
        prompt.append("\n- Package: generated.code");
        prompt.append("\n- Implement EVERY SINGLE feature mentioned in the requirement");
        prompt.append("\n- Include proper error handling, validation, and edge cases");
        prompt.append("\n- Add comprehensive comments explaining the logic");
        prompt.append("\n- Make the code production-ready and maintainable");
        prompt.append("\n- Include all necessary imports");
        prompt.append("\n- Return ONLY the complete Java class code, no explanations");
        prompt.append("\n\nGenerate the COMPLETE Java class implementation:");
        return prompt.toString();
    }

    /**
     * Generate fallback production code when LLM service is unavailable
     */
    private String generateFallbackCode(Requirement requirement) {
        logger.error("LLM service failed - cannot generate code without AI");
        throw new RuntimeException("LLM service failed - cannot generate code without AI assistance");
    }

    /**
     * Create production code object
     */
    private ProductionCode createProductionCode(Requirement requirement, String generatedCode) {
        String classNameBase = requirement.getTitle().replaceAll("[^a-zA-Z0-9]", "");
        if (classNameBase.isEmpty()) classNameBase = requirement.getId().replaceAll("[^a-zA-Z0-9]", "");
        String className = (classNameBase.isEmpty() ? "Generated" : Character.toUpperCase(classNameBase.charAt(0)) + (classNameBase.length() > 1 ? classNameBase.substring(1) : "")) + "Service";
        String fileName = className + ".java";
        ProductionCode productionCode = new ProductionCode();
        productionCode.setId(requirement.getId());
        productionCode.setRequirementId(requirement.getId());
        productionCode.setClassName(className);
        productionCode.setFileName(fileName);
        productionCode.setCode(generatedCode);
        productionCode.setStatus(ProductionCode.CodeStatus.GENERATED);
        productionCode.setGeneratedAt(java.time.LocalDateTime.now());
        return productionCode;
    }

    /**
     * Write production code to file
     */
    public void writeCodeFile(ProductionCode productionCode, Path outputPath) throws IOException {
        Path codePath = outputPath.resolve("src/main/java/generated/code");
        Files.createDirectories(codePath);
        
        Path filePath = codePath.resolve(productionCode.getFileName());
        Files.write(filePath, productionCode.getCode().getBytes());
        
        logger.debug("Written production code file: {}", filePath);
    }

    /**
     * Generate code for multiple requirements
     */
    public List<ProductionCode> generateCodeForRequirements(List<Requirement> requirements, List<GeneratedTest> tests) {
        logger.info("Generating production code for {} requirements", requirements.size());
        
        return requirements.stream()
                .map(req -> {
                    GeneratedTest test = tests.stream()
                            .filter(t -> t.getRequirementId().equals(req.getId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (test != null) {
                        return generateCode(req, test);
                    } else {
                        logger.warn("No test found for requirement: {}", req.getId());
                        return generateCode(req, null);
                    }
                })
                .toList();
    }
} 