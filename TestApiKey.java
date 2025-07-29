package demoproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

public class TestApiKey {
    private static final Logger logger = LoggerFactory.getLogger(TestApiKey.class);

    public static void main(String[] args) {
        try {
            logger.info("Testing API Key...");
            
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            logger.info("Configuration loaded");
            logger.info("LLM Provider: {}", config.getLlmProvider());
            logger.info("LLM Model: {}", config.getLlmModel());
            logger.info("API Key: {}", config.getLlmApiKey().substring(0, 10) + "...");
            
            // Test LLM service
            LlmService llmService = new LlmService(config);
            
            // Simple test prompt
            String testPrompt = "Generate a simple JUnit test for a calculator add method.";
            logger.info("Sending test prompt to LLM...");
            
            String response = llmService.generateTestCode(testPrompt);
            
            logger.info("LLM Response received!");
            logger.info("Response length: {} characters", response.length());
            logger.info("First 200 characters: {}", response.substring(0, Math.min(200, response.length())));
            
            logger.info("API Key test completed successfully!");
            
        } catch (Exception e) {
            logger.error("API Key test failed", e);
            e.printStackTrace();
        }
    }
} 