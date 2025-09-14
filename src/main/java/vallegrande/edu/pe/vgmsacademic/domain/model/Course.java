package vallegrande.edu.pe.vgmsacademic.domain.model;

import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "courses")
public class Course {
    
    @Id
    private String id;
    
    @Field("institutionId")
    private String institutionId;
    
    @Field("courseCode")
    private String courseCode;
    
    @Field("courseName")
    private String courseName;
    
    @Field("level")
    private String level;
    
    @Field("description")
    private String description;
    
    @Field("hoursPerWeek")
    private Integer hoursPerWeek;
    
    @Field("status")
    private StatusEnum status;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public Course() {
        this.status = StatusEnum.A; // Estado por defecto
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor completo
    public Course(String institutionId, String courseCode, String courseName, String level,
                  String description, Integer hoursPerWeek, StatusEnum status) {
        this.institutionId = institutionId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.level = level;
        this.description = description;
        this.hoursPerWeek = hoursPerWeek;
        this.status = status;
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
    
    public String getInstitutionId() {
        return institutionId;
    }
    
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }
    
    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }
    
    public StatusEnum getStatus() {
        return status;
    }
    
    public void setStatus(StatusEnum status) {
        this.status = status;
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