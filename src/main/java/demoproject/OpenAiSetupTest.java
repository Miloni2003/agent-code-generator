package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class OpenAiSetupTest {
    public static void main(String[] args) {
        System.out.println("=== OpenAI Integration Test ===");
        try {
            AgentConfig config = AgentConfig.fromArgs(args);
            LlmService llmService = new LlmService(config);
            System.out.println("Testing API configuration...");
            boolean isConfigured = llmService.isApiConfigured();
            System.out.println("  API Configured: " + isConfigured);
            if (!isConfigured) {
                System.out.println("  Please check your config.properties for OpenAI settings.");
                return;
            }
            System.out.println("Testing API connection...");
            boolean ok = llmService.testApiConnection();
            System.out.println("  API Connection: " + (ok ? "SUCCESS" : "FAILED"));
            if (ok) {
                String prompt = "Generate a simple Java method that adds two integers.";
                String response = llmService.generateCode(prompt);
                System.out.println("\nGenerated Code:\n" + response);
            } else {
                System.out.println("  OpenAI API call failed. Check your API key, quota, or network.");
            }
        } catch (Exception e) {
            System.err.println("Error testing OpenAI: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Test completed.");
    }
} 