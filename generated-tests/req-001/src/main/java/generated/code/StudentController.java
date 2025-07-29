package generated.code;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Simplified REST Controller for Student Management System - Demo Version
 * Provides comprehensive HTTP endpoints for CRUD operations with improved error handling
 * Note: This is a simplified version for demo purposes
 */
public class StudentController {
    
    private final Map<String, Student> students = new HashMap<>();
    
    public StudentController() {
        // Initialize with some demo data
        students.put("STU001", new Student("STU001", "John Doe", "john.doe@email.com", 20));
        students.put("STU002", new Student("STU002", "Jane Smith", "jane.smith@email.com", 22));
    }
    
    /**
     * Create a new student
     * @param student the student data
     * @return the created student with success response
     */
    public ApiResponse<Student> createStudent(Student student) {
        try {
            if (students.containsKey(student.getId())) {
                return new ApiResponse<>(
                    false, 
                    "Student with ID " + student.getId() + " already exists", 
                    null,
                    LocalDateTime.now()
                );
            }
            
            students.put(student.getId(), student);
            return new ApiResponse<>(
                true, 
                "Student created successfully", 
                student,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error creating student: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Get a student by ID
     * @param id the student ID
     * @return the student if found
     */
    public ApiResponse<Student> getStudentById(String id) {
        Student student = students.get(id);
        if (student != null) {
            return new ApiResponse<>(
                true, 
                "Student found", 
                student,
                LocalDateTime.now()
            );
        } else {
            return new ApiResponse<>(
                false, 
                "Student with ID " + id + " not found", 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Get all students with pagination and filtering
     * @param page the page number (default: 1)
     * @param size the page size (default: 10, max: 100)
     * @param sortBy the field to sort by (default: name)
     * @param sortOrder the sort order (asc/desc, default: asc)
     * @return paginated list of students
     */
    public ApiResponse<PaginatedResponse<Student>> getAllStudents(
            int page, int size, String sortBy, String sortOrder) {
        
        // Validate pagination parameters
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        
        try {
            List<Student> allStudents = new ArrayList<>(students.values());
            int totalCount = allStudents.size();
            int totalPages = (int) Math.ceil((double) totalCount / size);
            
            // Apply pagination
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, allStudents.size());
            List<Student> paginatedStudents = allStudents.subList(startIndex, endIndex);
            
            PaginatedResponse<Student> paginatedResponse = new PaginatedResponse<>(
                paginatedStudents, page, size, totalCount, totalPages, sortBy, sortOrder
            );
            
            return new ApiResponse<>(
                true, 
                "Students retrieved successfully", 
                paginatedResponse,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error retrieving students: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Update a student
     * @param id the student ID
     * @param student the updated student data
     * @return the updated student
     */
    public ApiResponse<Student> updateStudent(String id, Student student) {
        try {
            if (!students.containsKey(id)) {
                return new ApiResponse<>(
                    false, 
                    "Student with ID " + id + " not found", 
                    null,
                    LocalDateTime.now()
                );
            }
            
            student.setId(id); // Ensure ID matches path
            students.put(id, student);
            return new ApiResponse<>(
                true, 
                "Student updated successfully", 
                student,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error updating student: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Partially update a student (PATCH operation)
     * @param id the student ID
     * @param updates the fields to update
     * @return the updated student
     */
    public ApiResponse<Student> partialUpdateStudent(String id, Map<String, Object> updates) {
        try {
            Student existingStudent = students.get(id);
            if (existingStudent == null) {
                return new ApiResponse<>(
                    false, 
                    "Student with ID " + id + " not found", 
                    null,
                    LocalDateTime.now()
                );
            }
            
            // Apply updates
            if (updates.containsKey("name")) {
                existingStudent.setName((String) updates.get("name"));
            }
            if (updates.containsKey("email")) {
                existingStudent.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("age")) {
                existingStudent.setAge((Integer) updates.get("age"));
            }
            
            students.put(id, existingStudent);
            return new ApiResponse<>(
                true, 
                "Student partially updated successfully", 
                existingStudent,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error updating student: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Delete a student
     * @param id the student ID
     * @return success status
     */
    public ApiResponse<Void> deleteStudent(String id) {
        Student removed = students.remove(id);
        if (removed != null) {
            return new ApiResponse<>(
                true, 
                "Student deleted successfully", 
                null,
                LocalDateTime.now()
            );
        } else {
            return new ApiResponse<>(
                false, 
                "Student with ID " + id + " not found", 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Bulk delete students
     * @param ids list of student IDs to delete
     * @return bulk operation result
     */
    public ApiResponse<BulkOperationResult> bulkDeleteStudents(List<String> ids) {
        BulkOperationResult result = new BulkOperationResult();
        
        for (String id : ids) {
            Student removed = students.remove(id);
            if (removed != null) {
                result.addSuccess(id);
            } else {
                result.addFailure(id, "Student not found");
            }
        }
        
        return new ApiResponse<>(
            true, 
            "Bulk delete operation completed", 
            result,
            LocalDateTime.now()
        );
    }
    
    /**
     * Search students by name
     * @param name the name to search for
     * @param page the page number (default: 1)
     * @param size the page size (default: 10)
     * @return list of matching students
     */
    public ApiResponse<PaginatedResponse<Student>> searchByName(
            String name, int page, int size) {
        
        try {
            List<Student> matchingStudents = students.values().stream()
                .filter(student -> student.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            
            // Apply pagination manually for search results
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, matchingStudents.size());
            List<Student> paginatedStudents = matchingStudents.subList(startIndex, endIndex);
            
            PaginatedResponse<Student> paginatedResponse = new PaginatedResponse<>(
                paginatedStudents, page, size, matchingStudents.size(), 
                (int) Math.ceil((double) matchingStudents.size() / size), "name", "asc"
            );
            
            return new ApiResponse<>(
                true, 
                "Search completed successfully", 
                paginatedResponse,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error during search: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Search students by email
     * @param email the email to search for
     * @param page the page number (default: 1)
     * @param size the page size (default: 10)
     * @return list of matching students
     */
    public ApiResponse<PaginatedResponse<Student>> searchByEmail(
            String email, int page, int size) {
        
        try {
            List<Student> matchingStudents = students.values().stream()
                .filter(student -> student.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            
            // Apply pagination manually for search results
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, matchingStudents.size());
            List<Student> paginatedStudents = matchingStudents.subList(startIndex, endIndex);
            
            PaginatedResponse<Student> paginatedResponse = new PaginatedResponse<>(
                paginatedStudents, page, size, matchingStudents.size(), 
                (int) Math.ceil((double) matchingStudents.size() / size), "email", "asc"
            );
            
            return new ApiResponse<>(
                true, 
                "Search completed successfully", 
                paginatedResponse,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                false, 
                "Error during search: " + e.getMessage(), 
                null,
                LocalDateTime.now()
            );
        }
    }
    
    /**
     * Get total student count
     * @return the total count
     */
    public ApiResponse<CountResponse> getTotalCount() {
        int count = students.size();
        CountResponse countResponse = new CountResponse(count);
        
        return new ApiResponse<>(
            true, 
            "Count retrieved successfully", 
            countResponse,
            LocalDateTime.now()
        );
    }
    
    /**
     * Get system statistics
     * @return system statistics
     */
    public ApiResponse<SystemStats> getSystemStats() {
        int totalStudents = students.size();
        List<Student> allStudents = new ArrayList<>(students.values());
        
        // Calculate statistics
        double avgAge = allStudents.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        
        long studentsWithValidEmail = allStudents.stream()
                .filter(student -> student.getEmail().contains("@"))
                .count();
        
        SystemStats stats = new SystemStats(totalStudents, avgAge, studentsWithValidEmail);
        
        return new ApiResponse<>(
            true, 
            "Statistics retrieved successfully", 
            stats,
            LocalDateTime.now()
        );
    }
    
    // ==================== STUDENT MODEL ====================
    
    /**
     * Student entity
     */
    public static class Student {
        private String id;
        private String name;
        private String email;
        private int age;
        
        public Student() {}
        
        public Student(String id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
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
        
        @Override
        public String toString() {
            return "Student{id='" + id + "', name='" + name + "', email='" + email + "', age=" + age + "}";
        }
    }
    
    // ==================== RESPONSE MODELS ====================
    
    /**
     * Standard API response wrapper
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;
        
        public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.timestamp = timestamp;
        }
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
    
    /**
     * Paginated response wrapper
     */
    public static class PaginatedResponse<T> {
        private List<T> content;
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private String sortBy;
        private String sortOrder;
        
        public PaginatedResponse(List<T> content, int currentPage, int pageSize, 
                               long totalElements, int totalPages, String sortBy, String sortOrder) {
            this.content = content;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.sortBy = sortBy;
            this.sortOrder = sortOrder;
        }
        
        // Getters and setters
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
        
        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        
        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }
        
        public String getSortOrder() { return sortOrder; }
        public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
    }
    
    /**
     * Count response
     */
    public static class CountResponse {
        private int count;
        
        public CountResponse(int count) {
            this.count = count;
        }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
    
    /**
     * System statistics
     */
    public static class SystemStats {
        private int totalStudents;
        private double averageAge;
        private long studentsWithValidEmail;
        
        public SystemStats(int totalStudents, double averageAge, long studentsWithValidEmail) {
            this.totalStudents = totalStudents;
            this.averageAge = averageAge;
            this.studentsWithValidEmail = studentsWithValidEmail;
        }
        
        // Getters and setters
        public int getTotalStudents() { return totalStudents; }
        public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
        
        public double getAverageAge() { return averageAge; }
        public void setAverageAge(double averageAge) { this.averageAge = averageAge; }
        
        public long getStudentsWithValidEmail() { return studentsWithValidEmail; }
        public void setStudentsWithValidEmail(long studentsWithValidEmail) { this.studentsWithValidEmail = studentsWithValidEmail; }
    }
    
    /**
     * Bulk operation result
     */
    public static class BulkOperationResult {
        private List<String> successfulIds;
        private Map<String, String> failedIds;
        
        public BulkOperationResult() {
            this.successfulIds = new ArrayList<>();
            this.failedIds = new HashMap<>();
        }
        
        public void addSuccess(String id) {
            successfulIds.add(id);
        }
        
        public void addFailure(String id, String reason) {
            failedIds.put(id, reason);
        }
        
        // Getters and setters
        public List<String> getSuccessfulIds() { return successfulIds; }
        public void setSuccessfulIds(List<String> successfulIds) { this.successfulIds = successfulIds; }
        
        public Map<String, String> getFailedIds() { return failedIds; }
        public void setFailedIds(Map<String, String> failedIds) { this.failedIds = failedIds; }
    }
} 