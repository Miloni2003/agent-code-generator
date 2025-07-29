package demoproject.services;

import demoproject.config.AgentConfig;
import demoproject.models.Requirement;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for providing test templates for different frameworks and languages
 */
public class TestTemplateService {

    private final AgentConfig config;
    private final Map<String, String> templates;

    public TestTemplateService(AgentConfig config) {
        this.config = config;
        this.templates = initializeTemplates();
    }

    /**
     * Initialize test templates
     */
    private Map<String, String> initializeTemplates() {
        Map<String, String> templates = new HashMap<>();

        // JUnit 5 Java templates
        templates.put("junit5-java", getJunit5JavaTemplate());
        templates.put("junit4-java", getJunit4JavaTemplate());
        templates.put("testng-java", getTestNGJavaTemplate());
        templates.put("pytest-python", getPytestPythonTemplate());
        templates.put("jest-javascript", getJestJavaScriptTemplate());

        return templates;
    }

    /**
     * Get template for the configured framework and language
     */
    public String getTemplate() {
        String key = config.getTestFramework() + "-" + config.getLanguage();
        return templates.getOrDefault(key, getJunit5JavaTemplate());
    }

    /**
     * Get template with requirement data filled in
     */
    public String getTemplateWithData(Requirement requirement) {
        String template = getTemplate();

        return template
                .replace("{{CLASS_NAME}}", "Test" + requirement.getId().replaceAll("[^a-zA-Z0-9]", ""))
                .replace("{{REQUIREMENT_ID}}", requirement.getId())
                .replace("{{REQUIREMENT_TITLE}}", requirement.getTitle())
                .replace("{{REQUIREMENT_DESCRIPTION}}", requirement.getDescription())
                .replace("{{PACKAGE_NAME}}", "generated.tests");
    }

    /**
     * JUnit 5 Java template
     */
    private String getJunit5JavaTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import org.junit.jupiter.api.Test;
            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Nested;
            import org.junit.jupiter.params.ParameterizedTest;
            import org.junit.jupiter.params.provider.ValueSource;
            import static org.junit.jupiter.api.Assertions.*;
            
            @DisplayName("{{REQUIREMENT_TITLE}}")
            public class {{CLASS_NAME}} {
                
                @BeforeEach
                void setUp() {
                    // Initialize test environment
                }
                
                @Nested
                @DisplayName("Positive Test Cases")
                class PositiveTests {
                    
                    @Test
                    @DisplayName("Should handle normal operation")
                    void shouldHandleNormalOperation() {
                        // Given
                        // TODO: Set up test data
                        
                        // When
                        // TODO: Execute the functionality
                        
                        // Then
                        // TODO: Verify the results
                        assertTrue(true, "Test not implemented yet");
                    }
                    
                    @Test
                    @DisplayName("Should handle valid input")
                    void shouldHandleValidInput() {
                        // TODO: Implement test for valid input
                        assertTrue(true, "Test not implemented yet");
                    }
                }
                
                @Nested
                @DisplayName("Negative Test Cases")
                class NegativeTests {
                    
                    @Test
                    @DisplayName("Should handle invalid input")
                    void shouldHandleInvalidInput() {
                        // TODO: Implement test for invalid input
                        assertTrue(true, "Test not implemented yet");
                    }
                    
                    @Test
                    @DisplayName("Should handle null input")
                    void shouldHandleNullInput() {
                        // TODO: Implement test for null input
                        assertTrue(true, "Test not implemented yet");
                    }
                }
                
                @Nested
                @DisplayName("Edge Cases")
                class EdgeCases {
                    
                    @ParameterizedTest
                    @ValueSource(strings = {"", " ", "\\t", "\\n"})
                    @DisplayName("Should handle edge case inputs")
                    void shouldHandleEdgeCaseInputs(String input) {
                        // TODO: Implement parameterized test for edge cases
                        assertTrue(true, "Test not implemented yet");
                    }
                }
                
                @Nested
                @DisplayName("Integration Tests")
                class IntegrationTests {
                    
                    @Test
                    @DisplayName("Should integrate with other components")
                    void shouldIntegrateWithOtherComponents() {
                        // TODO: Implement integration test
                        assertTrue(true, "Test not implemented yet");
                    }
                }
            }
            """;
    }

    /**
     * JUnit 4 Java template
     */
    private String getJunit4JavaTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import org.junit.Test;
            import org.junit.Before;
            import org.junit.After;
            import static org.junit.Assert.*;
            
            public class {{CLASS_NAME}} {
                
                @Before
                public void setUp() {
                    // Initialize test environment
                }
                
                @After
                public void tearDown() {
                    // Clean up after tests
                }
                
                @Test
                public void testNormalOperation() {
                    // TODO: Implement test for normal operation
                    assertTrue("Test not implemented yet", true);
                }
                
                @Test
                public void testValidInput() {
                    // TODO: Implement test for valid input
                    assertTrue("Test not implemented yet", true);
                }
                
                @Test
                public void testInvalidInput() {
                    // TODO: Implement test for invalid input
                    assertTrue("Test not implemented yet", true);
                }
                
                @Test
                public void testNullInput() {
                    // TODO: Implement test for null input
                    assertTrue("Test not implemented yet", true);
                }
            }
            """;
    }

    /**
     * TestNG Java template
     */
    private String getTestNGJavaTemplate() {
        return """
            package {{PACKAGE_NAME}};
            
            import org.testng.annotations.Test;
            import org.testng.annotations.BeforeMethod;
            import org.testng.annotations.AfterMethod;
            import org.testng.annotations.DataProvider;
            import static org.testng.Assert.*;
            
            public class {{CLASS_NAME}} {
                
                @BeforeMethod
                public void setUp() {
                    // Initialize test environment
                }
                
                @AfterMethod
                public void tearDown() {
                    // Clean up after tests
                }
                
                @Test(description = "Test normal operation")
                public void testNormalOperation() {
                    // TODO: Implement test for normal operation
                    assertTrue(true, "Test not implemented yet");
                }
                
                @Test(description = "Test valid input")
                public void testValidInput() {
                    // TODO: Implement test for valid input
                    assertTrue(true, "Test not implemented yet");
                }
                
                @Test(description = "Test invalid input")
                public void testInvalidInput() {
                    // TODO: Implement test for invalid input
                    assertTrue(true, "Test not implemented yet");
                }
                
                @DataProvider(name = "edgeCaseData")
                public Object[][] edgeCaseData() {
                    return new Object[][] {
                        {""},
                        {" "},
                        {"\\t"},
                        {"\\n"}
                    };
                }
                
                @Test(dataProvider = "edgeCaseData", description = "Test edge cases")
                public void testEdgeCases(String input) {
                    // TODO: Implement parameterized test for edge cases
                    assertTrue(true, "Test not implemented yet");
                }
            }
            """;
    }

    /**
     * PyTest Python template
     */
    private String getPytestPythonTemplate() {
        return """
            import pytest
            
            class Test{{CLASS_NAME}}:
                \"\"\"
                Test suite for {{REQUIREMENT_TITLE}}
                Requirement ID: {{REQUIREMENT_ID}}
                Description: {{REQUIREMENT_DESCRIPTION}}
                \"\"\"
                
                def setup_method(self):
                    \"\"\"Set up test environment\"\"\"
                    pass
                
                def teardown_method(self):
                    \"\"\"Clean up after tests\"\"\"
                    pass
                
                def test_normal_operation(self):
                    \"\"\"Test normal operation\"\"\"
                    # TODO: Implement test for normal operation
                    assert True, "Test not implemented yet"
                
                def test_valid_input(self):
                    \"\"\"Test valid input\"\"\"
                    # TODO: Implement test for valid input
                    assert True, "Test not implemented yet"
                
                def test_invalid_input(self):
                    \"\"\"Test invalid input\"\"\"
                    # TODO: Implement test for invalid input
                    assert True, "Test not implemented yet"
                
                def test_null_input(self):
                    \"\"\"Test null input\"\"\"
                    # TODO: Implement test for null input
                    assert True, "Test not implemented yet"
                
                @pytest.mark.parametrize("input_value", ["", " ", "\\t", "\\n"])
                def test_edge_cases(self, input_value):
                    \"\"\"Test edge cases\"\"\"
                    # TODO: Implement parameterized test for edge cases
                    assert True, "Test not implemented yet"
            """;
    }

    /**
     * Jest JavaScript template
     */
    private String getJestJavaScriptTemplate() {
        return """
            /**
             * Test suite for {{REQUIREMENT_TITLE}}
             * Requirement ID: {{REQUIREMENT_ID}}
             * Description: {{REQUIREMENT_DESCRIPTION}}
             */
            
            describe('{{CLASS_NAME}}', () => {
                beforeEach(() => {
                    // Set up test environment
                });
                
                afterEach(() => {
                    // Clean up after tests
                });
                
                describe('Positive Test Cases', () => {
                    test('should handle normal operation', () => {
                        // TODO: Implement test for normal operation
                        expect(true).toBe(true);
                    });
                    
                    test('should handle valid input', () => {
                        // TODO: Implement test for valid input
                        expect(true).toBe(true);
                    });
                });
                
                describe('Negative Test Cases', () => {
                    test('should handle invalid input', () => {
                        // TODO: Implement test for invalid input
                        expect(true).toBe(true);
                    });
                    
                    test('should handle null input', () => {
                        // TODO: Implement test for null input
                        expect(true).toBe(true);
                    });
                });
                
                describe('Edge Cases', () => {
                    test.each(['', ' ', '\\t', '\\n'])('should handle edge case input: %s', (input) => {
                        // TODO: Implement parameterized test for edge cases
                        expect(true).toBe(true);
                    });
                });
            });
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
    public boolean hasTemplate() {
        String key = config.getTestFramework() + "-" + config.getLanguage();
        return templates.containsKey(key);
    }
}
