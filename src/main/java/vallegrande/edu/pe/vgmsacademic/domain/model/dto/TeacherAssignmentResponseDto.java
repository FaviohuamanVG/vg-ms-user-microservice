package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TeacherAssignmentResponseDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("teacherId")
    private String teacherId;
    
    @JsonProperty("courseId")
    private String courseId;
    
    @JsonProperty("classroomId")
    private String classroomId;
    
    @JsonProperty("assignmentDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("statusName")
    private String statusName;
    
    @JsonProperty("academicPeriod")
    private String academicPeriod;
    
    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public TeacherAssignmentResponseDto() {}
    
    // Constructor completo
    public TeacherAssignmentResponseDto(String id, String teacherId, String courseId, String classroomId, 
                                       LocalDate assignmentDate, String status, 
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate;
        this.status = status;
        this.statusName = getStatusDescription(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Constructor completo con período académico
    public TeacherAssignmentResponseDto(String id, String teacherId, String courseId, String classroomId, 
                                       LocalDate assignmentDate, String status, String academicPeriod,
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate;
        this.status = status;
        this.statusName = getStatusDescription(status);
        this.academicPeriod = academicPeriod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Método auxiliar para obtener descripción del estado
    private String getStatusDescription(String status) {
        if (status == null) return "";
        switch (status) {
            case "A": return "Activo";
            case "I": return "Inactivo";
            case "C": return "Completado";
            case "T": return "Transferido";
            default: return status;
        }
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getClassroomId() {
        return classroomId;
    }
    
    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }
    
    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }
    
    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.statusName = getStatusDescription(status);
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public String getAcademicPeriod() {
        return academicPeriod;
    }
    
    public void setAcademicPeriod(String academicPeriod) {
        this.academicPeriod = academicPeriod;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}