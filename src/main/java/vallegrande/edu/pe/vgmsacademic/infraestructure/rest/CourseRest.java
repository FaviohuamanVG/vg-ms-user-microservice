package vallegrande.edu.pe.vgmsacademic.infraestructure.rest;

import vallegrande.edu.pe.vgmsacademic.application.service.CourseService;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.CourseResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses")
@Validated
@CrossOrigin(origins = "*")
public class CourseRest {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * Crear un nuevo curso
     * POST /api/v1/courses
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createCourse(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        return courseService.createCourse(courseRequestDto)
            .map(course -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Curso creado exitosamente");
                response.put("data", course);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            })
            .onErrorResume(IllegalArgumentException.class, e -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(response));
            })
            .onErrorResume(Exception.class, e -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Error interno del servidor");
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
            });
    }
    
    /**
     * Obtener todos los cursos
     * GET /api/v1/courses
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getAllCourses() {
        return courseService.getAllCourses()
            .collectList()
            .map(courses -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Cursos obtenidos exitosamente");
                response.put("data", courses);
                response.put("total", courses.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener curso por ID
     * GET /api/v1/courses/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id)
            .map(course -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Curso encontrado");
                response.put("data", course);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Curso no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }));
    }
    
    /**
     * Obtener cursos por institución
     * GET /api/v1/courses/by-institution/{institutionId}
     */
    @GetMapping("/by-institution/{institutionId}")
    public Mono<ResponseEntity<Map<String, Object>>> getCoursesByInstitution(@PathVariable String institutionId) {
        return courseService.getCoursesByInstitution(institutionId)
            .collectList()
            .map(courses -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Cursos obtenidos exitosamente");
                response.put("data", courses);
                response.put("total", courses.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener cursos por institución y nivel
     * GET /api/v1/courses/by-institution/{institutionId}/level/{level}
     */
    @GetMapping("/by-institution/{institutionId}/level/{level}")
    public Mono<ResponseEntity<Map<String, Object>>> getCoursesByInstitutionAndLevel(
            @PathVariable String institutionId, 
            @PathVariable String level) {
        return courseService.getCoursesByInstitutionAndLevel(institutionId, level)
            .collectList()
            .map(courses -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Cursos obtenidos exitosamente");
                response.put("data", courses);
                response.put("total", courses.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener cursos por estado
     * GET /api/v1/courses/status/{status}
     */
    @GetMapping("/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> getCoursesByStatus(@PathVariable String status) {
        return courseService.getCoursesByStatus(status)
            .collectList()
            .map(courses -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Cursos obtenidos exitosamente");
                response.put("data", courses);
                response.put("total", courses.size());
                return ResponseEntity.ok(response);
            })
            .onErrorResume(IllegalArgumentException.class, e -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(response));
            });
    }
    
    /**
     * Actualizar un curso
     * PUT /api/v1/courses/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCourse(@PathVariable String id, 
                                                                 @Valid @RequestBody CourseRequestDto courseRequestDto) {
        return courseService.updateCourse(id, courseRequestDto)
            .map(course -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Curso actualizado exitosamente");
                response.put("data", course);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Curso no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }))
            .onErrorResume(IllegalArgumentException.class, e -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(response));
            });
    }
    
    /**
     * Eliminado lógico - cambiar estado a 'I'
     * PATCH /api/v1/courses/{id}/delete
     */
    @PatchMapping("/{id}/delete")
    public Mono<ResponseEntity<Map<String, Object>>> logicalDelete(@PathVariable String id) {
        return courseService.logicalDelete(id)
            .map(deleted -> {
                Map<String, Object> response = new HashMap<>();
                if (deleted) {
                    response.put("success", true);
                    response.put("message", "Curso eliminado exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Curso no encontrado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Restaurar curso - cambiar estado a 'A'
     * PATCH /api/v1/courses/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<Map<String, Object>>> restoreCourse(@PathVariable String id) {
        return courseService.restoreCourse(id)
            .map(restored -> {
                Map<String, Object> response = new HashMap<>();
                if (restored) {
                    response.put("success", true);
                    response.put("message", "Curso restaurado exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Curso no encontrado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Verificar si existe un curso con el código especificado
     * GET /api/v1/courses/exists/{courseCode}
     */
    @GetMapping("/exists/{courseCode}")
    public Mono<ResponseEntity<Map<String, Object>>> existsByCourseCode(@PathVariable String courseCode) {
        return courseService.existsByCourseCode(courseCode)
            .map(exists -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("exists", exists);
                response.put("message", exists ? "Curso existe" : "Curso no existe");
                return ResponseEntity.ok(response);
            });
    }
}