package vallegrande.edu.pe.vgmsacademic.application.impl;

import vallegrande.edu.pe.vgmsacademic.application.service.PeriodService;
import vallegrande.edu.pe.vgmsacademic.domain.model.Period;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodResponseDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.PeriodTypeEnum;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.PeriodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PeriodServiceImpl implements PeriodService {
    
    @Autowired
    private PeriodRepository periodRepository;
    
    @Override
    public Mono<PeriodResponseDto> createPeriod(PeriodRequestDto periodRequestDto) {
        return periodRepository.existsByInstitutionIdAndLevelAndPeriodAndAcademicYear(
                periodRequestDto.getInstitutionId(),
                periodRequestDto.getLevel(),
                periodRequestDto.getPeriod(),
                periodRequestDto.getAcademicYear())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Ya existe un período con estos datos: " + 
                        periodRequestDto.getInstitutionId() + " - " + periodRequestDto.getLevel() + 
                        " - " + periodRequestDto.getPeriod() + " - " + periodRequestDto.getAcademicYear()));
                }
                
                // Validar fechas
                if (periodRequestDto.getEndDate().isBefore(periodRequestDto.getStartDate())) {
                    return Mono.error(new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio"));
                }
                
                Period period = new Period(
                    periodRequestDto.getInstitutionId(),
                    periodRequestDto.getLevel(),
                    periodRequestDto.getPeriod(),
                    periodRequestDto.getAcademicYear(),
                    PeriodTypeEnum.fromValue(periodRequestDto.getPeriodType()),
                    periodRequestDto.getStartDate(),
                    periodRequestDto.getEndDate(),
                    periodRequestDto.getStatus() != null ? StatusEnum.fromValue(periodRequestDto.getStatus()) : StatusEnum.A
                );
                
                return periodRepository.save(period)
                    .map(this::convertToResponseDto);
            });
    }
    
    @Override
    public Flux<PeriodResponseDto> getAllPeriods() {
        return periodRepository.findAll()
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<PeriodResponseDto> getPeriodById(String id) {
        return periodRepository.findById(id)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<PeriodResponseDto> getPeriodsByInstitution(String institutionId) {
        return periodRepository.findByInstitutionId(institutionId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<PeriodResponseDto> getPeriodsByInstitutionAndLevel(String institutionId, String level) {
        return periodRepository.findByInstitutionIdAndLevel(institutionId, level)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<PeriodResponseDto> getPeriodsByAcademicYear(String academicYear) {
        return periodRepository.findByAcademicYear(academicYear)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<PeriodResponseDto> getPeriodsByType(String periodType) {
        return periodRepository.findByPeriodType(PeriodTypeEnum.fromValue(periodType))
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<PeriodResponseDto> getPeriodsByStatus(String status) {
        return periodRepository.findByStatus(StatusEnum.fromValue(status))
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<PeriodResponseDto> updatePeriod(String id, PeriodRequestDto periodRequestDto) {
        return periodRepository.findById(id)
            .flatMap(existingPeriod -> {
                // Verificar si los datos han cambiado y si ya existe otro período con esos datos
                boolean dataChanged = !existingPeriod.getInstitutionId().equals(periodRequestDto.getInstitutionId()) ||
                                    !existingPeriod.getLevel().equals(periodRequestDto.getLevel()) ||
                                    !existingPeriod.getPeriod().equals(periodRequestDto.getPeriod()) ||
                                    !existingPeriod.getAcademicYear().equals(periodRequestDto.getAcademicYear());
                
                if (dataChanged) {
                    return periodRepository.existsByInstitutionIdAndLevelAndPeriodAndAcademicYear(
                            periodRequestDto.getInstitutionId(),
                            periodRequestDto.getLevel(),
                            periodRequestDto.getPeriod(),
                            periodRequestDto.getAcademicYear())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("Ya existe un período con estos datos"));
                            }
                            return updatePeriodFields(existingPeriod, periodRequestDto);
                        });
                } else {
                    return updatePeriodFields(existingPeriod, periodRequestDto);
                }
            });
    }
    
    private Mono<PeriodResponseDto> updatePeriodFields(Period period, PeriodRequestDto periodRequestDto) {
        // Validar fechas
        if (periodRequestDto.getEndDate().isBefore(periodRequestDto.getStartDate())) {
            return Mono.error(new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio"));
        }
        
        period.setInstitutionId(periodRequestDto.getInstitutionId());
        period.setLevel(periodRequestDto.getLevel());
        period.setPeriod(periodRequestDto.getPeriod());
        period.setAcademicYear(periodRequestDto.getAcademicYear());
        period.setPeriodType(PeriodTypeEnum.fromValue(periodRequestDto.getPeriodType()));
        period.setStartDate(periodRequestDto.getStartDate());
        period.setEndDate(periodRequestDto.getEndDate());
        if (periodRequestDto.getStatus() != null) {
            period.setStatus(StatusEnum.fromValue(periodRequestDto.getStatus()));
        }
        period.updateTimestamp();
        
        return periodRepository.save(period)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<Boolean> logicalDelete(String id) {
        return periodRepository.findById(id)
            .flatMap(period -> {
                period.setStatus(StatusEnum.I);
                period.updateTimestamp();
                return periodRepository.save(period)
                    .map(savedPeriod -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> restorePeriod(String id) {
        return periodRepository.findById(id)
            .flatMap(period -> {
                period.setStatus(StatusEnum.A);
                period.updateTimestamp();
                return periodRepository.save(period)
                    .map(savedPeriod -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> existsPeriod(String institutionId, String level, String period, String academicYear) {
        return periodRepository.existsByInstitutionIdAndLevelAndPeriodAndAcademicYear(
                institutionId, level, period, academicYear);
    }
    
    private PeriodResponseDto convertToResponseDto(Period period) {
        return new PeriodResponseDto(
            period.getId(),
            period.getInstitutionId(),
            period.getLevel(),
            period.getPeriod(),
            period.getAcademicYear(),
            period.getPeriodType().getValue(),
            period.getStartDate(),
            period.getEndDate(),
            period.getStatus().getValue(),
            period.getCreatedAt(),
            period.getUpdatedAt()
        );
    }
}