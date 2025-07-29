package demoproject;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class DebugLlmResponse {
    public static void main(String[] args) {
        try {
            System.out.println("=== Debugging LLM Response ===");
            
            AgentConfig config = new AgentConfig();
            LlmService llmService = new LlmService(config);
            
            System.out.println("API Key configured: " + llmService.isApiConfigured());
            
            String testPrompt = "Generate a simple Java method that adds two integers and returns the result.";
            
            System.out.println("\nSending prompt to Google Gemini:");
            System.out.println(testPrompt);
            
            String response = llmService.generateCode(testPrompt);
            
            System.out.println("\n=== RAW RESPONSE FROM GOOGLE GEMINI ===");
            System.out.println(response);
            System.out.println("=== END RESPONSE ===");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 