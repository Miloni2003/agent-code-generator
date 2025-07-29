package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class TestLlmSimple {
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing LLM Service ===");
            
            AgentConfig config = new AgentConfig();
            LlmService llmService = new LlmService(config);
            
            System.out.println("API Key configured: " + llmService.isApiConfigured());
            System.out.println("API URL: " + config.getLlmApiUrl());
            System.out.println("API Key: " + config.getLlmApiKey().substring(0, 10) + "...");
            
            String simplePrompt = "Write a Java method that adds two integers. Return only the method code, no explanations.";
            
            System.out.println("\nSending prompt: " + simplePrompt);
            
            String response = llmService.generateCode(simplePrompt);
            
            System.out.println("\n=== RESPONSE ===");
            System.out.println(response);
            System.out.println("=== END RESPONSE ===");
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 