package demoproject.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demoproject.config.AgentConfig;

/**
 * Simplified Git service that uses command-line Git operations
 */
public class GitService {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class);

    private final AgentConfig config;

    public GitService(AgentConfig config) {
        this.config = config;
    }

    /**
     * Initialize or clone the git repository
     */
    public void initializeRepository() throws IOException {
        Path outputPath = Paths.get(config.getOutputFolder());
        File gitDir = outputPath.resolve(".git").toFile();

        if (gitDir.exists()) {
            logger.info("Git repository already exists at: {}", outputPath);
        } else {
            logger.info("Initializing git repository at: {}", outputPath);
            executeGitCommand("init", outputPath.toString());

            if (config.getGitRepositoryUrl() != null) {
                executeGitCommand("remote", "add", "origin", config.getGitRepositoryUrl(), outputPath.toString());
            }
        }
    }

    /**
     * Add files to git staging area
     */
    public void addFiles(String folderPath) throws IOException {
        logger.info("Adding files from folder: {}", folderPath);
        executeGitCommand("add", ".", folderPath);
    }

    /**
     * Commit changes with a message
     */
    public void commit(String message) throws IOException {
        logger.info("Committing changes with message: {}", message);

        // Check if there are changes to commit
        String status = executeGitCommand("status", "--porcelain", config.getOutputFolder());
        if (!status.trim().isEmpty()) {
            executeGitCommand("commit", "-m", message, config.getOutputFolder());
            logger.info("Changes committed successfully");
        } else {
            logger.info("No changes to commit");
        }
    }

    /**
     * Push changes to remote repository
     */
    public void push() throws IOException {
        logger.info("Pushing changes to remote repository");

        try {
            executeGitCommand("push", "origin", config.getGitBranch(), config.getOutputFolder());
            logger.info("Changes pushed successfully");
        } catch (IOException e) {
            logger.error("Failed to push changes: {}", e.getMessage());

            // Try to pull first if push fails
            logger.info("Attempting to pull latest changes first");
            try {
                executeGitCommand("pull", "origin", config.getGitBranch(), config.getOutputFolder());
                executeGitCommand("push", "origin", config.getGitBranch(), config.getOutputFolder());
                logger.info("Changes pushed successfully after pull");
            } catch (IOException pullError) {
                logger.error("Failed to pull and push: {}", pullError.getMessage());
                throw new IOException("Failed to push changes", pullError);
            }
        }
    }

    /**
     * Execute git command
     */
    private String executeGitCommand(String... args) throws IOException {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(args);
            pb.directory(new File(args[args.length - 1]));

            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes());
            String error = new String(process.getErrorStream().readAllBytes());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                logger.error("Git command failed: {} - Error: {}", String.join(" ", args), error);
                throw new IOException("Git command failed: " + error);
            }

            return output;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Git command interrupted", e);
        }
    }

    /**
     * Check if git is available
     */
    public boolean isGitAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Close git resources (no-op for command-line version)
     */
    public void close() {
        // No resources to close for command-line version
    }
}
