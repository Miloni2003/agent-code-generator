package demoproject;

import demoproject.config.AgentConfig;

public class TestApiKeyFormat {
    public static void main(String[] args) {
        try {
            System.out.println("=== API Key Format Test ===");
            
            AgentConfig config = new AgentConfig();
            
            System.out.println("API Key: " + config.getLlmApiKey());
            System.out.println("API URL: " + config.getLlmApiUrl());
            System.out.println("Provider: " + config.getLlmProvider());
            System.out.println("Model: " + config.getLlmModel());
            
            // Check if API key looks valid
            String apiKey = config.getLlmApiKey();
            if (apiKey != null && apiKey.startsWith("AIza")) {
                System.out.println("✅ API Key format looks correct (starts with AIza)");
            } else {
                System.out.println("❌ API Key format looks incorrect");
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 