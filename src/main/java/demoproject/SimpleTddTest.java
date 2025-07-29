package demoproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;

/**
 * Simple test to run TDD agent and see what happens
 */
public class SimpleTddTest {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleTddTest.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Simple TDD Test...");
            
            // Create config
            AgentConfig config = new AgentConfig();
            config.setRequirementsFolder("./requirements");
            config.setOutputFolder("./generated-tests");
            config.setLlmProvider("google");
            config.setLlmModel("gemini-pro");
            config.setLlmApiKey("AIzaSyAxm3ARZByaUaOxZR76ErH8XC7DQ1RrcfQ");
            
            logger.info("Config created successfully");
            logger.info("API Key: {}", config.getLlmApiKey());
            logger.info("Provider: {}", config.getLlmProvider());
            logger.info("Model: {}", config.getLlmModel());
            
            // Create and run TDD agent
            TddAgent agent = new TddAgent(config);
            logger.info("TDD Agent created successfully");
            
            agent.run();
            logger.info("TDD Agent completed successfully");
            
        } catch (Exception e) {
            logger.error("Simple TDD Test failed", e);
            e.printStackTrace();
        }
    }
} 