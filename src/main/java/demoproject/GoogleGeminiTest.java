package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class GoogleGeminiTest {
    public static void main(String[] args) {
        System.out.println("=== Google Gemini Integration Test ===");
        try {
            AgentConfig config = AgentConfig.fromArgs(args);
            LlmService llmService = new LlmService(config);
            
            System.out.println("Configuration loaded:");
            System.out.println("  LLM Provider: " + config.getLlmProvider());
            System.out.println("  LLM API URL: " + config.getLlmApiUrl());
            System.out.println("  LLM Model: " + config.getLlmModel());
            System.out.println("  API Key: " + (config.getLlmApiKey() != null && !config.getLlmApiKey().isEmpty() && !config.getLlmApiKey().equals("YOUR_GOOGLE_API_KEY_HERE") ? "SET" : "NOT SET"));
            
            System.out.println("\nTesting API configuration...");
            boolean isConfigured = llmService.isApiConfigured();
            System.out.println("  API Configured: " + isConfigured);
            
            if (isConfigured) {
                System.out.println("\nTesting API connection...");
                boolean connectionOk = llmService.testApiConnection();
                System.out.println("  API Connection: " + (connectionOk ? "SUCCESS" : "FAILED"));
                
                if (connectionOk) {
                    System.out.println("\n✅ Google Gemini is working! Testing code generation...");
                    String prompt = "Generate a simple Java method that adds two integers.";
                    String response = llmService.generateCode(prompt);
                    System.out.println("  Generated Code Length: " + (response != null ? response.length() : 0) + " characters");
                    if (response != null && response.length() > 0) {
                        System.out.println("  First 200 chars: " + response.substring(0, Math.min(200, response.length())));
                    }
                } else {
                    System.out.println("\n❌ Google Gemini connection failed. Please check:");
                    System.out.println("  1. Is your Google API key valid?");
                    System.out.println("  2. Check your network connection");
                    System.out.println("  3. Verify the API key has access to Gemini API");
                }
            } else {
                System.out.println("\n❌ Google Gemini not configured properly. Check config.properties");
                System.out.println("  Make sure to replace 'YOUR_GOOGLE_API_KEY_HERE' with your actual API key");
            }
            
        } catch (Exception e) {
            System.err.println("Error testing Google Gemini: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Google Gemini Setup Instructions ===");
        System.out.println("1. Go to https://makersuite.google.com/app/apikey");
        System.out.println("2. Create a new API key");
        System.out.println("3. Replace 'YOUR_GOOGLE_API_KEY_HERE' in config.properties");
        System.out.println("4. Run this test again");
        System.out.println("\nTest completed.");
    }
} 