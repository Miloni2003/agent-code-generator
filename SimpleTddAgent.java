package demoproject;

import java.util.List;

import demoproject.config.AgentConfig;
import demoproject.models.GeneratedTest;
import demoproject.models.Requirement;
import demoproject.services.RequirementsReader;
import demoproject.services.SimpleTestGenerator;

public class SimpleTddAgent {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Simple TDD Agent ===");
            System.out.println("Starting test generation...");
            
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            System.out.println("Configuration loaded successfully");
            System.out.println("LLM Provider: " + config.getLlmProvider());
            System.out.println("LLM Model: " + config.getLlmModel());
            System.out.println("API Key: " + config.getLlmApiKey().substring(0, 10) + "...");
            
            // Read requirements
            RequirementsReader reader = new RequirementsReader(config);
            List<Requirement> requirements = reader.readRequirements();
            
            System.out.println("Found " + requirements.size() + " requirements");
            
            if (requirements.isEmpty()) {
                System.out.println("❌ No requirements found. Please check the requirements folder.");
                return;
            }
            
            // Generate tests for each requirement
            SimpleTestGenerator testGenerator = new SimpleTestGenerator(config);
            
            for (Requirement req : requirements) {
                System.out.println("\n--- Processing Requirement: " + req.getId() + " ---");
                System.out.println("Title: " + req.getTitle());
                
                try {
                    GeneratedTest test = testGenerator.generateTests(req);
                    System.out.println("✅ Generated test: " + test.getFileName());
                    System.out.println("Test class: " + test.getTestClassName());
                    System.out.println("Status: " + test.getStatus());
                    
                    if (test.getErrorMessage() != null) {
                        System.out.println("⚠️ Error: " + test.getErrorMessage());
                    }
                    
                } catch (Exception e) {
                    System.out.println("❌ Failed to generate test for " + req.getId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("\n=== TDD Agent Completed ===");
            System.out.println("Check the generated-tests folder for output files.");
            
        } catch (Exception e) {
            System.err.println("❌ TDD Agent failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 