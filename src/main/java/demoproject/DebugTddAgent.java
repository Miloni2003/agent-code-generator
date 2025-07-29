package demoproject;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.Requirement;
import demoproject.models.TestResult;
import demoproject.models.ValidationReport;
import demoproject.services.CodeGenerator;
import demoproject.services.RequirementsReader;
import demoproject.services.TestGenerator;
import demoproject.services.TestRunner;
import demoproject.services.Validator;

/**
 * Debug class to test TDD agent step by step
 */
public class DebugTddAgent {
    
    private static final Logger logger = LoggerFactory.getLogger(DebugTddAgent.class);
    
    public static void main(String[] args) {
        try {
            logger.info("=== Starting Debug TDD Agent ===");
            
            // Step 1: Create config
            logger.info("Step 1: Creating config...");
            AgentConfig config = new AgentConfig();
            config.setRequirementsFolder("./requirements");
            config.setOutputFolder("./generated-tests");
            config.setLlmProvider("google");
            config.setLlmModel("gemini-pro");
            config.setLlmApiKey("AIzaSyAxm3ARZByaUaOxZR76ErH8XC7DQ1RrcfQ");
            logger.info("✅ Config created successfully");
            
            // Step 2: Read requirements
            logger.info("Step 2: Reading requirements...");
            RequirementsReader reader = new RequirementsReader(config);
            List<Requirement> requirements = reader.readRequirements();
            logger.info("✅ Found {} requirements", requirements.size());
            
            for (Requirement req : requirements) {
                logger.info("  - Requirement: {}", req.getTitle());
            }
            
            // Step 3: Generate tests
            logger.info("Step 3: Generating tests...");
            TestGenerator testGenerator = new TestGenerator(config);
            List<GeneratedTest> tests = new ArrayList<>();
            for (Requirement req : requirements) {
                GeneratedTest test = testGenerator.generateTests(req);
                tests.add(test);
            }
            logger.info("✅ Generated {} test cases", tests.size());
            
            // Step 4: Generate code
            logger.info("Step 4: Generating code...");
            CodeGenerator codeGenerator = new CodeGenerator(config);
            List<ProductionCode> codes = new ArrayList<>();
            for (int i = 0; i < requirements.size() && i < tests.size(); i++) {
                ProductionCode code = codeGenerator.generateCode(requirements.get(i), tests.get(i));
                codes.add(code);
            }
            logger.info("✅ Generated {} production codes", codes.size());
            
            // Step 5: Run tests
            logger.info("Step 5: Running tests...");
            TestRunner testRunner = new TestRunner(config);
            List<TestResult> results = new ArrayList<>();
            for (int i = 0; i < tests.size() && i < codes.size(); i++) {
                TestResult result = testRunner.runTests(tests.get(i), codes.get(i));
                results.add(result);
            }
            logger.info("✅ Ran {} tests", results.size());
            
            // Step 6: Validate
            logger.info("Step 6: Validating results...");
            Validator validator = new Validator(config);
            List<ValidationReport> reports = validator.validateMultipleRequirements(requirements, tests, codes, results);
            logger.info("✅ Generated {} validation reports", reports.size());
            
            logger.info("=== Debug TDD Agent completed successfully ===");
            
        } catch (Exception e) {
            logger.error("❌ Debug TDD Agent failed", e);
            e.printStackTrace();
        }
    }
} 