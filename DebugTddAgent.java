package demoproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import demoproject.config.AgentConfig;
import demoproject.services.RequirementsReader;
import demoproject.models.Requirement;
import java.util.List;

public class DebugTddAgent {
    private static final Logger logger = LoggerFactory.getLogger(DebugTddAgent.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting Debug TDD Agent");
            
            // Load configuration
            AgentConfig config = AgentConfig.fromArgs(args);
            logger.info("Configuration loaded successfully");
            
            // Test requirements reading
            RequirementsReader reader = new RequirementsReader(config);
            List<Requirement> requirements = reader.readRequirements();
            
            logger.info("Found {} requirements", requirements.size());
            
            for (Requirement req : requirements) {
                logger.info("Requirement: {} - {}", req.getId(), req.getTitle());
            }
            
            logger.info("Debug TDD Agent completed successfully");
            
        } catch (Exception e) {
            logger.error("Debug TDD Agent failed", e);
            e.printStackTrace();
        }
    }
} 