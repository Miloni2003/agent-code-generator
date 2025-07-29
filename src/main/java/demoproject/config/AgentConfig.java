package demoproject.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Configuration class for the Test Generation Agent
 */
public class AgentConfig {

    private static final Logger logger = LoggerFactory.getLogger(AgentConfig.class);

    // Default values
    private static final String DEFAULT_REQUIREMENTS_FOLDER = "requirements";
    private static final String DEFAULT_OUTPUT_FOLDER = "generated-tests";
    private static final int DEFAULT_MAX_CONCURRENT_TASKS = 4;
    private static final String DEFAULT_TEST_FRAMEWORK = "junit5";
    private static final String DEFAULT_LANGUAGE = "java";

    // Configuration properties
    private String requirementsFolder;
    private String outputFolder;
    private int maxConcurrentTasks;
    private String testFramework;
    private String language;
    private String llmApiKey;
    private String llmApiUrl;
    private String llmModel;
    private String llmProvider; // New field for LLM provider
    private String gitRepositoryUrl;
    private String gitBranch;

    public AgentConfig() {
        // Set default values
        this.requirementsFolder = DEFAULT_REQUIREMENTS_FOLDER;
        this.outputFolder = DEFAULT_OUTPUT_FOLDER;
        this.maxConcurrentTasks = DEFAULT_MAX_CONCURRENT_TASKS;
        this.testFramework = DEFAULT_TEST_FRAMEWORK;
        this.language = DEFAULT_LANGUAGE;
    }

    /**
     * Create configuration from command line arguments
     */
    public static AgentConfig fromArgs(String[] args) {
        AgentConfig config = new AgentConfig();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "--requirements-folder":
                case "-r":
                    if (i + 1 < args.length) {
                        config.setRequirementsFolder(args[++i]);
                    }
                    break;
                case "--output-folder":
                case "-o":
                    if (i + 1 < args.length) {
                        config.setOutputFolder(args[++i]);
                    }
                    break;
                case "--max-tasks":
                    if (i + 1 < args.length) {
                        config.setMaxConcurrentTasks(Integer.parseInt(args[++i]));
                    }
                    break;
                case "--test-framework":
                    if (i + 1 < args.length) {
                        config.setTestFramework(args[++i]);
                    }
                    break;
                case "--language":
                case "-l":
                    if (i + 1 < args.length) {
                        config.setLanguage(args[++i]);
                    }
                    break;
                case "--llm-api-key":
                    if (i + 1 < args.length) {
                        config.setLlmApiKey(args[++i]);
                    }
                    break;
                case "--llm-api-url":
                    if (i + 1 < args.length) {
                        config.setLlmApiUrl(args[++i]);
                    }
                    break;
                case "--llm-model":
                    if (i + 1 < args.length) {
                        config.setLlmModel(args[++i]);
                    }
                    break;
                case "--llm-provider":
                    if (i + 1 < args.length) {
                        config.setLlmProvider(args[++i]);
                    }
                    break;
                case "--git-repo":
                    if (i + 1 < args.length) {
                        config.setGitRepositoryUrl(args[++i]);
                    }
                    break;
                case "--git-branch":
                    if (i + 1 < args.length) {
                        config.setGitBranch(args[++i]);
                    }
                    break;
                case "--config":
                case "-c":
                    if (i + 1 < args.length) {
                        config.loadFromFile(args[++i]);
                    }
                    break;
                case "--help":
                case "-h":
                    printUsage();
                    System.exit(0);
                    break;
            }
        }

        // Load from environment variables if not set
        config.loadFromEnvironment();

        // Validate configuration
        config.validate();

        return config;
    }

    /**
     * Load configuration from properties file
     */
    public void loadFromFile(String configFile) {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(fis);

            requirementsFolder = props.getProperty("requirements.folder", requirementsFolder);
            outputFolder = props.getProperty("output.folder", outputFolder);
            maxConcurrentTasks = Integer.parseInt(props.getProperty("max.concurrent.tasks", String.valueOf(maxConcurrentTasks)));
            testFramework = props.getProperty("test.framework", testFramework);
            language = props.getProperty("language", language);
            llmApiKey = props.getProperty("llm.api.key", llmApiKey);
            llmApiUrl = props.getProperty("llm.api.url", llmApiUrl);
            llmModel = props.getProperty("llm.model", llmModel);

            logger.info("Loaded configuration from file: {}", configFile);
        } catch (IOException e) {
            logger.error("Failed to load configuration from file: {}", configFile, e);
        }
    }

    /**
     * Load configuration from environment variables
     */
    public void loadFromEnvironment() {
        // Load from system environment variables
        requirementsFolder = System.getenv("TESTGEN_REQUIREMENTS_FOLDER") != null ? System.getenv("TESTGEN_REQUIREMENTS_FOLDER") : requirementsFolder;
        outputFolder = System.getenv("TESTGEN_OUTPUT_FOLDER") != null ? System.getenv("TESTGEN_OUTPUT_FOLDER") : outputFolder;
        llmApiKey = System.getenv("TESTGEN_LLM_API_KEY") != null ? System.getenv("TESTGEN_LLM_API_KEY") : llmApiKey;
        llmApiUrl = System.getenv("TESTGEN_LLM_API_URL") != null ? System.getenv("TESTGEN_LLM_API_URL") : llmApiUrl;
        llmModel = System.getenv("TESTGEN_LLM_MODEL") != null ? System.getenv("TESTGEN_LLM_MODEL") : llmModel;
        llmProvider = System.getenv("TESTGEN_LLM_PROVIDER") != null ? System.getenv("TESTGEN_LLM_PROVIDER") : llmProvider;
        gitRepositoryUrl = System.getenv("TESTGEN_GIT_REPOSITORY_URL") != null ? System.getenv("TESTGEN_GIT_REPOSITORY_URL") : gitRepositoryUrl;
        gitBranch = System.getenv("TESTGEN_GIT_BRANCH") != null ? System.getenv("TESTGEN_GIT_BRANCH") : gitBranch;
        
        String maxTasksStr = System.getenv("TESTGEN_MAX_CONCURRENT_TASKS");
        if (maxTasksStr != null) {
            try {
                maxConcurrentTasks = Integer.parseInt(maxTasksStr);
            } catch (NumberFormatException e) {
                logger.warn("Invalid TESTGEN_MAX_CONCURRENT_TASKS value: {}", maxTasksStr);
            }
        }
        
        testFramework = System.getenv("TESTGEN_TEST_FRAMEWORK") != null ? System.getenv("TESTGEN_TEST_FRAMEWORK") : testFramework;
        language = System.getenv("TESTGEN_LANGUAGE") != null ? System.getenv("TESTGEN_LANGUAGE") : language;
        
        // Also check for legacy environment variable names
        if (llmApiKey == null) llmApiKey = System.getenv("LLM_API_KEY");
        if (llmApiUrl == null) llmApiUrl = System.getenv("LLM_API_URL");
        if (llmModel == null) llmModel = System.getenv("LLM_MODEL");
    }

    /**
     * Print configuration summary for debugging
     */
    public void printConfigSummary() {
        logger.info("=== Agent Configuration Summary ===");
        logger.info("Requirements Folder: {}", requirementsFolder);
        logger.info("Output Folder: {}", outputFolder);
        logger.info("Max Concurrent Tasks: {}", maxConcurrentTasks);
        logger.info("Test Framework: {}", testFramework);
        logger.info("Language: {}", language);
        logger.info("LLM API Key configured: {}", llmApiKey != null && !llmApiKey.trim().isEmpty());
        logger.info("LLM API URL: {}", llmApiUrl);
        logger.info("LLM Model: {}", llmModel);
        logger.info("LLM Provider: {}", llmProvider != null ? llmProvider : "openai");
        logger.info("Git Repository URL: {}", gitRepositoryUrl);
        logger.info("Git Branch: {}", gitBranch);
        logger.info("=====================================");
    }

    /**
     * Validate configuration
     */
    public void validate() {
        if (requirementsFolder == null || requirementsFolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Requirements folder is required");
        }

        if (outputFolder == null || outputFolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Output folder is required");
        }

        if (maxConcurrentTasks <= 0) {
            throw new IllegalArgumentException("Max concurrent tasks must be greater than 0");
        }

        // Create directories if they don't exist
        try {
            Files.createDirectories(Paths.get(requirementsFolder));
            Files.createDirectories(Paths.get(outputFolder));
        } catch (IOException e) {
            logger.error("Failed to create directories", e);
        }
    }

    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Test Generation Agent Usage:");
        System.out.println("java -cp <classpath> demoproject.TestGenerationAgent [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -r, --requirements-folder <path>  Folder containing requirements (default: requirements)");
        System.out.println("  -o, --output-folder <path>        Output folder for generated tests (default: generated-tests)");
        System.out.println("      --max-tasks <number>          Max concurrent tasks (default: 4)");
        System.out.println("      --test-framework <framework>  Test framework (default: junit5)");
        System.out.println("  -l, --language <language>         Programming language (default: java)");
        System.out.println("      --llm-api-key <key>           LLM API key");
        System.out.println("      --llm-api-url <url>           LLM API URL");
        System.out.println("      --llm-model <model>           LLM model name");
        System.out.println("      --llm-provider <provider>     LLM provider (e.g., openai, anthropic)");
        System.out.println("      --git-repo <url>              Git repository URL");
        System.out.println("      --git-branch <branch>         Git branch (default: main)");
        System.out.println("  -c, --config <file>               Configuration file");
        System.out.println("  -h, --help                        Show this help message");
        System.out.println();
        System.out.println("Environment Variables:");
        System.out.println("  TESTGEN_REQUIREMENTS_FOLDER       Requirements folder");
        System.out.println("  TESTGEN_OUTPUT_FOLDER             Output folder");
        System.out.println("  TESTGEN_LLM_API_KEY               LLM API key");
        System.out.println("  TESTGEN_LLM_API_URL               LLM API URL");
        System.out.println("  TESTGEN_LLM_MODEL                 LLM model");
    }

    // Getters and Setters
    public String getRequirementsFolder() {
        return requirementsFolder;
    }

    public void setRequirementsFolder(String requirementsFolder) {
        this.requirementsFolder = requirementsFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public int getMaxConcurrentTasks() {
        return maxConcurrentTasks;
    }

    public void setMaxConcurrentTasks(int maxConcurrentTasks) {
        this.maxConcurrentTasks = maxConcurrentTasks;
    }

    public String getTestFramework() {
        return testFramework;
    }

    public void setTestFramework(String testFramework) {
        this.testFramework = testFramework;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLlmApiKey() {
        return llmApiKey;
    }

    public void setLlmApiKey(String llmApiKey) {
        this.llmApiKey = llmApiKey;
    }

    public String getLlmApiUrl() {
        return llmApiUrl;
    }

    public void setLlmApiUrl(String llmApiUrl) {
        this.llmApiUrl = llmApiUrl;
    }

    public String getLlmModel() {
        return llmModel;
    }

    public void setLlmModel(String llmModel) {
        this.llmModel = llmModel;
    }

    public String getLlmProvider() {
        return llmProvider;
    }

    public void setLlmProvider(String llmProvider) {
        this.llmProvider = llmProvider;
    }

    public String getGitRepositoryUrl() {
        return gitRepositoryUrl;
    }

    public void setGitRepositoryUrl(String gitRepositoryUrl) {
        this.gitRepositoryUrl = gitRepositoryUrl;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public void setGitBranch(String gitBranch) {
        this.gitBranch = gitBranch;
    }
}
