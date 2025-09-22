package vallegrande.edu.pe.vgmsacademic.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class TeacherAssignmentRequestDto {
    
    @JsonProperty("teacherId")
    @NotBlank(message = "El ID del profesor es obligatorio")
    private String teacherId;
    
    @JsonProperty("courseId")
    @NotBlank(message = "El ID del curso es obligatorio")
    private String courseId;
    
    @JsonProperty("classroomId")
    @NotBlank(message = "El ID del aula es obligatoria")
    private String classroomId;
    
    @JsonProperty("assignmentDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;
    
    @JsonProperty("status")
    @NotBlank(message = "El estado es obligatorio")
    private String status;
    
    @JsonProperty("academicPeriod")
    private String academicPeriod;
    
    // Constructor vacío
    public TeacherAssignmentRequestDto() {}
    
    // Constructor completo
    public TeacherAssignmentRequestDto(String teacherId, String courseId, String classroomId, 
                                      LocalDate assignmentDate, String status) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate;
        this.status = status;
    }
    
    // Constructor completo con período académico
    public TeacherAssignmentRequestDto(String teacherId, String courseId, String classroomId, 
                                      LocalDate assignmentDate, String status, String academicPeriod) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.assignmentDate = assignmentDate;
        this.status = status;
        this.academicPeriod = academicPeriod;
    }
    
    // Getters y Setters
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAcademicPeriod() {
        return academicPeriod;
    }
    
    public void setAcademicPeriod(String academicPeriod) {
        this.academicPeriod = academicPeriod;
    }
}