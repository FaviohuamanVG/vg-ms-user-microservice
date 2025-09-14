package vallegrande.edu.pe.vgmsacademic.application.impl;

import vallegrande.edu.pe.vgmsacademic.application.service.CourseService;
import vallegrande.edu.pe.vgmsacademic.domain.model.Course;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseResponseDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.enums.StatusEnum;
import vallegrande.edu.pe.vgmsacademic.infraestructure.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public Mono<CourseResponseDto> createCourse(CourseRequestDto courseRequestDto) {
        return courseRepository.existsByCourseCode(courseRequestDto.getCourseCode())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Ya existe un curso con el código: " + courseRequestDto.getCourseCode()));
                }
                
                Course course = new Course(
                    courseRequestDto.getInstitutionId(),
                    courseRequestDto.getCourseCode(),
                    courseRequestDto.getCourseName(),
                    courseRequestDto.getLevel(),
                    courseRequestDto.getDescription(),
                    courseRequestDto.getHoursPerWeek(),
                    courseRequestDto.getStatus() != null ? StatusEnum.fromValue(courseRequestDto.getStatus()) : StatusEnum.A
                );
                
                return courseRepository.save(course)
                    .map(this::convertToResponseDto);
            });
    }
    
    @Override
    public Flux<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<CourseResponseDto> getCourseById(String id) {
        return courseRepository.findById(id)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<CourseResponseDto> getCoursesByInstitution(String institutionId) {
        return courseRepository.findByInstitutionId(institutionId)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<CourseResponseDto> getCoursesByInstitutionAndLevel(String institutionId, String level) {
        return courseRepository.findByInstitutionIdAndLevel(institutionId, level)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Flux<CourseResponseDto> getCoursesByStatus(String status) {
        return courseRepository.findByStatus(StatusEnum.fromValue(status))
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<CourseResponseDto> updateCourse(String id, CourseRequestDto courseRequestDto) {
        return courseRepository.findById(id)
            .flatMap(existingCourse -> {
                // Verificar si el código del curso ha cambiado y si ya existe otro curso con ese código
                if (!existingCourse.getCourseCode().equals(courseRequestDto.getCourseCode())) {
                    return courseRepository.existsByCourseCode(courseRequestDto.getCourseCode())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("Ya existe un curso con el código: " + courseRequestDto.getCourseCode()));
                            }
                            return updateCourseFields(existingCourse, courseRequestDto);
                        });
                } else {
                    return updateCourseFields(existingCourse, courseRequestDto);
                }
            });
    }
    
    private Mono<CourseResponseDto> updateCourseFields(Course course, CourseRequestDto courseRequestDto) {
        course.setInstitutionId(courseRequestDto.getInstitutionId());
        course.setCourseCode(courseRequestDto.getCourseCode());
        course.setCourseName(courseRequestDto.getCourseName());
        course.setLevel(courseRequestDto.getLevel());
        course.setDescription(courseRequestDto.getDescription());
        course.setHoursPerWeek(courseRequestDto.getHoursPerWeek());
        if (courseRequestDto.getStatus() != null) {
            course.setStatus(StatusEnum.fromValue(courseRequestDto.getStatus()));
        }
        course.updateTimestamp();
        
        return courseRepository.save(course)
            .map(this::convertToResponseDto);
    }
    
    @Override
    public Mono<Boolean> logicalDelete(String id) {
        return courseRepository.findById(id)
            .flatMap(course -> {
                course.setStatus(StatusEnum.I);
                course.updateTimestamp();
                return courseRepository.save(course)
                    .map(savedCourse -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> restoreCourse(String id) {
        return courseRepository.findById(id)
            .flatMap(course -> {
                course.setStatus(StatusEnum.A);
                course.updateTimestamp();
                return courseRepository.save(course)
                    .map(savedCourse -> true);
            })
            .defaultIfEmpty(false);
    }
    
    @Override
    public Mono<Boolean> existsByCourseCode(String courseCode) {
        return courseRepository.existsByCourseCode(courseCode);
    }
    
    private CourseResponseDto convertToResponseDto(Course course) {
        return new CourseResponseDto(
            course.getId(),
            course.getInstitutionId(),
            course.getCourseCode(),
            course.getCourseName(),
            course.getLevel(),
            course.getDescription(),
            course.getHoursPerWeek(),
            course.getStatus().getValue(),
            course.getCreatedAt(),
            course.getUpdatedAt()
        );
    }
}