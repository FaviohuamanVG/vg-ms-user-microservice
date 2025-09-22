package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ClassroomResponseDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("headquarterId")
    private String headquarterId;
    
    @JsonProperty("periodId")
    private String periodId;
    
    @JsonProperty("section")
    private String section;
    
    @JsonProperty("classroomName")
    private String classroomName;
    
    @JsonProperty("grade")
    private Integer grade;
    
    @JsonProperty("shift")
    private String shift;
    
    @JsonProperty("shiftName")
    private String shiftName;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("statusName")
    private String statusName;
    
    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public ClassroomResponseDto() {}
    
    // Constructor completo
    public ClassroomResponseDto(String id, String headquarterId, String periodId, String section, 
                               String classroomName, Integer grade, String shift, String status, 
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.headquarterId = headquarterId;
        this.periodId = periodId;
        this.section = section;
        this.classroomName = classroomName;
        this.grade = grade;
        this.shift = shift;
        this.shiftName = getShiftDescription(shift);
        this.status = status;
        this.statusName = getStatusDescription(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Método auxiliar para obtener descripción del turno
    private String getShiftDescription(String shift) {
        if (shift == null) return "";
        switch (shift) {
            case "M": return "Mañana";
            case "T": return "Tarde";
            default: return shift;
        }
    }
    
    // Método auxiliar para obtener descripción del estado
    private String getStatusDescription(String status) {
        if (status == null) return "";
        switch (status) {
            case "A": return "Activo";
            case "I": return "Inactivo";
            case "C": return "Completado";
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
    
    public String getClassroomName() {
        return classroomName;
    }
    
    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
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
        this.shiftName = getShiftDescription(shift);
    }
    
    public String getShiftName() {
        return shiftName;
    }
    
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
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