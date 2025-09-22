package vallegrande.edu.pe.vgmsacademic.application.impl;

import vallegrande.edu.pe.vgmsacademic.application.service.TeacherAssignmentService;
import vallegrande.edu.pe.vgmsacademic.domain.model.TeacherAssignment;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentResponseDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.TeacherAssignmentRepository;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.CourseRepository;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.ClassroomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {
    
    @Autowired
    private TeacherAssignmentRepository teacherAssignmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Override
    public Mono<TeacherAssignmentResponseDto> createTeacherAssignment(TeacherAssignmentRequestDto teacherAssignmentRequestDto) {
        // Validar que el curso existe y está activo
        return validateActiveCourse(teacherAssignmentRequestDto.getCourseId())
            // Validar que el aula existe y está activa
            .then(validateActiveClassroom(teacherAssignmentRequestDto.getClassroomId()))
            // Verificar que no existe ya una asignación idéntica
            .then(teacherAssignmentRepository.existsByTeacherIdAndCourseIdAndClassroomId(
                teacherAssignmentRequestDto.getTeacherId(),
                teacherAssignmentRequestDto.getCourseId(),
                teacherAssignmentRequestDto.getClassroomId()))
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Ya existe una asignación para este profesor, curso y aula"));
                }
                
                TeacherAssignment teacherAssignment = new TeacherAssignment(
                    teacherAssignmentRequestDto.getTeacherId(),
                    teacherAssignmentRequestDto.getCourseId(),
                    teacherAssignmentRequestDto.getClassroomId(),
                    teacherAssignmentRequestDto.getAssignmentDate(),
                    teacherAssignmentRequestDto.getStatus() != null ? StatusEnum.fromValue(teacherAssignmentRequestDto.getStatus()) : StatusEnum.A,
                    teacherAssignmentRequestDto.getAcademicPeriod()
                );
                
                return teacherAssignmentRepository.save(teacherAssignment)
                    .map(this::convertToResponseDto);
            });
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAllTeacherAssignments() {
        return teacherAssignmentRepository.findAll()
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<TeacherAssignmentResponseDto> getTeacherAssignmentById(String id) {
        return teacherAssignmentRepository.findById(id)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacher(String teacherId) {
        return teacherAssignmentRepository.findByTeacherId(teacherId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByCourse(String courseId) {
        return teacherAssignmentRepository.findByCourseId(courseId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByClassroom(String classroomId) {
        return teacherAssignmentRepository.findByClassroomId(classroomId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByStatus(String status) {
        return teacherAssignmentRepository.findByStatus(StatusEnum.fromValue(status))
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByTeacher(String teacherId) {
        return teacherAssignmentRepository.findActiveAssignmentsByTeacher(teacherId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByCourse(String courseId) {
        return teacherAssignmentRepository.findActiveAssignmentsByCourse(courseId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getActiveAssignmentsByClassroom(String classroomId) {
        return teacherAssignmentRepository.findActiveAssignmentsByClassroom(classroomId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacherAndCourse(String teacherId, String courseId) {
        return teacherAssignmentRepository.findByTeacherIdAndCourseId(teacherId, courseId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByTeacherAndClassroom(String teacherId, String classroomId) {
        return teacherAssignmentRepository.findByTeacherIdAndClassroomId(teacherId, classroomId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByCourseAndClassroom(String courseId, String classroomId) {
        return teacherAssignmentRepository.findByCourseIdAndClassroomId(courseId, classroomId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByDate(LocalDate assignmentDate) {
        return teacherAssignmentRepository.findByAssignmentDate(assignmentDate)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<TeacherAssignmentResponseDto> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return teacherAssignmentRepository.findByAssignmentDateBetween(startDate, endDate)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<TeacherAssignmentResponseDto> updateTeacherAssignment(String id, TeacherAssignmentRequestDto teacherAssignmentRequestDto) {
        // Validar que el curso existe y está activo
        return validateActiveCourse(teacherAssignmentRequestDto.getCourseId())
            // Validar que el aula existe y está activa
            .then(validateActiveClassroom(teacherAssignmentRequestDto.getClassroomId()))
            // Buscar la asignación existente
            .then(teacherAssignmentRepository.findById(id))
            .flatMap(existingAssignment -> {
                // Verificar si los datos únicos han cambiado y si ya existe otra asignación con esos datos
                if (!existingAssignment.getTeacherId().equals(teacherAssignmentRequestDto.getTeacherId()) ||
                    !existingAssignment.getCourseId().equals(teacherAssignmentRequestDto.getCourseId()) ||
                    !existingAssignment.getClassroomId().equals(teacherAssignmentRequestDto.getClassroomId())) {
                    
                    return teacherAssignmentRepository.existsByTeacherIdAndCourseIdAndClassroomId(
                            teacherAssignmentRequestDto.getTeacherId(),
                            teacherAssignmentRequestDto.getCourseId(),
                            teacherAssignmentRequestDto.getClassroomId())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("Ya existe una asignación para este profesor, curso y aula"));
                            }
                            return updateAssignmentFields(existingAssignment, teacherAssignmentRequestDto);
                        });
                } else {
                    return updateAssignmentFields(existingAssignment, teacherAssignmentRequestDto);
                }
            });
    }
    
    private Mono<TeacherAssignmentResponseDto> updateAssignmentFields(TeacherAssignment assignment, TeacherAssignmentRequestDto requestDto) {
        assignment.setTeacherId(requestDto.getTeacherId());
        assignment.setCourseId(requestDto.getCourseId());
        assignment.setClassroomId(requestDto.getClassroomId());
        assignment.setAssignmentDate(requestDto.getAssignmentDate());
        if (requestDto.getStatus() != null) {
            assignment.setStatus(StatusEnum.fromValue(requestDto.getStatus()));
        }
        assignment.updateTimestamp();
        
        return teacherAssignmentRepository.save(assignment)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<Boolean> logicalDelete(String id) {
        return teacherAssignmentRepository.findById(id)
            .flatMap(assignment -> {
                assignment.setStatus(StatusEnum.I);
                assignment.updateTimestamp();
                return teacherAssignmentRepository.save(assignment)
                    .map(savedAssignment -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> restoreTeacherAssignment(String id) {
        return teacherAssignmentRepository.findById(id)
            .flatMap(assignment -> {
                assignment.setStatus(StatusEnum.A);
                assignment.updateTimestamp();
                return teacherAssignmentRepository.save(assignment)
                    .map(savedAssignment -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> transferAssignment(String id) {
        return teacherAssignmentRepository.findById(id)
            .flatMap(assignment -> {
                assignment.setStatus(StatusEnum.T); // Transferido
                assignment.updateTimestamp();
                return teacherAssignmentRepository.save(assignment)
                    .map(savedAssignment -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> completeAssignment(String id) {
        return teacherAssignmentRepository.findById(id)
            .flatMap(assignment -> {
                assignment.setStatus(StatusEnum.C); // Completado
                assignment.updateTimestamp();
                return teacherAssignmentRepository.save(assignment)
                    .map(savedAssignment -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> existsByTeacherCourseAndClassroom(String teacherId, String courseId, String classroomId) {
        return teacherAssignmentRepository.existsByTeacherIdAndCourseIdAndClassroomId(teacherId, courseId, classroomId);
    }
    
    @Override
    public Mono<Long> countByTeacher(String teacherId) {
        return teacherAssignmentRepository.countByTeacherId(teacherId);
    }
    
    @Override
    public Mono<Long> countByCourse(String courseId) {
        return teacherAssignmentRepository.countByCourseId(courseId);
    }
    
    @Override
    public Mono<Long> countByClassroom(String classroomId) {
        return teacherAssignmentRepository.countByClassroomId(classroomId);
    }
    
    @Override
    public Mono<Long> countByStatus(String status) {
        return teacherAssignmentRepository.countByStatus(StatusEnum.fromValue(status));
    }
    
    @Override
    public Mono<Long> countActiveByTeacher(String teacherId) {
        return teacherAssignmentRepository.countByTeacherIdAndStatus(teacherId, StatusEnum.A);
    }
    
    // Método auxiliar para validar que el curso exista y esté activo
    private Mono<Void> validateActiveCourse(String courseId) {
        return courseRepository.findById(courseId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El curso con ID '" + courseId + "' no existe")))
            .flatMap(course -> {
                if (course.getStatus() != StatusEnum.A) {
                    return Mono.error(new IllegalArgumentException("El curso con ID '" + courseId + "' no está activo. Estado actual: " + course.getStatus().getValue()));
                }
                return Mono.empty();
            });
    }
    
    // Método auxiliar para validar que el aula exista y esté activa
    private Mono<Void> validateActiveClassroom(String classroomId) {
        return classroomRepository.findById(classroomId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El aula con ID '" + classroomId + "' no existe")))
            .flatMap(classroom -> {
                if (classroom.getStatus() != StatusEnum.A) {
                    return Mono.error(new IllegalArgumentException("El aula con ID '" + classroomId + "' no está activa. Estado actual: " + classroom.getStatus().getValue()));
                }
                return Mono.empty();
            });
    }
    
    private TeacherAssignmentResponseDto convertToResponseDto(TeacherAssignment assignment) {
        return new TeacherAssignmentResponseDto(
            assignment.getId(),
            assignment.getTeacherId(),
            assignment.getCourseId(),
            assignment.getClassroomId(),
            assignment.getAssignmentDate(),
            assignment.getStatus().getValue(),
            assignment.getAcademicPeriod(),
            assignment.getCreatedAt(),
            assignment.getUpdatedAt()
        );
    }
    
    @Override
    public Mono<java.util.Map<String, Object>> renewAssignmentForNewPeriod(String id, TeacherAssignmentRequestDto newAssignmentDto) {
        return teacherAssignmentRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Asignación no encontrada con ID: " + id)))
            .flatMap(currentAssignment -> {
                // Verificar que la asignación actual no esté ya completada
                if (currentAssignment.getStatus() == StatusEnum.C) {
                    return Mono.error(new IllegalArgumentException("La asignación ya está completada"));
                }
                
                // Completar la asignación actual
                currentAssignment.setStatus(StatusEnum.C);
                currentAssignment.setUpdatedAt(java.time.LocalDateTime.now());
                
                // Guardar la asignación completada
                return teacherAssignmentRepository.save(currentAssignment)
                    .then(validateActiveCourse(newAssignmentDto.getCourseId()))
                    .then(validateActiveClassroom(newAssignmentDto.getClassroomId()))
                    // Crear la nueva asignación
                    .then(Mono.fromCallable(() -> {
                        TeacherAssignment newAssignment = new TeacherAssignment(
                            newAssignmentDto.getTeacherId(),
                            newAssignmentDto.getCourseId(),
                            newAssignmentDto.getClassroomId(),
                            newAssignmentDto.getAssignmentDate(),
                            StatusEnum.A,
                            newAssignmentDto.getAcademicPeriod()
                        );
                        return newAssignment;
                    }))
                    .flatMap(teacherAssignmentRepository::save)
                    .map(savedNewAssignment -> {
                        java.util.Map<String, Object> result = new java.util.HashMap<>();
                        result.put("previousAssignment", convertToResponseDto(currentAssignment));
                        result.put("newAssignment", convertToResponseDto(savedNewAssignment));
                        return result;
                    });
            });
    }
}