package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CourseRequestDto {
    
    @JsonProperty("institutionId")
    @NotBlank(message = "El ID de la institución es obligatorio")
    private String institutionId;
    
    @JsonProperty("courseCode")
    @NotBlank(message = "El código del curso es obligatorio")
    private String courseCode;
    
    @JsonProperty("courseName")
    @NotBlank(message = "El nombre del curso es obligatorio")
    private String courseName;
    
    @JsonProperty("level")
    @NotBlank(message = "El nivel es obligatorio")
    private String level;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("hoursPerWeek")
    @NotNull(message = "Las horas por semana son obligatorias")
    @Positive(message = "Las horas por semana deben ser mayor a 0")
    private Integer hoursPerWeek;
    
    @JsonProperty("status")
    @NotBlank(message = "El estado es obligatorio")
    private String status;
    
    // Constructor vacío
    public CourseRequestDto() {}
    
    // Constructor completo
    public CourseRequestDto(String institutionId, String courseCode, String courseName, 
                           String level, String description, Integer hoursPerWeek, String status) {
        this.institutionId = institutionId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.level = level;
        this.description = description;
        this.hoursPerWeek = hoursPerWeek;
        this.status = status;
    }
    
    // Getters y Setters
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
}