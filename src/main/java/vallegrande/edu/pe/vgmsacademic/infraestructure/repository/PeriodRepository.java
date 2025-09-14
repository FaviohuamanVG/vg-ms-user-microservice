package vallegrande.edu.pe.vgmsacademic.infraestructure.repository;

import vallegrande.edu.pe.vgmsacademic.domain.model.Period;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.PeriodTypeEnum;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PeriodRepository extends ReactiveMongoRepository<Period, String> {
    
    /**
     * Buscar períodos por ID de institución
     */
    Flux<Period> findByInstitutionId(String institutionId);
    
    /**
     * Buscar períodos por estado
     */
    Flux<Period> findByStatus(StatusEnum status);
    
    /**
     * Buscar períodos por año académico
     */
    Flux<Period> findByAcademicYear(String academicYear);
    
    /**
     * Buscar períodos por tipo
     */
    Flux<Period> findByPeriodType(PeriodTypeEnum periodType);
    
    /**
     * Buscar períodos por institución y estado
     */
    Flux<Period> findByInstitutionIdAndStatus(String institutionId, StatusEnum status);
    
    /**
     * Buscar períodos por institución y año académico
     */
    Flux<Period> findByInstitutionIdAndAcademicYear(String institutionId, String academicYear);
    
    /**
     * Buscar períodos por nivel
     */
    Flux<Period> findByLevel(String level);
    
    /**
     * Buscar períodos por institución y nivel
     */
    Flux<Period> findByInstitutionIdAndLevel(String institutionId, String level);
    
    /**
     * Verificar si existe un período con los mismos datos
     */
    Mono<Boolean> existsByInstitutionIdAndLevelAndPeriodAndAcademicYear(
            String institutionId, String level, String period, String academicYear);
    
    /**
     * Buscar períodos activos por institución
     */
    @Query("{'institutionId': ?0, 'status': 'A'}")
    Flux<Period> findActiveByInstitution(String institutionId);
}