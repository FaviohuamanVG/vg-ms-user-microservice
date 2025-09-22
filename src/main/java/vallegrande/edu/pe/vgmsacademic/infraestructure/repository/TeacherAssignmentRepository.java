package vallegrande.edu.pe.vgmsacademic.infraestructure.repository;

import vallegrande.edu.pe.vgmsacademic.domain.model.TeacherAssignment;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface TeacherAssignmentRepository extends ReactiveMongoRepository<TeacherAssignment, String> {
    
    /**
     * Buscar asignaciones por ID de profesor
     */
    Flux<TeacherAssignment> findByTeacherId(String teacherId);
    
    /**
     * Buscar asignaciones por ID de curso
     */
    Flux<TeacherAssignment> findByCourseId(String courseId);
    
    /**
     * Buscar asignaciones por ID de aula
     */
    Flux<TeacherAssignment> findByClassroomId(String classroomId);
    
    /**
     * Buscar asignaciones por estado
     */
    Flux<TeacherAssignment> findByStatus(StatusEnum status);
    
    /**
     * Buscar asignaciones por fecha de asignación
     */
    Flux<TeacherAssignment> findByAssignmentDate(LocalDate assignmentDate);
    
    /**
     * Buscar asignaciones por profesor y estado
     */
    Flux<TeacherAssignment> findByTeacherIdAndStatus(String teacherId, StatusEnum status);
    
    /**
     * Buscar asignaciones por curso y estado
     */
    Flux<TeacherAssignment> findByCourseIdAndStatus(String courseId, StatusEnum status);
    
    /**
     * Buscar asignaciones por aula y estado
     */
    Flux<TeacherAssignment> findByClassroomIdAndStatus(String classroomId, StatusEnum status);
    
    /**
     * Buscar asignaciones por profesor y curso
     */
    Flux<TeacherAssignment> findByTeacherIdAndCourseId(String teacherId, String courseId);
    
    /**
     * Buscar asignaciones por profesor y aula
     */
    Flux<TeacherAssignment> findByTeacherIdAndClassroomId(String teacherId, String classroomId);
    
    /**
     * Buscar asignaciones por curso y aula
     */
    Flux<TeacherAssignment> findByCourseIdAndClassroomId(String courseId, String classroomId);
    
    /**
     * Buscar asignación específica por profesor, curso y aula
     */
    Mono<TeacherAssignment> findByTeacherIdAndCourseIdAndClassroomId(String teacherId, String courseId, String classroomId);
    
    /**
     * Verificar si existe una asignación específica por profesor, curso y aula
     */
    Mono<Boolean> existsByTeacherIdAndCourseIdAndClassroomId(String teacherId, String courseId, String classroomId);
    
    /**
     * Buscar asignaciones por rango de fechas
     */
    Flux<TeacherAssignment> findByAssignmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Buscar asignaciones activas por profesor
     */
    @Query("{'teacherId': ?0, 'status': 'A'}")
    Flux<TeacherAssignment> findActiveAssignmentsByTeacher(String teacherId);
    
    /**
     * Buscar asignaciones activas por curso
     */
    @Query("{'courseId': ?0, 'status': 'A'}")
    Flux<TeacherAssignment> findActiveAssignmentsByCourse(String courseId);
    
    /**
     * Buscar asignaciones activas por aula
     */
    @Query("{'classroomId': ?0, 'status': 'A'}")
    Flux<TeacherAssignment> findActiveAssignmentsByClassroom(String classroomId);
    
    /**
     * Contar asignaciones por profesor
     */
    Mono<Long> countByTeacherId(String teacherId);
    
    /**
     * Contar asignaciones por curso
     */
    Mono<Long> countByCourseId(String courseId);
    
    /**
     * Contar asignaciones por aula
     */
    Mono<Long> countByClassroomId(String classroomId);
    
    /**
     * Contar asignaciones por estado
     */
    Mono<Long> countByStatus(StatusEnum status);
    
    /**
     * Contar asignaciones activas por profesor
     */
    Mono<Long> countByTeacherIdAndStatus(String teacherId, StatusEnum status);
}