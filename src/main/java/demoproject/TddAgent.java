package demoproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;
import demoproject.controller.TddController;
import demoproject.view.TddView;

/**
 * Main TDD Agent that orchestrates the complete Test-Driven Development
 * workflow using MVC pattern. Implements the full TDD cycle: 
 * Requirements → Tests → Code → Refactor → Test → Validate
 */
public class TddAgent {

    private static final Logger logger = LoggerFactory.getLogger(TddAgent.class);

    private final TddController controller;
    private final TddView view;
    private final AgentConfig config;

    public TddAgent(AgentConfig config) {
        this.config = config;
        this.controller = new TddController(config);
        this.view = new TddView();
    }

    /**
     * Main method to run the TDD agent
     */
    public static void main(String[] args) {
        try {
            AgentConfig config = AgentConfig.fromArgs(args);
            TddAgent agent = new TddAgent(config);
            agent.run();
        } catch (Exception e) {
            logger.error("Failed to run TDD agent", e);
            System.exit(1);
        }
    }

    /**
     * Main execution method that orchestrates the complete TDD workflow
     */
    public void run() {
        try {
            // Display header and configuration
            view.displayHeader();
            view.displayConfiguration(
                config.getRequirementsFolder(),
                config.getOutputFolder(),
                config.getLlmProvider() != null ? config.getLlmProvider() : "google",
                config.getLlmModel(),
                config.getLlmApiKey() != null && !config.getLlmApiKey().isEmpty() && 
                !config.getLlmApiKey().equals("YOUR_GOOGLE_API_KEY_HERE")
            );

            // Execute TDD workflow
            view.displayProgress("Starting", "TDD workflow execution");
            TddController.TddWorkflowResult result = controller.executeTddWorkflow();

            // Display results
            view.displayRequirements(result.getRequirements());
            view.displayTestSummary(result.getTests());
            view.displayCodeSummary(result.getProductionCodes());
            view.displayTestResults(result.getTestResults());
            view.displayValidationSummary(result.getValidationReports());
            view.displayFinalSummary(
                result.getRequirements(),
                result.getTests(),
                result.getProductionCodes(),
                result.getTestResults(),
                result.getValidationReports()
            );

            view.displaySuccess("TDD workflow completed successfully!");

        } catch (Exception e) {
            logger.error("TDD workflow failed", e);
            view.displayError("TDD workflow execution failed", e);
            System.exit(1);
        } finally {
            controller.shutdown();
        }
    }

    /**
     * Shutdown the agent and release resources
     */
    public void shutdown() {
        if (controller != null) {
            controller.shutdown();
        }
    }
}
