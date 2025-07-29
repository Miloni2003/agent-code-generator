package demoproject.services;

import demoproject.config.AgentConfig;
import demoproject.models.Requirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for reading requirements from various file formats
 */
public class RequirementsReader {

    private static final Logger logger = LoggerFactory.getLogger(RequirementsReader.class);

    private final AgentConfig config;
    private final Map<String, RequirementParser> parsers;

    public RequirementsReader(AgentConfig config) {
        this.config = config;
        this.parsers = initializeParsers();
    }

    /**
     * Read all requirements from the configured folder
     */
    public List<Requirement> readRequirements() throws IOException {
        List<Requirement> requirements = new ArrayList<>();
        Path requirementsPath = Paths.get(config.getRequirementsFolder());

        if (!Files.exists(requirementsPath)) {
            logger.warn("Requirements folder does not exist: {}", requirementsPath);
            return requirements;
        }

        logger.info("Reading requirements from: {}", requirementsPath);

        Files.walkFileTree(requirementsPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (isRequirementsFile(file)) {
                    try {
                        List<Requirement> fileRequirements = parseRequirementsFile(file);
                        requirements.addAll(fileRequirements);
                        logger.debug("Parsed {} requirements from file: {}", fileRequirements.size(), file);
                    } catch (Exception e) {
                        logger.error("Failed to parse requirements file: {}", file, e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        logger.info("Total requirements found: {}", requirements.size());
        return requirements;
    }

    /**
     * Check if a file is a requirements file
     */
    private boolean isRequirementsFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return fileName.endsWith(".md")
                || fileName.endsWith(".txt")
                || fileName.endsWith(".json")
                || fileName.endsWith(".yaml")
                || fileName.endsWith(".yml")
                || fileName.endsWith(".req")
                || fileName.endsWith(".spec");
    }

    /**
     * Parse a requirements file based on its extension
     */
    private List<Requirement> parseRequirementsFile(Path file) throws IOException {
        String fileName = file.getFileName().toString().toLowerCase();
        String content = Files.readString(file);

        if (fileName.endsWith(".json")) {
            return parseJsonRequirements(content, file);
        } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            return parseYamlRequirements(content, file);
        } else {
            return parseTextRequirements(content, file);
        }
    }

    /**
     * Parse requirements from JSON format
     */
    private List<Requirement> parseJsonRequirements(String content, Path file) {
        List<Requirement> requirements = new ArrayList<>();

        try {
            // Simple JSON parsing - in a real implementation, you'd use Jackson or Gson
            Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\".*?\"title\"\\s*:\\s*\"([^\"]+)\".*?\"description\"\\s*:\\s*\"([^\"]+)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                Requirement req = new Requirement();
                req.setId(matcher.group(1));
                req.setTitle(matcher.group(2));
                req.setDescription(matcher.group(3));
                req.setSourceFile(file.toString());
                requirements.add(req);
            }
        } catch (Exception e) {
            logger.error("Failed to parse JSON requirements from file: {}", file, e);
        }

        return requirements;
    }

    /**
     * Parse requirements from YAML format
     */
    private List<Requirement> parseYamlRequirements(String content, Path file) {
        List<Requirement> requirements = new ArrayList<>();

        try {
            // Simple YAML parsing - in a real implementation, you'd use SnakeYAML
            Pattern pattern = Pattern.compile("id:\\s*([^\\n]+).*?title:\\s*([^\\n]+).*?description:\\s*([^\\n]+)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                Requirement req = new Requirement();
                req.setId(matcher.group(1).trim());
                req.setTitle(matcher.group(2).trim());
                req.setDescription(matcher.group(3).trim());
                req.setSourceFile(file.toString());
                requirements.add(req);
            }
        } catch (Exception e) {
            logger.error("Failed to parse YAML requirements from file: {}", file, e);
        }

        return requirements;
    }

    /**
     * Parse requirements from text format (Markdown, plain text, etc.)
     */
    private List<Requirement> parseTextRequirements(String content, Path file) {
        List<Requirement> requirements = new ArrayList<>();

        try {
            // Parse different text formats
            if (content.contains("##") || content.contains("#")) {
                // Markdown format
                requirements.addAll(parseMarkdownRequirements(content, file));
            } else {
                // Plain text format
                requirements.addAll(parsePlainTextRequirements(content, file));
            }
        } catch (Exception e) {
            logger.error("Failed to parse text requirements from file: {}", file, e);
        }

        return requirements;
    }

    /**
     * Parse requirements from Markdown format
     */
    private List<Requirement> parseMarkdownRequirements(String content, Path file) {
        List<Requirement> requirements = new ArrayList<>();

        // Split by headers
        String[] sections = content.split("(?=^#{1,3}\\s)");

        for (String section : sections) {
            if (section.trim().isEmpty()) {
                continue;
            }

            try {
                Requirement req = parseMarkdownSection(section, file);
                if (req != null) {
                    requirements.add(req);
                }
            } catch (Exception e) {
                logger.debug("Failed to parse markdown section: {}", section.substring(0, Math.min(100, section.length())), e);
            }
        }

        return requirements;
    }

    /**
     * Parse a single markdown section into a requirement
     */
    private Requirement parseMarkdownSection(String section, Path file) {
        String[] lines = section.split("\n");
        if (lines.length < 2) {
            return null;
        }

        // Extract header (ID and title)
        String header = lines[0].replaceAll("^#{1,3}\\s*", "").trim();
        if (header.isEmpty()) {
            return null;
        }

        // Parse ID and title from header (format: "REQ-001: Title")
        String id = null;
        String title = header;
        if (header.contains(":")) {
            String[] parts = header.split(":", 2);
            id = parts[0].trim();
            title = parts[1].trim();
        } else {
            id = generateIdFromTitle(header);
        }

        // Initialize requirement
        Requirement req = new Requirement();
        req.setId(id);
        req.setTitle(title);
        req.setSourceFile(file.toString());

        // Parse structured content
        String currentSection = null;
        StringBuilder currentContent = new StringBuilder();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            
            if (line.startsWith("##")) {
                // Save previous section content
                saveSectionContent(req, currentSection, currentContent.toString().trim());
                
                // Start new section
                currentSection = line.replaceAll("^##\\s*", "").toLowerCase();
                currentContent.setLength(0);
            } else if (line.startsWith("- ") || line.startsWith("* ")) {
                // Handle list items (for acceptance criteria, dependencies)
                String item = line.substring(2);
                if ("acceptance criteria".equals(currentSection)) {
                    req.addAcceptanceCriterion(item);
                } else if ("dependencies".equals(currentSection)) {
                    req.addDependency(item);
                }
            } else if (!line.isEmpty() && !line.equals("---")) {
                // Regular content
                currentContent.append(line).append(" ");
            }
        }

        // Save last section content
        saveSectionContent(req, currentSection, currentContent.toString().trim());

        return req;
    }

    /**
     * Save content to the appropriate field based on section name
     */
    private void saveSectionContent(Requirement req, String section, String content) {
        if (section == null || content.isEmpty()) {
            return;
        }

        switch (section.toLowerCase()) {
            case "user story":
                // User story becomes part of description
                String currentDesc = req.getDescription();
                req.setDescription((currentDesc != null ? currentDesc + "\n\n" : "") + "User Story: " + content);
                break;
            case "description":
                req.setDescription(content);
                break;
            case "priority":
                req.setPriority(content);
                break;
            case "type":
                req.setType(content);
                break;
            case "module":
                req.setModule(content);
                break;
            case "assignee":
                req.setAssignee(content);
                break;
            case "status":
                req.setStatus(content);
                break;
        }
    }

    /**
     * Parse requirements from plain text format
     */
    private List<Requirement> parsePlainTextRequirements(String content, Path file) {
        List<Requirement> requirements = new ArrayList<>();

        // Split by lines and look for requirement patterns
        String[] lines = content.split("\n");
        StringBuilder currentRequirement = new StringBuilder();
        String currentTitle = null;

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) {
                // End of current requirement
                if (currentTitle != null && currentRequirement.length() > 0) {
                    Requirement req = new Requirement();
                    req.setId(generateIdFromTitle(currentTitle));
                    req.setTitle(currentTitle);
                    req.setDescription(currentRequirement.toString().trim());
                    req.setSourceFile(file.toString());
                    requirements.add(req);

                    currentTitle = null;
                    currentRequirement.setLength(0);
                }
            } else if (line.matches("^REQ-\\d+.*") || line.matches("^Requirement\\s+\\d+.*")) {
                // New requirement
                if (currentTitle != null && currentRequirement.length() > 0) {
                    Requirement req = new Requirement();
                    req.setId(generateIdFromTitle(currentTitle));
                    req.setTitle(currentTitle);
                    req.setDescription(currentRequirement.toString().trim());
                    req.setSourceFile(file.toString());
                    requirements.add(req);
                }

                currentTitle = line;
                currentRequirement.setLength(0);
            } else if (currentTitle != null) {
                // Continuation of current requirement
                currentRequirement.append(line).append(" ");
            }
        }

        // Don't forget the last requirement
        if (currentTitle != null && currentRequirement.length() > 0) {
            Requirement req = new Requirement();
            req.setId(generateIdFromTitle(currentTitle));
            req.setTitle(currentTitle);
            req.setDescription(currentRequirement.toString().trim());
            req.setSourceFile(file.toString());
            requirements.add(req);
        }

        return requirements;
    }

    /**
     * Generate an ID from a title
     */
    private String generateIdFromTitle(String title) {
        return "REQ-" + title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .substring(0, Math.min(20, title.length()));
    }

    /**
     * Initialize parsers for different file formats
     */
    private Map<String, RequirementParser> initializeParsers() {
        Map<String, RequirementParser> parsers = new HashMap<>();
        // In a more complex implementation, you'd register different parsers here
        return parsers;
    }

    /**
     * Interface for requirement parsers
     */
    private interface RequirementParser {

        List<Requirement> parse(String content, Path file) throws IOException;
    }
}
