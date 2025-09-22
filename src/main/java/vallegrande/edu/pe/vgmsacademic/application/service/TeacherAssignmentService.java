package vallegrande.edu.pe.vgmsacademic.application.service;

import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TeacherAssignmentService {
    
    /**
     * Crear una nueva asignación de profesor
     */
    Mono<TeacherAssignmentResponseDto> createTeacherAssignment(TeacherAssignmentRequestDto teacherAssignmentRequestDto);
    
    /**
     * Obtener todas las asignaciones
     */
    Flux<TeacherAssignmentResponseDto> getAllTeacherAssignments();
    
    /**
     * Obtener asignación por ID
     */
    Mono<TeacherAssignmentResponseDto> getTeacherAssignmentById(String id);
    
    /**
     * Obtener asignaciones por ID de profesor
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacher(String teacherId);
    
    /**
     * Obtener asignaciones por ID de curso
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByCourse(String courseId);
    
    /**
     * Obtener asignaciones por ID de aula
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByClassroom(String classroomId);
    
    /**
     * Obtener asignaciones por estado
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByStatus(String status);
    
    /**
     * Obtener asignaciones activas por profesor
     */
    Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByTeacher(String teacherId);
    
    /**
     * Obtener asignaciones activas por curso
     */
    Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByCourse(String courseId);
    
    /**
     * Obtener asignaciones activas por aula
     */
    Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByClassroom(String classroomId);
    
    /**
     * Obtener asignaciones por profesor y curso
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacherAndCourse(String teacherId, String courseId);
    
    /**
     * Obtener asignaciones por profesor y aula
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacherAndClassroom(String teacherId, String classroomId);
    
    /**
     * Obtener asignaciones por curso y aula
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByCourseAndClassroom(String courseId, String classroomId);
    
    /**
     * Obtener asignaciones por fecha
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByDate(LocalDate assignmentDate);
    
    /**
     * Obtener asignaciones por rango de fechas
     */
    Flux<TeacherAssignmentResponseDto> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Actualizar una asignación
     */
    Mono<TeacherAssignmentResponseDto> updateTeacherAssignment(String id, TeacherAssignmentRequestDto teacherAssignmentRequestDto);
    
    /**
     * Eliminado lógico - cambiar estado a 'I'
     */
    Mono<Boolean> logicalDelete(String id);
    
    /**
     * Restaurar asignación - cambiar estado a 'A'
     */
    Mono<Boolean> restoreTeacherAssignment(String id);
    
    /**
     * Transferir asignación - cambiar estado a 'T'
     */
    Mono<Boolean> transferAssignment(String id);
    
    /**
     * Completar asignación - cambiar estado a 'C'
     */
    Mono<Boolean> completeAssignment(String id);
    
    /**
     * Verificar si existe una asignación específica
     */
    Mono<Boolean> existsByTeacherCourseAndClassroom(String teacherId, String courseId, String classroomId);
    
    /**
     * Contar asignaciones por profesor
     */
    Mono<Long> countByTeacher(String teacherId);
    
    /**
     * Contar asignaciones por curso
     */
    Mono<Long> countByCourse(String courseId);
    
    /**
     * Contar asignaciones por aula
     */
    Mono<Long> countByClassroom(String classroomId);
    
    /**
     * Contar asignaciones por estado
     */
    Mono<Long> countByStatus(String status);
    
    /**
     * Contar asignaciones activas por profesor
     */
    Mono<Long> countActiveByTeacher(String teacherId);
    
    /**
     * Renovar asignación para nuevo período académico
     * Completa la asignación actual y crea una nueva para el próximo período
     */
    Mono<java.util.Map<String, Object>> renewAssignmentForNewPeriod(String id, TeacherAssignmentRequestDto newAssignmentDto);
}