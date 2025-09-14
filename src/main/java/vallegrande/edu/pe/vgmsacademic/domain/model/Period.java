package vallegrande.edu.pe.vgmsacademic.domain.model;

import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.PeriodTypeEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "periods")
public class Period {
    
    @Id
    private String id;
    
    @Field("institutionId")
    private String institutionId;
    
    @Field("level")
    private String level;
    
    @Field("period")
    private String period;
    
    @Field("academicYear")
    private String academicYear;
    
    @Field("periodType")
    private PeriodTypeEnum periodType;
    
    @Field("startDate")
    private LocalDate startDate;
    
    @Field("endDate")
    private LocalDate endDate;
    
    @Field("status")
    private StatusEnum status;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public Period() {
        this.status = StatusEnum.A; // Estado por defecto
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor completo
    public Period(String institutionId, String level, String period, String academicYear,
                  PeriodTypeEnum periodType, LocalDate startDate, LocalDate endDate, StatusEnum status) {
        this.institutionId = institutionId;
        this.level = level;
        this.period = period;
        this.academicYear = academicYear;
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status != null ? status : StatusEnum.A;
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
    
    public PeriodTypeEnum getPeriodType() {
        return periodType;
    }
    
    public void setPeriodType(PeriodTypeEnum periodType) {
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