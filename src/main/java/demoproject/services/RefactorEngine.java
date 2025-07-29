package demoproject.services;

import demoproject.config.AgentConfig;
import demoproject.models.ProductionCode;
import demoproject.models.GeneratedTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for refactoring generated code to improve quality and maintainability
 */
public class RefactorEngine {

    private static final Logger logger = LoggerFactory.getLogger(RefactorEngine.class);

    private final AgentConfig config;
    private final LlmService llmService;

    public RefactorEngine(AgentConfig config) {
        this.config = config;
        this.llmService = new LlmService(config);
    }

    /**
     * Refactor production code to improve quality
     */
    public ProductionCode refactorCode(ProductionCode productionCode, GeneratedTest test) {
        logger.debug("Refactoring production code for requirement: {}", productionCode.getRequirementId());

        try {
            // Step 1: Analyze code quality
            CodeQualityReport qualityReport = analyzeCodeQuality(productionCode);

            // Step 2: Generate refactoring suggestions
            List<String> suggestions = generateRefactoringSuggestions(productionCode, qualityReport);

            // Step 3: Apply refactoring if needed
            if (!suggestions.isEmpty()) {
                String refactoredCode = applyRefactoring(productionCode, suggestions);
                productionCode.setCode(refactoredCode);
                productionCode.setStatus(ProductionCode.CodeStatus.GENERATED);
                logger.debug("Applied {} refactoring suggestions", suggestions.size());
            } else {
                logger.debug("No refactoring needed for requirement: {}", productionCode.getRequirementId());
            }

            return productionCode;

        } catch (Exception e) {
            logger.error("Failed to refactor code for requirement: {}", productionCode.getRequirementId(), e);
            productionCode.setErrorMessage("Refactoring failed: " + e.getMessage());
            return productionCode;
        }
    }

    /**
     * Analyze code quality and identify issues
     */
    private CodeQualityReport analyzeCodeQuality(ProductionCode productionCode) {
        CodeQualityReport report = new CodeQualityReport();
        String code = productionCode.getCode();

        if (code == null || code.isEmpty()) {
            report.addIssue("Code is empty or null");
            return report;
        }

        // Check for common code quality issues
        checkCodeLength(code, report);
        checkMethodComplexity(code, report);
        checkNamingConventions(code, report);
        checkCodeDuplication(code, report);
        checkErrorHandling(code, report);
        checkDocumentation(code, report);

        return report;
    }

    /**
     * Check code length
     */
    private void checkCodeLength(String code, CodeQualityReport report) {
        String[] lines = code.split("\n");
        if (lines.length > 100) {
            report.addIssue("Code is too long (" + lines.length + " lines). Consider breaking it into smaller methods.");
        }
    }

    /**
     * Check method complexity
     */
    private void checkMethodComplexity(String code, CodeQualityReport report) {
        // Count nested blocks to estimate complexity
        Pattern nestedPattern = Pattern.compile("\\{[^{}]*\\{[^{}]*\\}");
        Matcher matcher = nestedPattern.matcher(code);
        int nestedCount = 0;
        while (matcher.find()) {
            nestedCount++;
        }
        
        if (nestedCount > 3) {
            report.addIssue("High cyclomatic complexity detected. Consider simplifying the logic.");
        }
    }

    /**
     * Check naming conventions
     */
    private void checkNamingConventions(String code, CodeQualityReport report) {
        // Check for single-letter variable names
        Pattern singleLetterPattern = Pattern.compile("\\b[a-z]\\s*[=;]");
        Matcher matcher = singleLetterPattern.matcher(code);
        if (matcher.find()) {
            report.addIssue("Single-letter variable names detected. Use descriptive names.");
        }

        // Check for TODO comments
        if (code.contains("TODO")) {
            report.addIssue("TODO comments found. Consider implementing the missing functionality.");
        }
    }

    /**
     * Check for code duplication
     */
    private void checkCodeDuplication(String code, CodeQualityReport report) {
        // Simple duplication check - look for repeated code blocks
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length - 2; i++) {
            String block = lines[i] + "\n" + lines[i + 1] + "\n" + lines[i + 2];
            if (code.split(Pattern.quote(block)).length > 2) {
                report.addIssue("Potential code duplication detected. Consider extracting common functionality.");
                break;
            }
        }
    }

    /**
     * Check error handling
     */
    private void checkErrorHandling(String code, CodeQualityReport report) {
        if (!code.contains("try") && !code.contains("catch") && !code.contains("throws")) {
            report.addIssue("No error handling detected. Consider adding appropriate exception handling.");
        }
    }

    /**
     * Check documentation
     */
    private void checkDocumentation(String code, CodeQualityReport report) {
        if (!code.contains("/**") && !code.contains("/*") && !code.contains("//")) {
            report.addIssue("No documentation found. Consider adding comments and JavaDoc.");
        }
    }

    /**
     * Generate refactoring suggestions
     */
    private List<String> generateRefactoringSuggestions(ProductionCode productionCode, CodeQualityReport qualityReport) {
        if (qualityReport.getIssues().isEmpty()) {
            return List.of();
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Refactor the following code to address these quality issues:\n\n");
        
        for (String issue : qualityReport.getIssues()) {
            prompt.append("- ").append(issue).append("\n");
        }
        
        prompt.append("\nCurrent code:\n");
        prompt.append(productionCode.getCode()).append("\n\n");
        prompt.append("Requirements for refactoring:\n");
        prompt.append("- Maintain the same functionality\n");
        prompt.append("- Improve code quality and readability\n");
        prompt.append("- Follow best practices for ").append(config.getLanguage()).append("\n");
        prompt.append("- Add proper error handling\n");
        prompt.append("- Improve naming conventions\n");
        prompt.append("- Add appropriate documentation\n\n");
        prompt.append("Generate only the refactored code without any explanations.");

        try {
            String refactoredCode = llmService.generateCode(prompt.toString());
            return List.of("Code refactored to address quality issues");
        } catch (Exception e) {
            logger.error("Failed to generate refactoring suggestions", e);
            return qualityReport.getIssues();
        }
    }

    /**
     * Apply refactoring to the code
     */
    private String applyRefactoring(ProductionCode productionCode, List<String> suggestions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Refactor the following code to address these issues:\n\n");
        
        for (String suggestion : suggestions) {
            prompt.append("- ").append(suggestion).append("\n");
        }
        
        prompt.append("\nCurrent code:\n");
        prompt.append(productionCode.getCode()).append("\n\n");
        prompt.append("Generate the refactored code that addresses all the issues while maintaining functionality.");

        try {
            return llmService.generateCode(prompt.toString());
        } catch (Exception e) {
            logger.error("Failed to apply refactoring", e);
            return productionCode.getCode(); // Return original code if refactoring fails
        }
    }

    /**
     * Refactor multiple production code files
     */
    public List<ProductionCode> refactorCodeList(List<ProductionCode> productionCodes, List<GeneratedTest> tests) {
        logger.info("Refactoring {} production code files", productionCodes.size());
        
        return productionCodes.stream()
                .map(code -> {
                    GeneratedTest test = tests.stream()
                            .filter(t -> t.getRequirementId().equals(code.getRequirementId()))
                            .findFirst()
                            .orElse(null);
                    
                    return refactorCode(code, test);
                })
                .toList();
    }

    /**
     * Code quality report
     */
    private static class CodeQualityReport {
        private final List<String> issues = new java.util.ArrayList<>();

        public void addIssue(String issue) {
            issues.add(issue);
        }

        public List<String> getIssues() {
            return issues;
        }

        public boolean hasIssues() {
            return !issues.isEmpty();
        }
    }
} 