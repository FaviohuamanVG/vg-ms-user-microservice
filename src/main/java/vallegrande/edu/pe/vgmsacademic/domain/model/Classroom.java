package vallegrande.edu.pe.vgmsacademic.domain.model;

import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "classrooms")
public class Classroom {
    
    @Id
    private String id;
    
    @Field("headquarterId")
    private String headquarterId;
    
    @Field("periodId")
    private String periodId;
    
    @Field("section")
    private String section;
    
    @Field("classroomName")
    private String classroomName;
    
    @Field("grade")
    private Integer grade;
    
    @Field("shift")
    private String shift;
    
    @Field("status")
    private StatusEnum status;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public Classroom() {
        this.status = StatusEnum.A; // Estado por defecto
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor completo
    public Classroom(String headquarterId, String periodId, String section, 
                     Integer grade, String shift, StatusEnum status) {
        this.headquarterId = headquarterId;
        this.periodId = periodId;
        this.section = section;
        this.grade = grade;
        this.shift = shift;
        this.status = status != null ? status : StatusEnum.A;
        this.classroomName = generateClassroomName(grade, section);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Método para generar el nombre del aula automáticamente
    private String generateClassroomName(Integer grade, String section) {
        if (grade == null || section == null) {
            return "";
        }
        
        String gradeText;
        switch (grade) {
            case 1: gradeText = "1ro"; break;
            case 2: gradeText = "2do"; break;
            case 3: gradeText = "3ro"; break;
            case 4: gradeText = "4to"; break;
            case 5: gradeText = "5to"; break;
            case 6: gradeText = "6to"; break;
            default: gradeText = grade + "°"; break;
        }
        
        return gradeText + " " + section;
    }
    
    // Método para actualizar timestamp
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Método para actualizar nombre del aula cuando cambie grado o sección
    public void updateClassroomName() {
        this.classroomName = generateClassroomName(this.grade, this.section);
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
        updateClassroomName(); // Actualizar nombre cuando cambie la sección
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
        updateClassroomName(); // Actualizar nombre cuando cambie el grado
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        this.shift = shift;
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