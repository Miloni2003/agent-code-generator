package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class LlmApiKeyTest {
    public static void main(String[] args) {
        System.out.println("Testing LLM API Integration...");
        
        try {
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            
            System.out.println("Configuration loaded:");
            System.out.println("  LLM Provider: " + config.getLlmProvider());
            System.out.println("  LLM API URL: " + config.getLlmApiUrl());
            System.out.println("  LLM Model: " + config.getLlmModel());
            System.out.println("  API Key: " + (config.getLlmApiKey() != null && !config.getLlmApiKey().isEmpty() ? "SET" : "NOT SET"));
            
            // Create LLM service
            LlmService llmService = new LlmService(config);
            
            System.out.println("\nTesting API configuration...");
            boolean isConfigured = llmService.isApiConfigured();
            System.out.println("  API Configured: " + isConfigured);
            
            if (isConfigured) {
                System.out.println("\nTesting API connection...");
                boolean connectionOk = llmService.testApiConnection();
                System.out.println("  API Connection: " + (connectionOk ? "SUCCESS" : "FAILED"));
                
                if (connectionOk) {
                    System.out.println("\nTesting code generation...");
                    String prompt = "Generate a simple Java method that adds two integers.";
                    String response = llmService.generateCode(prompt);
                    System.out.println("  Generated Code Length: " + (response != null ? response.length() : 0) + " characters");
                    if (response != null && response.length() > 0) {
                        System.out.println("  First 200 chars: " + response.substring(0, Math.min(200, response.length())));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error testing LLM API: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nTest completed.");
    }
} 