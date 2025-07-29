package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class ApiKeyTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== API Key Test ===");
            
            AgentConfig config = new AgentConfig();
            LlmService llmService = new LlmService(config);
            
            System.out.println("API Key configured: " + llmService.isApiConfigured());
            System.out.println("API URL: " + config.getLlmApiUrl());
            System.out.println("API Key: " + config.getLlmApiKey().substring(0, 10) + "...");
            
            String testPrompt = "Write a simple Java method that adds two integers. Return only the method code.";
            
            System.out.println("\nTesting API call...");
            String response = llmService.generateCode(testPrompt);
            
            System.out.println("\n=== API RESPONSE ===");
            System.out.println("Response length: " + response.length());
            System.out.println("Response: " + response);
            System.out.println("=== END RESPONSE ===");
            
            if (response.contains("public") || response.contains("class") || response.contains("method")) {
                System.out.println("✅ API is working - received Java code!");
            } else {
                System.out.println("❌ API not working - no Java code received");
            }
            
        } catch (Exception e) {
            System.err.println("❌ API Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 