package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PeriodRequestDto {
    
    @JsonProperty("institutionId")
    @NotBlank(message = "El ID de la institución es obligatorio")
    private String institutionId;
    
    @JsonProperty("level")
    @NotBlank(message = "El nivel es obligatorio")
    private String level;
    
    @JsonProperty("period")
    @NotBlank(message = "El período es obligatorio")
    private String period;
    
    @JsonProperty("academicYear")
    @NotBlank(message = "El año académico es obligatorio")
    private String academicYear;
    
    @JsonProperty("periodType")
    @NotBlank(message = "El tipo de período es obligatorio")
    private String periodType;
    
    @JsonProperty("startDate")
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;
    
    @JsonProperty("endDate")
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;
    
    @JsonProperty("status")
    private String status;
    
    // Constructor vacío
    public PeriodRequestDto() {}
    
    // Constructor completo
    public PeriodRequestDto(String institutionId, String level, String period, String academicYear,
                           String periodType, LocalDate startDate, LocalDate endDate, String status) {
        this.institutionId = institutionId;
        this.level = level;
        this.period = period;
        this.academicYear = academicYear;
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    
    // Getters y Setters
    public String getInstitutionId() {
        return institutionId;
    }
    
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    public String getPeriodType() {
        return periodType;
    }
    
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}