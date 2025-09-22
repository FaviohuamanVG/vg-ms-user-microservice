package vallegrande.edu.pe.vgmsacademic.domain.model;

import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "teacher_assignments")
public class TeacherAssignment {
    
    @Id
    private String id;
    
    @Field("teacherId")
    private String teacherId;
    
    @Field("courseId")
    private String courseId;
    
    @Field("classroomId")
    private String classroomId;
    
    @Field("assignmentDate")
    private LocalDate assignmentDate;
    
    @Field("status")
    private StatusEnum status;
    
    @Field("academicPeriod")
    private String academicPeriod;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public TeacherAssignment() {
        this.status = StatusEnum.A; // Estado por defecto (ACTIVE)
        this.assignmentDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor completo
    public TeacherAssignment(String teacherId, String courseId, String classroomId, 
                            LocalDate assignmentDate, StatusEnum status) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate != null ? assignmentDate : LocalDate.now();
        this.status = status != null ? status : StatusEnum.A;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor simple con 3 parámetros (usado en algunos métodos)
    public TeacherAssignment(String teacherId, String courseId, String classroomId, LocalDate assignmentDate) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate != null ? assignmentDate : LocalDate.now();
        this.status = StatusEnum.A;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor con período académico
    public TeacherAssignment(String teacherId, String courseId, String classroomId, 
                            LocalDate assignmentDate, StatusEnum status, String academicPeriod) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate != null ? assignmentDate : LocalDate.now();
        this.status = status != null ? status : StatusEnum.A;
        this.academicPeriod = academicPeriod;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Método para actualizar timestamp
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
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
    
    public StatusEnum getStatus() {
        return status;
    }
    
    public void setStatus(StatusEnum status) {
        this.status = status;
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