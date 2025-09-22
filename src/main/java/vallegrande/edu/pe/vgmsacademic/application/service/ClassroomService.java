package vallegrande.edu.pe.vgmsacademic.application.service;

import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClassroomService {
    
    /**
     * Crear una nueva aula
     */
    Mono<ClassroomResponseDto> createClassroom(ClassroomRequestDto classroomRequestDto);
    
    /**
     * Obtener todas las aulas
     */
    Flux<ClassroomResponseDto> getAllClassrooms();
    
    /**
     * Obtener aula por ID
     */
    Mono<ClassroomResponseDto> getClassroomById(String id);
    
    /**
     * Obtener aulas por ID de sede
     */
    Flux<ClassroomResponseDto> getClassroomsByHeadquarter(String headquarterId);
    
    /**
     * Obtener aulas por ID de período
     */
    Flux<ClassroomResponseDto> getClassroomsByPeriod(String periodId);
    
    /**
     * Obtener aulas por sede y período
     */
    Flux<ClassroomResponseDto> getClassroomsByHeadquarterAndPeriod(String headquarterId, String periodId);
    
    /**
     * Obtener aulas por grado
     */
    Flux<ClassroomResponseDto> getClassroomsByGrade(Integer grade);
    
    /**
     * Obtener aulas por turno
     */
    Flux<ClassroomResponseDto> getClassroomsByShift(String shift);
    
    /**
     * Obtener aulas por estado
     */
    Flux<ClassroomResponseDto> getClassroomsByStatus(String status);
    
    /**
     * Obtener aulas por sede, período y grado
     */
    Flux<ClassroomResponseDto> getClassroomsByHeadquarterPeriodAndGrade(String headquarterId, String periodId, Integer grade);
    
    /**
     * Obtener aulas por sede, período y turno
     */
    Flux<ClassroomResponseDto> getClassroomsByHeadquarterPeriodAndShift(String headquarterId, String periodId, String shift);
    
    /**
     * Actualizar un aula
     */
    Mono<ClassroomResponseDto> updateClassroom(String id, ClassroomRequestDto classroomRequestDto);
    
    /**
     * Eliminado lógico - cambiar estado a 'I'
     */
    Mono<Boolean> logicalDelete(String id);
    
    /**
     * Restaurar aula - cambiar estado a 'A'
     */
    Mono<Boolean> restoreClassroom(String id);
    
    /**
     * Verificar si existe un aula con sede, período, grado y sección específicos
     */
    Mono<Boolean> existsByHeadquarterPeriodGradeAndSection(String headquarterId, String periodId, Integer grade, String section);
    
    /**
     * Contar aulas por sede y período
     */
    Mono<Long> countByHeadquarterAndPeriod(String headquarterId, String periodId);
    
    /**
     * Contar aulas por estado
     */
    Mono<Long> countByStatus(String status);
}