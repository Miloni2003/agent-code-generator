package generated.code;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Student Management System Service
 * Implements CRUD operations for student records
 * Generated based on REQ-001 requirements
 */
public class StudentManagementService {
    
    private final Map<String, Student> students = new HashMap<>();
    private final StudentValidator validator = new StudentValidator();
    
    /**
     * Create a new student record
     * @param student the student to create
     * @return the created student
     * @throws ValidationException if student data is invalid
     * @throws DuplicateStudentException if student ID already exists
     */
    public Student createStudent(Student student) {
        // Validate student data
        validator.validateStudent(student);
        
        // Check for duplicate ID
        if (students.containsKey(student.getId())) {
            throw new DuplicateStudentException("Student with ID " + student.getId() + " already exists");
        }
        
        // Set enrollment date if not provided
        if (student.getEnrollmentDate() == null) {
            student.setEnrollmentDate(LocalDate.now());
        }
        
        // Store the student
        students.put(student.getId(), student);
        return student;
    }
    
    /**
     * Get a student by ID
     * @param id the student ID
     * @return Optional containing the student if found
     */
    public Optional<Student> getStudentById(String id) {
        return Optional.ofNullable(students.get(id));
    }
    
    /**
     * Get all students with pagination
     * @param page the page number (1-based)
     * @param size the page size
     * @return list of students for the specified page
     */
    public List<Student> getAllStudents(int page, int size) {
        int offset = (page - 1) * size;
        return students.values().stream()
                .skip(offset)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    /**
     * Update an existing student
     * @param student the updated student data
     * @return the updated student
     * @throws ValidationException if student data is invalid
     * @throws StudentNotFoundException if student doesn't exist
     */
    public Student updateStudent(Student student) {
        // Validate student data
        validator.validateStudent(student);
        
        // Check if student exists
        if (!students.containsKey(student.getId())) {
            throw new StudentNotFoundException("Student with ID " + student.getId() + " not found");
        }
        
        // Update the student
        students.put(student.getId(), student);
        return student;
    }
    
    /**
     * Delete a student by ID
     * @param id the student ID
     * @return true if student was deleted, false if not found
     */
    public boolean deleteStudent(String id) {
        return students.remove(id) != null;
    }
    
    /**
     * Search students by name
     * @param name the name to search for
     * @return list of students matching the name
     */
    public List<Student> searchStudentsByName(String name) {
        return students.values().stream()
                .filter(student -> student.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Search students by email
     * @param email the email to search for
     * @return list of students matching the email
     */
    public List<Student> searchStudentsByEmail(String email) {
        return students.values().stream()
                .filter(student -> student.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Sort students by specified criteria
     * @param criteria the sorting criteria (name, age, enrollmentDate)
     * @return sorted list of students
     */
    public List<Student> sortStudentsBy(String criteria) {
        Comparator<Student> comparator = switch (criteria.toLowerCase()) {
            case "name" -> Comparator.comparing(Student::getName);
            case "age" -> Comparator.comparing(Student::getAge);
            case "enrollmentdate" -> Comparator.comparing(Student::getEnrollmentDate);
            default -> throw new IllegalArgumentException("Invalid sorting criteria: " + criteria);
        };
        
        return students.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Get total number of students
     * @return the total count
     */
    public int getTotalStudentCount() {
        return students.size();
    }
    
    /**
     * Clear all students (for testing purposes)
     */
    public void clearAllStudents() {
        students.clear();
    }
    
    /**
     * Student entity class
     */
    public static class Student {
        private String id;
        private String name;
        private String email;
        private int age;
        private LocalDate enrollmentDate;
        
        // Constructors
        public Student() {}
        
        public Student(String id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
            this.enrollmentDate = LocalDate.now();
        }
        
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
        
        @Override
        public String toString() {
            return "Student{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", age=" + age +
                    ", enrollmentDate=" + enrollmentDate +
                    '}';
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return Objects.equals(id, student.id);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
    
    /**
     * Student validator class
     */
    public static class StudentValidator {
        
        /**
         * Validate a complete student object
         * @param student the student to validate
         * @throws ValidationException if validation fails
         */
        public void validateStudent(Student student) {
            if (student == null) {
                throw new ValidationException("Student cannot be null");
            }
            
            if (student.getId() == null || student.getId().trim().isEmpty()) {
                throw new ValidationException("Student ID is required");
            }
            
            if (student.getName() == null || student.getName().trim().isEmpty()) {
                throw new ValidationException("Student name is required");
            }
            
            if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
                throw new ValidationException("Student email is required");
            }
            
            validateEmail(student.getEmail());
            validateAge(student.getAge());
        }
        
        /**
         * Validate email format
         * @param email the email to validate
         * @throws ValidationException if email is invalid
         */
        public void validateEmail(String email) {
            if (email == null || email.trim().isEmpty()) {
                throw new ValidationException("Email cannot be empty");
            }
            
            // Basic email validation
            if (!email.contains("@") || !email.contains(".")) {
                throw new ValidationException("Invalid email format");
            }
            
            String[] parts = email.split("@");
            if (parts.length != 2) {
                throw new ValidationException("Invalid email format");
            }
            
            if (parts[0].isEmpty() || parts[1].isEmpty()) {
                throw new ValidationException("Invalid email format");
            }
        }
        
        /**
         * Validate age range
         * @param age the age to validate
         * @throws ValidationException if age is invalid
         */
        public void validateAge(int age) {
            if (age < 16 || age > 100) {
                throw new ValidationException("Age must be between 16 and 100");
            }
        }
    }
    
    /**
     * Custom exceptions
     */
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    public static class DuplicateStudentException extends RuntimeException {
        public DuplicateStudentException(String message) {
            super(message);
        }
    }
    
    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }
} 