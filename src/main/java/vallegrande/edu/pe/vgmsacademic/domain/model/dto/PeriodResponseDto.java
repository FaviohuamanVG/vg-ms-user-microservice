package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PeriodResponseDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("institutionId")
    private String institutionId;
    
    @JsonProperty("level")
    private String level;
    
    @JsonProperty("period")
    private String period;
    
    @JsonProperty("academicYear")
    private String academicYear;
    
    @JsonProperty("periodType")
    private String periodType;
    
    @JsonProperty("startDate")
    private LocalDate startDate;
    
    @JsonProperty("endDate")
    private LocalDate endDate;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public PeriodResponseDto() {}
    
    // Constructor completo
    public PeriodResponseDto(String id, String institutionId, String level, String period,
                            String academicYear, String periodType, LocalDate startDate, LocalDate endDate,
                            String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.institutionId = institutionId;
        this.level = level;
        this.period = period;
        this.academicYear = academicYear;
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
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