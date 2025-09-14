package vallegrande.edu.pe.vgmsacademic.application.service;

import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeriodService {
    
    /**
     * Crear un nuevo período académico
     */
    Mono<PeriodResponseDto> createPeriod(PeriodRequestDto periodRequestDto);
    
    /**
     * Obtener todos los períodos
     */
    Flux<PeriodResponseDto> getAllPeriods();
    
    /**
     * Obtener período por ID
     */
    Mono<PeriodResponseDto> getPeriodById(String id);
    
    /**
     * Obtener períodos por institución
     */
    Flux<PeriodResponseDto> getPeriodsByInstitution(String institutionId);
    
    /**
     * Obtener períodos por institución y nivel
     */
    Flux<PeriodResponseDto> getPeriodsByInstitutionAndLevel(String institutionId, String level);
    
    /**
     * Obtener períodos por año académico
     */
    Flux<PeriodResponseDto> getPeriodsByAcademicYear(String academicYear);
    
    /**
     * Obtener períodos por tipo
     */
    Flux<PeriodResponseDto> getPeriodsByType(String periodType);
    
    /**
     * Obtener períodos por estado
     */
    Flux<PeriodResponseDto> getPeriodsByStatus(String status);
    
    /**
     * Actualizar un período
     */
    Mono<PeriodResponseDto> updatePeriod(String id, PeriodRequestDto periodRequestDto);
    
    /**
     * Eliminado lógico - cambiar estado a 'I'
     */
    Mono<Boolean> logicalDelete(String id);
    
    /**
     * Restaurar período - cambiar estado a 'A'
     */
    Mono<Boolean> restorePeriod(String id);
    
    /**
     * Verificar si existe un período con los mismos datos
     */
    Mono<Boolean> existsPeriod(String institutionId, String level, String period, String academicYear);
}