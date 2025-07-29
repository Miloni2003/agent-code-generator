package demoproject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.services.RequirementsReader;
import demoproject.services.LlmService;

/**
 * Simple debug agent to test components step by step
 */
public class DebugAgent {

    private static final Logger logger = LoggerFactory.getLogger(DebugAgent.class);

    public static void main(String[] args) {
        try {
            System.out.println("=== DEBUG AGENT STARTING ===");
            
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            System.out.println("✅ Configuration loaded");
            System.out.println("   Requirements folder: " + config.getRequirementsFolder());
            System.out.println("   Output folder: " + config.getOutputFolder());
            System.out.println("   API Key: " + (config.getLlmApiKey() != null && !config.getLlmApiKey().isEmpty() ? "SET" : "NOT SET"));
            
            // Test requirements reading
            System.out.println("\n🔄 Testing requirements reading...");
            RequirementsReader reader = new RequirementsReader(config);
            List<demoproject.models.Requirement> requirements = reader.readRequirements();
            System.out.println("✅ Found " + requirements.size() + " requirements");
            
            if (requirements.isEmpty()) {
                System.out.println("❌ No requirements found! This is why no code is generated.");
                System.out.println("   Check if requirements folder exists: " + config.getRequirementsFolder());
                return;
            }
            
            // Test LLM service
            System.out.println("\n🔄 Testing LLM service...");
            LlmService llmService = new LlmService(config);
            boolean apiConfigured = llmService.isApiConfigured();
            System.out.println("✅ API configured: " + apiConfigured);
            
            if (!apiConfigured) {
                System.out.println("❌ API not configured! This is why no code is generated.");
                return;
            }
            
            // Test API connection
            System.out.println("\n🔄 Testing API connection...");
            boolean connectionOk = llmService.testApiConnection();
            System.out.println("✅ API connection: " + connectionOk);
            
            if (!connectionOk) {
                System.out.println("❌ API connection failed! This is why no code is generated.");
                return;
            }
            
            // Test code generation
            System.out.println("\n🔄 Testing code generation...");
            String testPrompt = "Generate a simple Java method that adds two integers.";
            String generatedCode = llmService.generateCode(testPrompt);
            System.out.println("✅ Code generation: " + (generatedCode != null && !generatedCode.isEmpty() ? "SUCCESS" : "FAILED"));
            System.out.println("   Generated code length: " + (generatedCode != null ? generatedCode.length() : 0) + " characters");
            
            System.out.println("\n=== DEBUG COMPLETE ===");
            
        } catch (Exception e) {
            System.err.println("❌ Debug failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 