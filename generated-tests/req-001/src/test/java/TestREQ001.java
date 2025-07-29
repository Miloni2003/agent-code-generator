package generated.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Test class for Student Management System CRUD Operations
 * Generated based on REQ-001 requirements
 */
@DisplayName("Student Management System CRUD Operations")
public class TestREQ001 {

    private StudentService studentService;
    private StudentValidator validator;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
        validator = new StudentValidator();
    }

    // ==================== CREATE OPERATIONS ====================

    @Test
    @DisplayName("Should create new student with valid information")
    void shouldCreateNewStudentWithValidInformation() {
        // Given
        Student student = new Student();
        student.setId("STU001");
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");
        student.setAge(20);
        student.setEnrollmentDate(LocalDate.now());

        // When
        Student createdStudent = studentService.createStudent(student);

        // Then
        assertNotNull(createdStudent);
        assertEquals("STU001", createdStudent.getId());
        assertEquals("John Doe", createdStudent.getName());
        assertEquals("john.doe@example.com", createdStudent.getEmail());
        assertEquals(20, createdStudent.getAge());
        assertNotNull(createdStudent.getEnrollmentDate());
    }

    @Test
    @DisplayName("Should throw exception when creating student with invalid email")
    void shouldThrowExceptionWhenCreatingStudentWithInvalidEmail() {
        // Given
        Student student = new Student();
        student.setId("STU002");
        student.setName("Jane Smith");
        student.setEmail("invalid-email");
        student.setAge(19);
        student.setEnrollmentDate(LocalDate.now());

        // When & Then
        assertThrows(ValidationException.class, () -> {
            studentService.createStudent(student);
        });
    }

    @Test
    @DisplayName("Should throw exception when creating student with invalid age")
    void shouldThrowExceptionWhenCreatingStudentWithInvalidAge() {
        // Given
        Student student = new Student();
        student.setId("STU003");
        student.setName("Bob Wilson");
        student.setEmail("bob.wilson@example.com");
        student.setAge(15); // Invalid age (below 16)
        student.setEnrollmentDate(LocalDate.now());

        // When & Then
        assertThrows(ValidationException.class, () -> {
            studentService.createStudent(student);
        });
    }

    @Test
    @DisplayName("Should throw exception when creating student with duplicate ID")
    void shouldThrowExceptionWhenCreatingStudentWithDuplicateId() {
        // Given
        Student student1 = new Student();
        student1.setId("STU004");
        student1.setName("Alice Brown");
        student1.setEmail("alice.brown@example.com");
        student1.setAge(22);
        student1.setEnrollmentDate(LocalDate.now());

        Student student2 = new Student();
        student2.setId("STU004"); // Same ID
        student2.setName("Charlie Davis");
        student2.setEmail("charlie.davis@example.com");
        student2.setAge(21);
        student2.setEnrollmentDate(LocalDate.now());

        // When
        studentService.createStudent(student1);

        // Then
        assertThrows(DuplicateStudentException.class, () -> {
            studentService.createStudent(student2);
        });
    }

    // ==================== READ OPERATIONS ====================

    @Test
    @DisplayName("Should read student by ID successfully")
    void shouldReadStudentByIdSuccessfully() {
        // Given
        Student student = new Student();
        student.setId("STU005");
        student.setName("Emma Wilson");
        student.setEmail("emma.wilson@example.com");
        student.setAge(23);
        student.setEnrollmentDate(LocalDate.now());
        studentService.createStudent(student);

        // When
        Optional<Student> foundStudent = studentService.getStudentById("STU005");

        // Then
        assertTrue(foundStudent.isPresent());
        assertEquals("STU005", foundStudent.get().getId());
        assertEquals("Emma Wilson", foundStudent.get().getName());
    }

    @Test
    @DisplayName("Should return empty when reading student with non-existent ID")
    void shouldReturnEmptyWhenReadingStudentWithNonExistentId() {
        // When
        Optional<Student> foundStudent = studentService.getStudentById("NONEXISTENT");

        // Then
        assertFalse(foundStudent.isPresent());
    }

    @Test
    @DisplayName("Should list all students with pagination")
    void shouldListAllStudentsWithPagination() {
        // Given
        createSampleStudents();

        // When
        List<Student> students = studentService.getAllStudents(1, 5);

        // Then
        assertNotNull(students);
        assertTrue(students.size() <= 5);
    }

    // ==================== UPDATE OPERATIONS ====================

    @Test
    @DisplayName("Should update existing student information successfully")
    void shouldUpdateExistingStudentInformationSuccessfully() {
        // Given
        Student student = new Student();
        student.setId("STU006");
        student.setName("Frank Miller");
        student.setEmail("frank.miller@example.com");
        student.setAge(24);
        student.setEnrollmentDate(LocalDate.now());
        studentService.createStudent(student);

        // When
        Student updatedStudent = new Student();
        updatedStudent.setId("STU006");
        updatedStudent.setName("Frank J. Miller");
        updatedStudent.setEmail("frank.j.miller@example.com");
        updatedStudent.setAge(25);
        updatedStudent.setEnrollmentDate(LocalDate.now());

        Student result = studentService.updateStudent(updatedStudent);

        // Then
        assertNotNull(result);
        assertEquals("STU006", result.getId());
        assertEquals("Frank J. Miller", result.getName());
        assertEquals("frank.j.miller@example.com", result.getEmail());
        assertEquals(25, result.getAge());
    }

    @Test
    @DisplayName("Should throw exception when updating student with invalid data")
    void shouldThrowExceptionWhenUpdatingStudentWithInvalidData() {
        // Given
        Student student = new Student();
        student.setId("STU007");
        student.setName("Grace Lee");
        student.setEmail("grace.lee@example.com");
        student.setAge(26);
        student.setEnrollmentDate(LocalDate.now());
        studentService.createStudent(student);

        // When
        Student invalidStudent = new Student();
        invalidStudent.setId("STU007");
        invalidStudent.setName("Grace Lee");
        invalidStudent.setEmail("invalid-email-format");
        invalidStudent.setAge(26);
        invalidStudent.setEnrollmentDate(LocalDate.now());

        // Then
        assertThrows(ValidationException.class, () -> {
            studentService.updateStudent(invalidStudent);
        });
    }

    // ==================== DELETE OPERATIONS ====================

    @Test
    @DisplayName("Should delete existing student successfully")
    void shouldDeleteExistingStudentSuccessfully() {
        // Given
        Student student = new Student();
        student.setId("STU008");
        student.setName("Henry Taylor");
        student.setEmail("henry.taylor@example.com");
        student.setAge(27);
        student.setEnrollmentDate(LocalDate.now());
        studentService.createStudent(student);

        // When
        boolean deleted = studentService.deleteStudent("STU008");

        // Then
        assertTrue(deleted);
        assertFalse(studentService.getStudentById("STU008").isPresent());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent student")
    void shouldReturnFalseWhenDeletingNonExistentStudent() {
        // When
        boolean deleted = studentService.deleteStudent("NONEXISTENT");

        // Then
        assertFalse(deleted);
    }

    // ==================== SEARCH OPERATIONS ====================

    @Test
    @DisplayName("Should search students by name")
    void shouldSearchStudentsByName() {
        // Given
        createSampleStudents();

        // When
        List<Student> results = studentService.searchStudentsByName("John");

        // Then
        assertNotNull(results);
        assertTrue(results.size() > 0);
        results.forEach(student -> 
            assertTrue(student.getName().contains("John"))
        );
    }

    @Test
    @DisplayName("Should search students by email")
    void shouldSearchStudentsByEmail() {
        // Given
        createSampleStudents();

        // When
        List<Student> results = studentService.searchStudentsByEmail("example.com");

        // Then
        assertNotNull(results);
        assertTrue(results.size() > 0);
        results.forEach(student -> 
            assertTrue(student.getEmail().contains("example.com"))
        );
    }

    // ==================== SORT OPERATIONS ====================

    @Test
    @DisplayName("Should sort students by name")
    void shouldSortStudentsByName() {
        // Given
        createSampleStudents();

        // When
        List<Student> sortedStudents = studentService.sortStudentsBy("name");

        // Then
        assertNotNull(sortedStudents);
        assertTrue(sortedStudents.size() > 1);
        
        // Verify sorting
        for (int i = 0; i < sortedStudents.size() - 1; i++) {
            assertTrue(sortedStudents.get(i).getName()
                .compareTo(sortedStudents.get(i + 1).getName()) <= 0);
        }
    }

    @Test
    @DisplayName("Should sort students by age")
    void shouldSortStudentsByAge() {
        // Given
        createSampleStudents();

        // When
        List<Student> sortedStudents = studentService.sortStudentsBy("age");

        // Then
        assertNotNull(sortedStudents);
        assertTrue(sortedStudents.size() > 1);
        
        // Verify sorting
        for (int i = 0; i < sortedStudents.size() - 1; i++) {
            assertTrue(sortedStudents.get(i).getAge() <= sortedStudents.get(i + 1).getAge());
        }
    }

    @Test
    @DisplayName("Should sort students by enrollment date")
    void shouldSortStudentsByEnrollmentDate() {
        // Given
        createSampleStudents();

        // When
        List<Student> sortedStudents = studentService.sortStudentsBy("enrollmentDate");

        // Then
        assertNotNull(sortedStudents);
        assertTrue(sortedStudents.size() > 1);
        
        // Verify sorting
        for (int i = 0; i < sortedStudents.size() - 1; i++) {
            assertTrue(sortedStudents.get(i).getEnrollmentDate()
                .compareTo(sortedStudents.get(i + 1).getEnrollmentDate()) <= 0);
        }
    }

    // ==================== VALIDATION TESTS ====================

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@domain.co.uk",
        "student123@university.edu"
    })
    @DisplayName("Should validate correct email formats")
    void shouldValidateCorrectEmailFormats(String email) {
        // Given
        Student student = new Student();
        student.setEmail(email);

        // When & Then
        assertDoesNotThrow(() -> validator.validateEmail(student.getEmail()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "@example.com",
        "test@",
        "test.example.com"
    })
    @DisplayName("Should reject invalid email formats")
    void shouldRejectInvalidEmailFormats(String email) {
        // When & Then
        assertThrows(ValidationException.class, () -> {
            validator.validateEmail(email);
        });
    }

    @ParameterizedTest
    @CsvSource({
        "16, true",
        "25, true",
        "100, true",
        "15, false",
        "101, false"
    })
    @DisplayName("Should validate age range correctly")
    void shouldValidateAgeRangeCorrectly(int age, boolean expectedValid) {
        if (expectedValid) {
            assertDoesNotThrow(() -> validator.validateAge(age));
        } else {
            assertThrows(ValidationException.class, () -> {
                validator.validateAge(age);
            });
        }
    }

    // ==================== HELPER METHODS ====================

    private void createSampleStudents() {
        String[] names = {"John Smith", "Jane Doe", "Bob Johnson", "Alice Brown", "Charlie Davis"};
        String[] emails = {"john.smith@example.com", "jane.doe@example.com", "bob.johnson@example.com", 
                          "alice.brown@example.com", "charlie.davis@example.com"};
        int[] ages = {20, 22, 19, 24, 21};

        for (int i = 0; i < names.length; i++) {
            Student student = new Student();
            student.setId("STU" + String.format("%03d", i + 1));
            student.setName(names[i]);
            student.setEmail(emails[i]);
            student.setAge(ages[i]);
            student.setEnrollmentDate(LocalDate.now().minusDays(i));
            studentService.createStudent(student);
        }
    }

    // ==================== MOCK CLASSES FOR TESTING ====================

    // These would be replaced with actual implementations
    static class Student {
        private String id;
        private String name;
        private String email;
        private int age;
        private LocalDate enrollmentDate;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public LocalDate getEnrollmentDate() { return enrollmentDate; }
        public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    }

    static class StudentService {
        public Student createStudent(Student student) {
            // Mock implementation
            return student;
        }
        
        public Optional<Student> getStudentById(String id) {
            // Mock implementation
            return Optional.empty();
        }
        
        public List<Student> getAllStudents(int page, int size) {
            // Mock implementation
            return List.of();
        }
        
        public Student updateStudent(Student student) {
            // Mock implementation
            return student;
        }
        
        public boolean deleteStudent(String id) {
            // Mock implementation
            return false;
        }
        
        public List<Student> searchStudentsByName(String name) {
            // Mock implementation
            return List.of();
        }
        
        public List<Student> searchStudentsByEmail(String email) {
            // Mock implementation
            return List.of();
        }
        
        public List<Student> sortStudentsBy(String criteria) {
            // Mock implementation
            return List.of();
        }
    }

    static class StudentValidator {
        public void validateEmail(String email) {
            if (!email.contains("@") || !email.contains(".")) {
                throw new ValidationException("Invalid email format");
            }
        }
        
        public void validateAge(int age) {
            if (age < 16 || age > 100) {
                throw new ValidationException("Age must be between 16 and 100");
            }
        }
    }

    static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    static class DuplicateStudentException extends RuntimeException {
        public DuplicateStudentException(String message) {
            super(message);
        }
    }
} 