package demoproject.view;

import java.util.List;

import demoproject.models.GeneratedTest;
import demoproject.models.ProductionCode;
import demoproject.models.Requirement;
import demoproject.models.TestResult;
import demoproject.models.ValidationReport;

/**
 * View class for displaying TDD workflow results and user interface
 * Handles all output formatting and user interaction
 */
public class TddView {
    
    /**
     * Display the TDD workflow header
     */
    public void displayHeader() {
        System.out.println("=".repeat(80));
        System.out.println("                    TDD AGENT - TEST DRIVEN DEVELOPMENT");
        System.out.println("=".repeat(80));
        System.out.println("Workflow: Requirements → Tests → Code → Refactor → Test → Validate");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Display configuration summary
     */
    public void displayConfiguration(String requirementsFolder, String outputFolder, 
                                  String llmProvider, String llmModel, boolean apiKeySet) {
        System.out.println("\n📋 CONFIGURATION:");
        System.out.println("   Requirements Folder: " + requirementsFolder);
        System.out.println("   Output Folder: " + outputFolder);
        System.out.println("   LLM Provider: " + llmProvider);
        System.out.println("   LLM Model: " + llmModel);
        System.out.println("   API Key: " + (apiKeySet ? "✅ SET" : "❌ NOT SET"));
        System.out.println();
    }
    
    /**
     * Display workflow progress
     */
    public void displayProgress(String step, String message) {
        System.out.println("🔄 " + step + ": " + message);
    }
    
    /**
     * Display requirements summary
     */
    public void displayRequirements(List<Requirement> requirements) {
        System.out.println("\n📝 REQUIREMENTS (" + requirements.size() + "):");
        for (int i = 0; i < requirements.size(); i++) {
            Requirement req = requirements.get(i);
            System.out.println("   " + (i + 1) + ". " + req.getId() + " - " + req.getTitle());
        }
        System.out.println();
    }
    
    /**
     * Display test generation summary
     */
    public void displayTestSummary(List<GeneratedTest> tests) {
        System.out.println("\n🧪 TESTS (" + tests.size() + "):");
        for (int i = 0; i < tests.size(); i++) {
            GeneratedTest test = tests.get(i);
            System.out.println("   " + (i + 1) + ". " + test.getTestName() + " (" + test.getStatus() + ")");
        }
        System.out.println();
    }
    
    /**
     * Display production code summary
     */
    public void displayCodeSummary(List<ProductionCode> codes) {
        System.out.println("\n💻 PRODUCTION CODE (" + codes.size() + "):");
        for (int i = 0; i < codes.size(); i++) {
            ProductionCode code = codes.get(i);
            System.out.println("   " + (i + 1) + ". " + code.getClassName() + " (" + code.getStatus() + ")");
        }
        System.out.println();
    }
    
    /**
     * Display test results summary
     */
    public void displayTestResults(List<TestResult> results) {
        System.out.println("\n✅ TEST RESULTS (" + results.size() + "):");
        int passed = 0, failed = 0;
        
        for (TestResult result : results) {
            if (result.isSuccess()) {
                passed++;
            } else {
                failed++;
            }
        }
        
        System.out.println("   ✅ Passed: " + passed);
        System.out.println("   ❌ Failed: " + failed);
        System.out.println();
    }
    
    /**
     * Display validation summary
     */
    public void displayValidationSummary(List<ValidationReport> reports) {
        System.out.println("\n📊 VALIDATION REPORTS (" + reports.size() + "):");
        for (int i = 0; i < reports.size(); i++) {
            ValidationReport report = reports.get(i);
            System.out.println("   " + (i + 1) + ". " + report.getRequirementId() + " - " + report.getStatus());
        }
        System.out.println();
    }
    
    /**
     * Display final summary
     */
    public void displayFinalSummary(List<Requirement> requirements, List<GeneratedTest> tests,
                                  List<ProductionCode> codes, List<TestResult> results,
                                  List<ValidationReport> reports) {
        System.out.println("=".repeat(80));
        System.out.println("                           FINAL SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("📝 Requirements Processed: " + requirements.size());
        System.out.println("🧪 Tests Generated: " + tests.size());
        System.out.println("💻 Code Files Created: " + codes.size());
        System.out.println("✅ Test Runs: " + results.size());
        System.out.println("📊 Validation Reports: " + reports.size());
        
        // Calculate success rate
        long passedTests = results.stream().filter(r -> r.isSuccess()).count();
        double successRate = results.isEmpty() ? 0 : (double) passedTests / results.size() * 100;
        
        System.out.println("📈 Test Success Rate: " + String.format("%.1f%%", successRate));
        System.out.println("=".repeat(80));
    }
    
    /**
     * Display error message
     */
    public void displayError(String message, Exception e) {
        System.err.println("\n❌ ERROR: " + message);
        if (e != null) {
            System.err.println("   Details: " + e.getMessage());
        }
        System.err.println();
    }
    
    /**
     * Display success message
     */
    public void displaySuccess(String message) {
        System.out.println("\n✅ SUCCESS: " + message);
    }
    
    /**
     * Display warning message
     */
    public void displayWarning(String message) {
        System.out.println("\n⚠️  WARNING: " + message);
    }
    
    /**
     * Display help information
     */
    public void displayHelp() {
        System.out.println("\n📖 TDD AGENT HELP:");
        System.out.println("   This agent implements Test-Driven Development workflow:");
        System.out.println("   1. Parse requirements from the requirements folder");
        System.out.println("   2. Generate test cases using Google Gemini");
        System.out.println("   3. Generate production code to make tests pass");
        System.out.println("   4. Refactor code for better quality");
        System.out.println("   5. Run tests to verify functionality");
        System.out.println("   6. Validate results against requirements");
        System.out.println();
        System.out.println("   Configuration:");
        System.out.println("   - Set your Google API key in config.properties");
        System.out.println("   - Place requirements in the requirements folder");
        System.out.println("   - Generated code will be in the output folder");
        System.out.println();
    }
} 