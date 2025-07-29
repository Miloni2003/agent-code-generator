package demoproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.Requirement;
import demoproject.models.TestResult;
import demoproject.models.ValidationReport;
import demoproject.services.CodeGenerator;
import demoproject.services.RefactorEngine;
import demoproject.services.RequirementsReader;
import demoproject.services.TestGenerator;
import demoproject.services.TestRunner;
import demoproject.services.Validator;

/**
 * Controller for TDD workflow orchestration
 * Handles the business logic and coordinates between services
 */
public class TddController {
    
    private static final Logger logger = LoggerFactory.getLogger(TddController.class);
    
    private final RequirementsReader requirementsReader;
    private final TestGenerator testGenerator;
    private final CodeGenerator codeGenerator;
    private final RefactorEngine refactorEngine;
    private final TestRunner testRunner;
    private final Validator validator;
    private final AgentConfig config;
    private final ExecutorService executorService;
    
    public TddController(AgentConfig config) {
        this.config = config;
        this.requirementsReader = new RequirementsReader(config);
        this.testGenerator = new TestGenerator(config);
        this.codeGenerator = new CodeGenerator(config);
        this.refactorEngine = new RefactorEngine(config);
        this.testRunner = new TestRunner(config);
        this.validator = new Validator(config);
        this.executorService = Executors.newFixedThreadPool(config.getMaxConcurrentTasks());
    }
    
    /**
     * Execute the complete TDD workflow
     */
    public TddWorkflowResult executeTddWorkflow() {
        logger.info("Starting TDD workflow execution");
        
        try {
            // Step 1: Parse Requirements
            List<Requirement> requirements = parseRequirements();
            logger.info("Parsed {} requirements", requirements.size());
            
            // Step 2: Generate Test Cases
            List<GeneratedTest> tests = generateTestCases(requirements);
            logger.info("Generated {} test cases", tests.size());
            
            // Step 3: Generate Production Code
            List<ProductionCode> productionCodes = generateProductionCode(requirements, tests);
            logger.info("Generated {} production code files", productionCodes.size());
            
            // Step 4: Refactor Code
            List<ProductionCode> refactoredCodes = refactorCode(productionCodes, tests);
            logger.info("Refactored {} production code files", refactoredCodes.size());
            
            // Step 5: Run Tests
            List<TestResult> testResults = runTests(tests, refactoredCodes);
            logger.info("Executed {} test runs", testResults.size());
            
            // Step 6: Validate Results
            List<ValidationReport> validationReports = validateResults(requirements, tests, refactoredCodes, testResults);
            logger.info("Generated {} validation reports", validationReports.size());
            
            return new TddWorkflowResult(requirements, tests, refactoredCodes, testResults, validationReports);
            
        } catch (Exception e) {
            logger.error("TDD workflow execution failed", e);
            throw new RuntimeException("TDD workflow execution failed", e);
        }
    }
    
    /**
     * Parse requirements from the configured folder
     */
    private List<Requirement> parseRequirements() {
        try {
            return requirementsReader.readRequirements();
        } catch (Exception e) {
            logger.error("Failed to parse requirements", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Generate test cases for the requirements
     */
    private List<GeneratedTest> generateTestCases(List<Requirement> requirements) {
        List<GeneratedTest> tests = new ArrayList<>();
        for (Requirement requirement : requirements) {
            tests.add(testGenerator.generateTests(requirement));
        }
        return tests;
    }
    
    /**
     * Generate production code based on requirements and tests
     */
    private List<ProductionCode> generateProductionCode(List<Requirement> requirements, List<GeneratedTest> tests) {
        return codeGenerator.generateCodeForRequirements(requirements, tests);
    }
    
    /**
     * Refactor the generated production code
     */
    private List<ProductionCode> refactorCode(List<ProductionCode> productionCodes, List<GeneratedTest> tests) {
        return refactorEngine.refactorCodeList(productionCodes, tests);
    }
    
    /**
     * Run tests against the production code
     */
    private List<TestResult> runTests(List<GeneratedTest> tests, List<ProductionCode> productionCodes) {
        return testRunner.runTestsForRequirements(tests, productionCodes);
    }
    
    /**
     * Validate the results against requirements
     */
    private List<ValidationReport> validateResults(List<Requirement> requirements, List<GeneratedTest> tests,
            List<ProductionCode> productionCodes, List<TestResult> testResults) {
        List<ValidationReport> reports = new ArrayList<>();
        for (int i = 0; i < requirements.size() && i < tests.size() && i < productionCodes.size() && i < testResults.size(); i++) {
            reports.add(validator.validateResults(requirements.get(i), tests.get(i), productionCodes.get(i), testResults.get(i)));
        }
        return reports;
    }
    
    /**
     * Shutdown the controller and release resources
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    
    /**
     * Result class to hold TDD workflow results
     */
    public static class TddWorkflowResult {
        private final List<Requirement> requirements;
        private final List<GeneratedTest> tests;
        private final List<ProductionCode> productionCodes;
        private final List<TestResult> testResults;
        private final List<ValidationReport> validationReports;
        
        public TddWorkflowResult(List<Requirement> requirements, List<GeneratedTest> tests, 
                               List<ProductionCode> productionCodes, List<TestResult> testResults,
                               List<ValidationReport> validationReports) {
            this.requirements = requirements;
            this.tests = tests;
            this.productionCodes = productionCodes;
            this.testResults = testResults;
            this.validationReports = validationReports;
        }
        
        // Getters
        public List<Requirement> getRequirements() { return requirements; }
        public List<GeneratedTest> getTests() { return tests; }
        public List<ProductionCode> getProductionCodes() { return productionCodes; }
        public List<TestResult> getTestResults() { return testResults; }
        public List<ValidationReport> getValidationReports() { return validationReports; }
    }
} 