package vallegrande.edu.pe.vgmsacademic.infraestructure.repository;

import vallegrande.edu.pe.vgmsacademic.domain.model.Classroom;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClassroomRepository extends ReactiveMongoRepository<Classroom, String> {
    
    /**
     * Buscar aulas por ID de sede
     */
    Flux<Classroom> findByHeadquarterId(String headquarterId);
    
    /**
     * Buscar aulas por ID de período
     */
    Flux<Classroom> findByPeriodId(String periodId);
    
    /**
     * Buscar aulas por estado
     */
    Flux<Classroom> findByStatus(StatusEnum status);
    
    /**
     * Buscar aulas por grado
     */
    Flux<Classroom> findByGrade(Integer grade);
    
    /**
     * Buscar aulas por turno
     */
    Flux<Classroom> findByShift(String shift);
    
    /**
     * Buscar aulas por sección
     */
    Flux<Classroom> findBySection(String section);
    
    /**
     * Buscar aulas por sede y período
     */
    Flux<Classroom> findByHeadquarterIdAndPeriodId(String headquarterId, String periodId);
    
    /**
     * Buscar aulas por sede y estado
     */
    Flux<Classroom> findByHeadquarterIdAndStatus(String headquarterId, StatusEnum status);
    
    /**
     * Buscar aulas por período y estado
     */
    Flux<Classroom> findByPeriodIdAndStatus(String periodId, StatusEnum status);
    
    /**
     * Buscar aulas por grado y turno
     */
    Flux<Classroom> findByGradeAndShift(Integer grade, String shift);
    
    /**
     * Buscar aulas por sede, período y grado
     */
    Flux<Classroom> findByHeadquarterIdAndPeriodIdAndGrade(String headquarterId, String periodId, Integer grade);
    
    /**
     * Buscar aulas por sede, período y turno
     */
    Flux<Classroom> findByHeadquarterIdAndPeriodIdAndShift(String headquarterId, String periodId, String shift);
    
    /**
     * Verificar si existe un aula con sede, período, grado y sección específicos
     */
    Mono<Boolean> existsByHeadquarterIdAndPeriodIdAndGradeAndSection(String headquarterId, String periodId, Integer grade, String section);
    
    /**
     * Buscar aula por sede, período, grado y sección
     */
    Mono<Classroom> findByHeadquarterIdAndPeriodIdAndGradeAndSection(String headquarterId, String periodId, Integer grade, String section);
    
    /**
     * Buscar aulas que contengan el texto en el nombre del aula
     */
    @Query("{'classroomName': {'$regex': ?0, '$options': 'i'}}")
    Flux<Classroom> findByClassroomNameContainingIgnoreCase(String classroomName);
    
    /**
     * Contar aulas por sede y período
     */
    Mono<Long> countByHeadquarterIdAndPeriodId(String headquarterId, String periodId);
    
    /**
     * Contar aulas por estado
     */
    Mono<Long> countByStatus(StatusEnum status);
}