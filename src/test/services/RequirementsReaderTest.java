package demoproject.services;

import demoproject.config.AgentConfig;
import demoproject.models.Requirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequirementsReader Tests")
class RequirementsReaderTest {

    @Mock
    private AgentConfig mockConfig;

    private RequirementsReader requirementsReader;

    @BeforeEach
    void setUp() {
        when(mockConfig.getRequirementsFolder()).thenReturn("requirements");
        requirementsReader = new RequirementsReader(mockConfig);
    }

    @Test
    @DisplayName("Should parse structured markdown requirement correctly")
    void shouldParseStructuredMarkdownRequirementCorrectly() throws IOException {
        // Given
        String markdownContent = """
            # REQ-001: Student Management System CRUD Operations
            
            ## User Story
            As an administrator, I want to manage student records through a comprehensive CRUD system.
            
            ## Description
            Implement a complete Student Management System that provides CRUD operations.
            
            ## Acceptance Criteria
            - Create new student with valid information (positive scenario)
            - Create student with invalid/missing data (negative scenario)
            - Read student by ID (positive scenario)
            
            ## Priority
            High
            
            ## Type
            Feature
            
            ## Dependencies
            - Database connection for student storage
            - Data validation service
            
            ## Module
            StudentManagement
            """;

        Path tempFile = Files.createTempFile("test-requirement", ".md");
        Files.write(tempFile, markdownContent.getBytes());

        try {
            // When
            List<Requirement> requirements = requirementsReader.readRequirements();

            // Then
            assertFalse(requirements.isEmpty());
            Requirement req = requirements.get(0);
            
            assertEquals("REQ-001", req.getId());
            assertEquals("Student Management System CRUD Operations", req.getTitle());
            assertTrue(req.getDescription().contains("Implement a complete Student Management System"));
            assertEquals("High", req.getPriority());
            assertEquals("Feature", req.getType());
            assertEquals("StudentManagement", req.getModule());
            assertEquals(3, req.getAcceptanceCriteria().size());
            assertEquals(2, req.getDependencies().size());
            
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @DisplayName("Should handle empty requirements folder gracefully")
    void shouldHandleEmptyRequirementsFolderGracefully() throws IOException {
        // Given
        when(mockConfig.getRequirementsFolder()).thenReturn("non-existent-folder");

        // When
        List<Requirement> requirements = requirementsReader.readRequirements();

        // Then
        assertTrue(requirements.isEmpty());
    }

    @Test
    @DisplayName("Should handle malformed markdown gracefully")
    void shouldHandleMalformedMarkdownGracefully() throws IOException {
        // Given
        String malformedContent = """
            # Invalid Requirement
            
            This is malformed content without proper sections
            """;

        Path tempFile = Files.createTempFile("malformed", ".md");
        Files.write(tempFile, malformedContent.getBytes());

        try {
            // When
            List<Requirement> requirements = requirementsReader.readRequirements();

            // Then
            // Should not throw exception, may return empty list or partial requirement
            assertNotNull(requirements);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
} 