package vallegrande.edu.pe.vgmsacademic.infraestructure.repository;

import vallegrande.edu.pe.vgmsacademic.domain.model.Course;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CourseRepository extends ReactiveMongoRepository<Course, String> {
    
    /**
     * Buscar cursos por ID de institución
     */
    Flux<Course> findByInstitutionId(String institutionId);
    
    /**
     * Buscar cursos por estado
     */
    Flux<Course> findByStatus(StatusEnum status);
    
    /**
     * Buscar curso por código de curso
     */
    Mono<Course> findByCourseCode(String courseCode);
    
    /**
     * Verificar si existe un curso con el código especificado
     */
    Mono<Boolean> existsByCourseCode(String courseCode);
    
    /**
     * Buscar cursos por institución y estado
     */
    Flux<Course> findByInstitutionIdAndStatus(String institutionId, StatusEnum status);
    
    /**
     * Buscar cursos por nivel
     */
    Flux<Course> findByLevel(String level);
    
    /**
     * Buscar cursos por institución y nivel
     */
    Flux<Course> findByInstitutionIdAndLevel(String institutionId, String level);
    
    /**
     * Buscar cursos que contengan el texto en el nombre
     */
    @Query("{'courseName': {'$regex': ?0, '$options': 'i'}}")
    Flux<Course> findByCourseNameContainingIgnoreCase(String courseName);
}