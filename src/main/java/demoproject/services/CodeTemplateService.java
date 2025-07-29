package demoproject.services;

import java.util.HashMap;
import java.util.Map;

import demoproject.config.AgentConfig;
import demoproject.models.Requirement;

/**
 * Service for providing production code templates for different languages and
 * patterns
 */
public class CodeTemplateService {

    private final AgentConfig config;
    private final Map<String, String> templates;

    public CodeTemplateService(AgentConfig config) {
        this.config = config;
        this.templates = initializeTemplates();
    }

    /**
     * Initialize code templates
     */
    private Map<String, String> initializeTemplates() {
        Map<String, String> templates = new HashMap<>();

        // Java templates
        templates.put("java-service", getJavaServiceTemplate());
        templates.put("java-util", getJavaUtilTemplate());
        templates.put("java-model", getJavaModelTemplate());

        // Python templates
        templates.put("python-class", getPythonClassTemplate());

        // JavaScript templates
        templates.put("javascript-class", getJavaScriptClassTemplate());

        return templates;
    }

    /**
     * Get template for the configured language and type
     */
    public String getTemplate(String type) {
        String key = config.getLanguage() + "-" + type;
        return templates.getOrDefault(key, getJavaServiceTemplate());
    }

    /**
     * Get template with requirement data filled in
     */
    public String getTemplateWithData(Requirement requirement, String type) {
        String template = getTemplate(type);

        return template
                .replace("{{CLASS_NAME}}", requirement.getId().replaceAll("[^a-zA-Z0-9]", "") + "Service")
                .replace("{{REQUIREMENT_ID}}", requirement.getId())
                .replace("{{REQUIREMENT_TITLE}}", requirement.getTitle())
                .replace("{{REQUIREMENT_DESCRIPTION}}", requirement.getDescription())
                .replace("{{PACKAGE_NAME}}", "generated.code");
    }

    /**
     * Java Service template
     */
    private String getJavaServiceTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import java.util.*;
            import java.time.*;
            import java.util.logging.Logger;
            import java.util.logging.Level;
            
            /**
             * Generated service class for requirement: {{REQUIREMENT_ID}}
             * {{REQUIREMENT_TITLE}}
             * 
             * {{REQUIREMENT_DESCRIPTION}}
             */
            public class {{CLASS_NAME}} {
                
                private static final Logger logger = Logger.getLogger({{CLASS_NAME}}.class.getName());
                
                /**
                 * Process the main functionality for this requirement
                 */
                public boolean process() {
                    try {
                        logger.info("Processing requirement: {{REQUIREMENT_ID}}");
                        // TODO: Implement actual functionality
                        return true;
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error processing requirement: {{REQUIREMENT_ID}}", e);
                        return false;
                    }
                }
                
                /**
                 * Validate input parameters
                 */
                public boolean validate(String input) {
                    if (input == null) {
                        logger.warning("Input is null");
                        return false;
                    }
                    return !input.trim().isEmpty();
                }
                
                /**
                 * Handle edge cases
                 */
                public String handleEdgeCase(String input) {
                    if (input == null) {
                        return null;
                    }
                    return input.trim();
                }
                
                /**
                 * Get requirement information
                 */
                public String getRequirementInfo() {
                    return "{{REQUIREMENT_ID}}: {{REQUIREMENT_TITLE}}";
                }
            }
            """;
    }

    /**
     * Java Utility template
     */
    private String getJavaUtilTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import java.util.*;
            import java.util.stream.Collectors;
            
            /**
             * Generated utility class for requirement: {{REQUIREMENT_ID}}
             */
            public class {{CLASS_NAME}} {
                
                private {{CLASS_NAME}}() {
                    // Utility class - prevent instantiation
                }
                
                /**
                 * Main processing method
                 */
                public static boolean process(String input) {
                    return input != null && !input.trim().isEmpty();
                }
                
                /**
                 * Validate input
                 */
                public static boolean isValid(String input) {
                    return process(input);
                }
                
                /**
                 * Transform input
                 */
                public static String transform(String input) {
                    if (!isValid(input)) {
                        return null;
                    }
                    return input.trim().toLowerCase();
                }
            }
            """;
    }

    /**
     * Java Model template
     */
    private String getJavaModelTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import java.time.LocalDateTime;
            import java.util.Objects;
            
            /**
             * Generated model class for requirement: {{REQUIREMENT_ID}}
             */
            public class {{CLASS_NAME}} {
                
                private String id;
                private String name;
                private String description;
                private LocalDateTime createdAt;
                private LocalDateTime updatedAt;
                
                public {{CLASS_NAME}}() {
                    this.createdAt = LocalDateTime.now();
                    this.updatedAt = LocalDateTime.now();
                }
                
                public {{CLASS_NAME}}(String id, String name) {
                    this();
                    this.id = id;
                    this.name = name;
                }
                
                // Getters and Setters
                public String getId() { return id; }
                public void setId(String id) { this.id = id; }
                
                public String getName() { return name; }
                public void setName(String name) { this.name = name; }
                
                public String getDescription() { return description; }
                public void setDescription(String description) { this.description = description; }
                
                public LocalDateTime getCreatedAt() { return createdAt; }
                public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
                
                public LocalDateTime getUpdatedAt() { return updatedAt; }
                public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
                
                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    {{CLASS_NAME}} that = ({{CLASS_NAME}}) o;
                    return Objects.equals(id, that.id);
                }
                
                @Override
                public int hashCode() {
                    return Objects.hash(id);
                }
                
                @Override
                public String toString() {
                    return "{{CLASS_NAME}}{" +
                            "id='" + id + '\'' +
                            ", name='" + name + '\'' +
                            '}';
                }
            }
            """;
    }

    /**
     * Python Class template
     */
    private String getPythonClassTemplate() {
        return """
            # Generated class for requirement: {{REQUIREMENT_ID}}
            # {{REQUIREMENT_TITLE}}
            # {{REQUIREMENT_DESCRIPTION}}
            
            import logging
            from datetime import datetime
            from typing import Optional, List, Dict, Any
            
            logger = logging.getLogger(__name__)
            
            class {{CLASS_NAME}}:
                \"\"\"
                Generated service class for requirement: {{REQUIREMENT_ID}}
                \"\"\"
                
                def __init__(self):
                    self.created_at = datetime.now()
                    self.updated_at = datetime.now()
                
                def process(self) -> bool:
                    \"\"\"
                    Process the main functionality for this requirement
                    \"\"\"
                    try:
                        logger.info(f"Processing requirement: {{REQUIREMENT_ID}}")
                        # TODO: Implement actual functionality
                        return True
                    except Exception as e:
                        logger.error(f"Error processing requirement: {{REQUIREMENT_ID}}", exc_info=True)
                        return False
                
                def validate(self, input_data: str) -> bool:
                    \"\"\"
                    Validate input parameters
                    \"\"\"
                    if input_data is None:
                        logger.warning("Input is None")
                        return False
                    return bool(input_data.strip())
                
                def handle_edge_case(self, input_data: str) -> Optional[str]:
                    \"\"\"
                    Handle edge cases
                    \"\"\"
                    if input_data is None:
                        return None
                    return input_data.strip()
                
                def get_requirement_info(self) -> str:
                    \"\"\"
                    Get requirement information
                    \"\"\"
                    return "{{REQUIREMENT_ID}}: {{REQUIREMENT_TITLE}}"
            """;
    }

    /**
     * JavaScript Class template
     */
    private String getJavaScriptClassTemplate() {
        return """
            /**
             * Generated class for requirement: {{REQUIREMENT_ID}}
             * {{REQUIREMENT_TITLE}}
             * {{REQUIREMENT_DESCRIPTION}}
             */
            
            class {{CLASS_NAME}} {
                constructor() {
                    this.createdAt = new Date();
                    this.updatedAt = new Date();
                }
                
                /**
                 * Process the main functionality for this requirement
                 */
                process() {
                    try {
                        console.log(`Processing requirement: {{REQUIREMENT_ID}}`);
                        // TODO: Implement actual functionality
                        return true;
                    } catch (error) {
                        console.error(`Error processing requirement: {{REQUIREMENT_ID}}`, error);
                        return false;
                    }
                }
                
                /**
                 * Validate input parameters
                 */
                validate(input) {
                    if (input === null || input === undefined) {
                        console.warn("Input is null or undefined");
                        return false;
                    }
                    return input.trim().length > 0;
                }
                
                /**
                 * Handle edge cases
                 */
                handleEdgeCase(input) {
                    if (input === null || input === undefined) {
                        return null;
                    }
                    return input.trim();
                }
                
                /**
                 * Get requirement information
                 */
                getRequirementInfo() {
                    return "{{REQUIREMENT_ID}}: {{REQUIREMENT_TITLE}}";
                }
            }
            
            module.exports = {{CLASS_NAME}};
            """;
    }

    /**
     * Get available template keys
     */
    public String[] getAvailableTemplates() {
        return templates.keySet().toArray(new String[0]);
    }

    /**
     * Check if template exists for current configuration
     */
    public boolean hasTemplate(String type) {
        String key = config.getLanguage() + "-" + type;
        return templates.containsKey(key);
    }

    /**
     * Java Controller template
     */
    private String getJavaControllerTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import org.springframework.web.bind.annotation.*;
            import org.springframework.http.ResponseEntity;
            
            /**
             * Generated controller for requirement: {{REQUIREMENT_ID}}
             */
            @RestController
            @RequestMapping("/api")
            public class {{CLASS_NAME}} {
                
                @PostMapping("/process")
                public ResponseEntity<String> process(@RequestBody String input) {
                    // TODO: Implement controller logic
                    return ResponseEntity.ok("Processed: " + input);
                }
                
                @GetMapping("/status")
                public ResponseEntity<String> getStatus() {
                    return ResponseEntity.ok("Service is running");
                }
            }
            """;
    }

    /**
     * Java Repository template
     */
    private String getJavaRepositoryTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import org.springframework.data.jpa.repository.JpaRepository;
            import org.springframework.stereotype.Repository;
            
            /**
             * Generated repository for requirement: {{REQUIREMENT_ID}}
             */
            @Repository
            public interface {{CLASS_NAME}} extends JpaRepository<Object, Long> {
                // TODO: Add custom query methods
            }
            """;
    }

    /**
     * Python Function template
     */
    private String getPythonFunctionTemplate() {
        return """
            # Generated function for requirement: {{REQUIREMENT_ID}}
            
            def {{CLASS_NAME.lower()}}_function(input_data):
                \"\"\"
                Generated function for requirement: {{REQUIREMENT_ID}}
                \"\"\"
                # TODO: Implement function logic
                return f"Processed: {input_data}"
            
            def validate_input(input_data):
                \"\"\"
                Validate input data
                \"\"\"
                return input_data is not None and len(str(input_data)) > 0
            """;
    }

    /**
     * JavaScript Function template
     */
    private String getJavaScriptFunctionTemplate() {
        return """
            /**
             * Generated function for requirement: {{REQUIREMENT_ID}}
             */
            
            function {{CLASS_NAME}}Function(inputData) {
                // TODO: Implement function logic
                return `Processed: ${inputData}`;
            }
            
            function validateInput(inputData) {
                return inputData !== null && inputData !== undefined && inputData.toString().length > 0;
            }
            
            module.exports = {
                {{CLASS_NAME}}Function,
                validateInput
            };
            """;
    }
}
