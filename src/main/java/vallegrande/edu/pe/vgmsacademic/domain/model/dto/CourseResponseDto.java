package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CourseResponseDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("institutionId")
    private String institutionId;
    
    @JsonProperty("courseCode")
    private String courseCode;
    
    @JsonProperty("courseName")
    private String courseName;
    
    @JsonProperty("level")
    private String level;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("hoursPerWeek")
    private Integer hoursPerWeek;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public CourseResponseDto() {}
    
    // Constructor completo
    public CourseResponseDto(String id, String institutionId, String courseCode, String courseName,
                            String level, String description, Integer hoursPerWeek, String status,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.institutionId = institutionId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.level = level;
        this.description = description;
        this.hoursPerWeek = hoursPerWeek;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
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