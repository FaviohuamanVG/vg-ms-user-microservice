package vallegrande.edu.pe.vgmsacademic.application.service;

import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {
    
    /**
     * Crear un nuevo curso
     */
    Mono<CourseResponseDto> createCourse(CourseRequestDto courseRequestDto);
    
    /**
     * Obtener todos los cursos
     */
    Flux<CourseResponseDto> getAllCourses();
    
    /**
     * Obtener curso por ID
     */
    Mono<CourseResponseDto> getCourseById(String id);
    
    /**
     * Obtener cursos por código de institución
     */
    Flux<CourseResponseDto> getCoursesByInstitution(String institutionId);
    
    /**
     * Obtener cursos por institución y nivel
     */
    Flux<CourseResponseDto> getCoursesByInstitutionAndLevel(String institutionId, String level);
    
    /**
     * Obtener cursos por estado
     */
    Flux<CourseResponseDto> getCoursesByStatus(String status);
    
    /**
     * Actualizar un curso
     */
    Mono<CourseResponseDto> updateCourse(String id, CourseRequestDto courseRequestDto);
    
    /**
     * Eliminado lógico - cambiar estado a 'I'
     */
    Mono<Boolean> logicalDelete(String id);
    
    /**
     * Restaurar curso - cambiar estado a 'A'
     */
    Mono<Boolean> restoreCourse(String id);
    
    /**
     * Verificar si existe un curso con el código especificado
     */
    Mono<Boolean> existsByCourseCode(String courseCode);
}