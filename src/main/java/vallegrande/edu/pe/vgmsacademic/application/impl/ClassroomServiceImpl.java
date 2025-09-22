package vallegrande.edu.pe.vgmsacademic.application.impl;

import vallegrande.edu.pe.vgmsacademic.application.service.ClassroomService;
import vallegrande.edu.pe.vgmsacademic.domain.model.Classroom;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomResponseDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.ClassroomRepository;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.PeriodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    
    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private PeriodRepository periodRepository;
    
    @Override
    public Mono<ClassroomResponseDto> createClassroom(ClassroomRequestDto classroomRequestDto) {
        // Primero validar que el período existe y esté activo
        return validateActivePeriod(classroomRequestDto.getPeriodId())
            .then(classroomRepository.existsByHeadquarterIdAndPeriodIdAndGradeAndSection(
                    classroomRequestDto.getHeadquarterId(),
                    classroomRequestDto.getPeriodId(),
                    classroomRequestDto.getGrade(),
                    classroomRequestDto.getSection()))
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Ya existe un aula con los mismos datos: Sede, Período, Grado " + classroomRequestDto.getGrade() + " y Sección " + classroomRequestDto.getSection()));
                }
                
                Classroom classroom = new Classroom(
                    classroomRequestDto.getHeadquarterId(),
                    classroomRequestDto.getPeriodId(),
                    classroomRequestDto.getSection(),
                    classroomRequestDto.getGrade(),
                    classroomRequestDto.getShift(),
                    classroomRequestDto.getStatus() != null ? StatusEnum.fromValue(classroomRequestDto.getStatus()) : StatusEnum.A
                );
                
                return classroomRepository.save(classroom)
                    .map(this::convertToResponseDto);
            });
    }
    
    @Override
    public Flux<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<ClassroomResponseDto> getClassroomById(String id) {
        return classroomRepository.findById(id)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByHeadquarter(String headquarterId) {
        return classroomRepository.findByHeadquarterId(headquarterId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByPeriod(String periodId) {
        return classroomRepository.findByPeriodId(periodId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByHeadquarterAndPeriod(String headquarterId, String periodId) {
        return classroomRepository.findByHeadquarterIdAndPeriodId(headquarterId, periodId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByGrade(Integer grade) {
        return classroomRepository.findByGrade(grade)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByShift(String shift) {
        if (!isValidShift(shift)) {
            return Flux.error(new IllegalArgumentException("Turno inválido. Use 'M' para Mañana o 'T' para Tarde"));
        }
        return classroomRepository.findByShift(shift)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByStatus(String status) {
        return classroomRepository.findByStatus(StatusEnum.fromValue(status))
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByHeadquarterPeriodAndGrade(String headquarterId, String periodId, Integer grade) {
        return classroomRepository.findByHeadquarterIdAndPeriodIdAndGrade(headquarterId, periodId, grade)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<ClassroomResponseDto> getClassroomsByHeadquarterPeriodAndShift(String headquarterId, String periodId, String shift) {
        if (!isValidShift(shift)) {
            return Flux.error(new IllegalArgumentException("Turno inválido. Use 'M' para Mañana o 'T' para Tarde"));
        }
        return classroomRepository.findByHeadquarterIdAndPeriodIdAndShift(headquarterId, periodId, shift)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<ClassroomResponseDto> updateClassroom(String id, ClassroomRequestDto classroomRequestDto) {
        // Primero validar que el período existe y esté activo
        return validateActivePeriod(classroomRequestDto.getPeriodId())
            .then(classroomRepository.findById(id))
            .flatMap(existingClassroom -> {
                // Verificar si los datos únicos han cambiado y si ya existe otra aula con esos datos
                if (!existingClassroom.getHeadquarterId().equals(classroomRequestDto.getHeadquarterId()) ||
                    !existingClassroom.getPeriodId().equals(classroomRequestDto.getPeriodId()) ||
                    !existingClassroom.getGrade().equals(classroomRequestDto.getGrade()) ||
                    !existingClassroom.getSection().equals(classroomRequestDto.getSection())) {
                    
                    return classroomRepository.existsByHeadquarterIdAndPeriodIdAndGradeAndSection(
                            classroomRequestDto.getHeadquarterId(),
                            classroomRequestDto.getPeriodId(),
                            classroomRequestDto.getGrade(),
                            classroomRequestDto.getSection())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("Ya existe un aula con los mismos datos: Sede, Período, Grado " + classroomRequestDto.getGrade() + " y Sección " + classroomRequestDto.getSection()));
                            }
                            return updateClassroomFields(existingClassroom, classroomRequestDto);
                        });
                } else {
                    return updateClassroomFields(existingClassroom, classroomRequestDto);
                }
            });
    }
    
    private Mono<ClassroomResponseDto> updateClassroomFields(Classroom classroom, ClassroomRequestDto classroomRequestDto) {
        if (!isValidShift(classroomRequestDto.getShift())) {
            return Mono.error(new IllegalArgumentException("Turno inválido. Use 'M' para Mañana o 'T' para Tarde"));
        }
        
        classroom.setHeadquarterId(classroomRequestDto.getHeadquarterId());
        classroom.setPeriodId(classroomRequestDto.getPeriodId());
        classroom.setSection(classroomRequestDto.getSection());
        classroom.setGrade(classroomRequestDto.getGrade());
        classroom.setShift(classroomRequestDto.getShift());
        if (classroomRequestDto.getStatus() != null) {
            classroom.setStatus(StatusEnum.fromValue(classroomRequestDto.getStatus()));
        }
        classroom.updateTimestamp();
        classroom.updateClassroomName(); // Actualizar el nombre auto-generado
        
        return classroomRepository.save(classroom)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<Boolean> logicalDelete(String id) {
        return classroomRepository.findById(id)
            .flatMap(classroom -> {
                classroom.setStatus(StatusEnum.I);
                classroom.updateTimestamp();
                return classroomRepository.save(classroom)
                    .map(savedClassroom -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> restoreClassroom(String id) {
        return classroomRepository.findById(id)
            .flatMap(classroom -> {
                classroom.setStatus(StatusEnum.A);
                classroom.updateTimestamp();
                return classroomRepository.save(classroom)
                    .map(savedClassroom -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> existsByHeadquarterPeriodGradeAndSection(String headquarterId, String periodId, Integer grade, String section) {
        return classroomRepository.existsByHeadquarterIdAndPeriodIdAndGradeAndSection(headquarterId, periodId, grade, section);
    }
    
    @Override
    public Mono<Long> countByHeadquarterAndPeriod(String headquarterId, String periodId) {
        return classroomRepository.countByHeadquarterIdAndPeriodId(headquarterId, periodId);
    }
    
    @Override
    public Mono<Long> countByStatus(String status) {
        return classroomRepository.countByStatus(StatusEnum.fromValue(status));
    }
    
    // Método auxiliar para validar turno
    private boolean isValidShift(String shift) {
        return shift != null && (shift.equals("M") || shift.equals("T"));
    }
    
    // Método auxiliar para validar que el período exista y esté activo
    private Mono<Void> validateActivePeriod(String periodId) {
        return periodRepository.findById(periodId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El período con ID '" + periodId + "' no existe")))
            .flatMap(period -> {
                if (period.getStatus() != StatusEnum.A) {
                    return Mono.error(new IllegalArgumentException("El período con ID '" + periodId + "' no está activo. Estado actual: " + period.getStatus().getValue()));
                }
                return Mono.empty();
            });
    }
    
    private ClassroomResponseDto convertToResponseDto(Classroom classroom) {
        return new ClassroomResponseDto(
            classroom.getId(),
            classroom.getHeadquarterId(),
            classroom.getPeriodId(),
            classroom.getSection(),
            classroom.getClassroomName(),
            classroom.getGrade(),
            classroom.getShift(),
            classroom.getStatus().getValue(),
            classroom.getCreatedAt(),
            classroom.getUpdatedAt()
        );
    }
}