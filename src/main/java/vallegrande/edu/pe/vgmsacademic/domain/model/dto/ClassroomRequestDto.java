package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public class ClassroomRequestDto {
    
    @JsonProperty("headquarterId")
    @NotBlank(message = "El ID de la sede es obligatorio")
    private String headquarterId;
    
    @JsonProperty("periodId")
    @NotBlank(message = "El ID del período es obligatorio")
    private String periodId;
    
    @JsonProperty("section")
    @NotBlank(message = "La sección es obligatoria")
    @Pattern(regexp = "^[A-Z]$", message = "La sección debe ser una letra mayúscula (A, B, C, etc.)")
    private String section;
    
    @JsonProperty("grade")
    @NotNull(message = "El grado es obligatorio")
    @Min(value = 1, message = "El grado debe ser mayor a 0")
    @Max(value = 6, message = "El grado no puede ser mayor a 6")
    private Integer grade;
    
    @JsonProperty("shift")
    @NotBlank(message = "El turno es obligatorio")
    @Pattern(regexp = "^[MT]$", message = "El turno debe ser 'M' (Mañana) o 'T' (Tarde)")
    private String shift;
    
    @JsonProperty("status")
    @NotBlank(message = "El estado es obligatorio")
    private String status;
    
    // Constructor vacío
    public ClassroomRequestDto() {}
    
    // Constructor completo
    public ClassroomRequestDto(String headquarterId, String periodId, String section, 
                              Integer grade, String shift, String status) {
        this.headquarterId = headquarterId;
        this.periodId = periodId;
        this.section = section;
        this.grade = grade;
        this.shift = shift;
        this.status = status;
    }
    
    // Getters y Setters
    public String getHeadquarterId() {
        return headquarterId;
    }
    
    public void setHeadquarterId(String headquarterId) {
        this.headquarterId = headquarterId;
    }
    
    public String getPeriodId() {
        return periodId;
    }
    
    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
    public Integer getGrade() {
        return grade;
    }
    
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        this.shift = shift;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}