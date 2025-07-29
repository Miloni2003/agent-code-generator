package demoproject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.services.LlmService;

/**
 * Main entry point for the TDD Agent
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting TDD Agent...");
            
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            config.loadFromEnvironment(); // Load from environment variables
            config.printConfigSummary(); // Print configuration for debugging
            logger.info("Configuration loaded successfully");
            
            // Test LLM connection first
            LlmService llmService = new LlmService(config);
            
            if (llmService.isApiConfigured()) {
                logger.info("Testing LLM API connection...");
                boolean connected = llmService.testApiConnection();
                logger.info("LLM API connection test result: {}", connected);
                
                if (!connected) {
                    logger.error("LLM API connection failed. Please check your API key and configuration.");
                    System.exit(1);
                }
            } else {
                logger.error("LLM API not properly configured. Please set TESTGEN_LLM_API_KEY, TESTGEN_LLM_API_URL, and TESTGEN_LLM_MODEL environment variables.");
                System.exit(1);
            }
            
            // Create and run TDD agent
            TddAgent tddAgent = new TddAgent(config);
            tddAgent.run();
            
            logger.info("TDD Agent completed successfully");
            
        } catch (Exception e) {
            logger.error("Error running TDD Agent", e);
            System.exit(1);
        }
    }
}
