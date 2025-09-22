package vallegrande.edu.pe.vgmsacademic.infraestructure.rest;

import vallegrande.edu.pe.vgmsacademic.application.service.TeacherAssignmentService;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.TeacherAssignmentResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher-assignments")
@Validated
@CrossOrigin(origins = "*")
public class TeacherAssignmentRest {
    
    @Autowired
    private TeacherAssignmentService teacherAssignmentService;
    
    /**
     * Crear una nueva asignación de profesor
     * POST /api/v1/teacher-assignments
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createTeacherAssignment(@Valid @RequestBody TeacherAssignmentRequestDto teacherAssignmentRequestDto) {
        return teacherAssignmentService.createTeacherAssignment(teacherAssignmentRequestDto)
            .map(assignment -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignación de profesor creada exitosamente");
                response.put("data", assignment);
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
     * Obtener todas las asignaciones
     * GET /api/v1/teacher-assignments
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getAllTeacherAssignments() {
        return teacherAssignmentService.getAllTeacherAssignments()
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignación por ID
     * GET /api/v1/teacher-assignments/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getTeacherAssignmentById(@PathVariable String id) {
        return teacherAssignmentService.getTeacherAssignmentById(id)
            .map(assignment -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignación encontrada");
                response.put("data", assignment);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Asignación no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }));
    }
    
    /**
     * Obtener asignaciones por profesor
     * GET /api/v1/teacher-assignments/by-teacher/{teacherId}
     */
    @GetMapping("/by-teacher/{teacherId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByTeacher(@PathVariable String teacherId) {
        return teacherAssignmentService.getAssignmentsByTeacher(teacherId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones activas por profesor
     * GET /api/v1/teacher-assignments/active/by-teacher/{teacherId}
     */
    @GetMapping("/active/by-teacher/{teacherId}")
    public Mono<ResponseEntity<Map<String, Object>>> getActiveAssignmentsByTeacher(@PathVariable String teacherId) {
        return teacherAssignmentService.getActiveAssignmentsByTeacher(teacherId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones activas obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por curso
     * GET /api/v1/teacher-assignments/by-course/{courseId}
     */
    @GetMapping("/by-course/{courseId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByCourse(@PathVariable String courseId) {
        return teacherAssignmentService.getAssignmentsByCourse(courseId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones activas por curso
     * GET /api/v1/teacher-assignments/active/by-course/{courseId}
     */
    @GetMapping("/active/by-course/{courseId}")
    public Mono<ResponseEntity<Map<String, Object>>> getActiveAssignmentsByCourse(@PathVariable String courseId) {
        return teacherAssignmentService.getActiveAssignmentsByCourse(courseId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones activas obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por aula
     * GET /api/v1/teacher-assignments/by-classroom/{classroomId}
     */
    @GetMapping("/by-classroom/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByClassroom(@PathVariable String classroomId) {
        return teacherAssignmentService.getAssignmentsByClassroom(classroomId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones activas por aula
     * GET /api/v1/teacher-assignments/active/by-classroom/{classroomId}
     */
    @GetMapping("/active/by-classroom/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> getActiveAssignmentsByClassroom(@PathVariable String classroomId) {
        return teacherAssignmentService.getActiveAssignmentsByClassroom(classroomId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones activas obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por estado
     * GET /api/v1/teacher-assignments/status/{status}
     */
    @GetMapping("/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByStatus(@PathVariable String status) {
        return teacherAssignmentService.getAssignmentsByStatus(status)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
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
     * Obtener asignaciones por profesor y curso
     * GET /api/v1/teacher-assignments/by-teacher/{teacherId}/course/{courseId}
     */
    @GetMapping("/by-teacher/{teacherId}/course/{courseId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByTeacherAndCourse(
            @PathVariable String teacherId, 
            @PathVariable String courseId) {
        return teacherAssignmentService.getAssignmentsByTeacherAndCourse(teacherId, courseId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por profesor y aula
     * GET /api/v1/teacher-assignments/by-teacher/{teacherId}/classroom/{classroomId}
     */
    @GetMapping("/by-teacher/{teacherId}/classroom/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByTeacherAndClassroom(
            @PathVariable String teacherId, 
            @PathVariable String classroomId) {
        return teacherAssignmentService.getAssignmentsByTeacherAndClassroom(teacherId, classroomId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por curso y aula
     * GET /api/v1/teacher-assignments/by-course/{courseId}/classroom/{classroomId}
     */
    @GetMapping("/by-course/{courseId}/classroom/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByCourseAndClassroom(
            @PathVariable String courseId, 
            @PathVariable String classroomId) {
        return teacherAssignmentService.getAssignmentsByCourseAndClassroom(courseId, classroomId)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por fecha
     * GET /api/v1/teacher-assignments/by-date/{assignmentDate}
     */
    @GetMapping("/by-date/{assignmentDate}")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate assignmentDate) {
        return teacherAssignmentService.getAssignmentsByDate(assignmentDate)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener asignaciones por rango de fechas
     * GET /api/v1/teacher-assignments/by-date-range?startDate={startDate}&endDate={endDate}
     */
    @GetMapping("/by-date-range")
    public Mono<ResponseEntity<Map<String, Object>>> getAssignmentsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return teacherAssignmentService.getAssignmentsByDateRange(startDate, endDate)
            .collectList()
            .map(assignments -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignaciones obtenidas exitosamente");
                response.put("data", assignments);
                response.put("total", assignments.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Actualizar una asignación
     * PUT /api/v1/teacher-assignments/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateTeacherAssignment(@PathVariable String id, 
                                                                            @Valid @RequestBody TeacherAssignmentRequestDto teacherAssignmentRequestDto) {
        return teacherAssignmentService.updateTeacherAssignment(id, teacherAssignmentRequestDto)
            .map(assignment -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignación actualizada exitosamente");
                response.put("data", assignment);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Asignación no encontrada");
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
     * PATCH /api/v1/teacher-assignments/{id}/delete
     */
    @PatchMapping("/{id}/delete")
    public Mono<ResponseEntity<Map<String, Object>>> logicalDelete(@PathVariable String id) {
        return teacherAssignmentService.logicalDelete(id)
            .map(deleted -> {
                Map<String, Object> response = new HashMap<>();
                if (deleted) {
                    response.put("success", true);
                    response.put("message", "Asignación eliminada exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Asignación no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Restaurar asignación - cambiar estado a 'A'
     * PATCH /api/v1/teacher-assignments/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<Map<String, Object>>> restoreTeacherAssignment(@PathVariable String id) {
        return teacherAssignmentService.restoreTeacherAssignment(id)
            .map(restored -> {
                Map<String, Object> response = new HashMap<>();
                if (restored) {
                    response.put("success", true);
                    response.put("message", "Asignación restaurada exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Asignación no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Transferir asignación - cambiar estado a 'T'
     * PATCH /api/v1/teacher-assignments/{id}/transfer
     */
    @PatchMapping("/{id}/transfer")
    public Mono<ResponseEntity<Map<String, Object>>> transferAssignment(@PathVariable String id) {
        return teacherAssignmentService.transferAssignment(id)
            .map(transferred -> {
                Map<String, Object> response = new HashMap<>();
                if (transferred) {
                    response.put("success", true);
                    response.put("message", "Asignación transferida exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Asignación no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Completar asignación - cambiar estado a 'C'
     * PATCH /api/v1/teacher-assignments/{id}/complete
     */
    @PatchMapping("/{id}/complete")
    public Mono<ResponseEntity<Map<String, Object>>> completeAssignment(@PathVariable String id) {
        return teacherAssignmentService.completeAssignment(id)
            .map(completed -> {
                Map<String, Object> response = new HashMap<>();
                if (completed) {
                    response.put("success", true);
                    response.put("message", "Asignación completada exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Asignación no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Verificar si existe una asignación específica
     * GET /api/v1/teacher-assignments/exists/{teacherId}/{courseId}/{classroomId}
     */
    @GetMapping("/exists/{teacherId}/{courseId}/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> existsByTeacherCourseAndClassroom(
            @PathVariable String teacherId,
            @PathVariable String courseId,
            @PathVariable String classroomId) {
        return teacherAssignmentService.existsByTeacherCourseAndClassroom(teacherId, courseId, classroomId)
            .map(exists -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("exists", exists);
                response.put("message", exists ? "Asignación existe" : "Asignación no existe");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar asignaciones por profesor
     * GET /api/v1/teacher-assignments/count/by-teacher/{teacherId}
     */
    @GetMapping("/count/by-teacher/{teacherId}")
    public Mono<ResponseEntity<Map<String, Object>>> countByTeacher(@PathVariable String teacherId) {
        return teacherAssignmentService.countByTeacher(teacherId)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de asignaciones obtenido exitosamente");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar asignaciones activas por profesor
     * GET /api/v1/teacher-assignments/count/active/by-teacher/{teacherId}
     */
    @GetMapping("/count/active/by-teacher/{teacherId}")
    public Mono<ResponseEntity<Map<String, Object>>> countActiveByTeacher(@PathVariable String teacherId) {
        return teacherAssignmentService.countActiveByTeacher(teacherId)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de asignaciones activas obtenido exitosamente");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar asignaciones por curso
     * GET /api/v1/teacher-assignments/count/by-course/{courseId}
     */
    @GetMapping("/count/by-course/{courseId}")
    public Mono<ResponseEntity<Map<String, Object>>> countByCourse(@PathVariable String courseId) {
        return teacherAssignmentService.countByCourse(courseId)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de asignaciones obtenido exitosamente");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar asignaciones por aula
     * GET /api/v1/teacher-assignments/count/by-classroom/{classroomId}
     */
    @GetMapping("/count/by-classroom/{classroomId}")
    public Mono<ResponseEntity<Map<String, Object>>> countByClassroom(@PathVariable String classroomId) {
        return teacherAssignmentService.countByClassroom(classroomId)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de asignaciones obtenido exitosamente");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar asignaciones por estado
     * GET /api/v1/teacher-assignments/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> countByStatus(@PathVariable String status) {
        return teacherAssignmentService.countByStatus(status)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de asignaciones obtenido exitosamente");
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
     * Renovar asignación para nuevo período académico
     * Completa la asignación actual y crea una nueva para el próximo período
     * POST /api/v1/teacher-assignments/{id}/renew
     */
    @PostMapping("/{id}/renew")
    public Mono<ResponseEntity<Map<String, Object>>> renewAssignmentForNewPeriod(
            @PathVariable String id,
            @Valid @RequestBody TeacherAssignmentRequestDto newAssignmentDto) {
        return teacherAssignmentService.renewAssignmentForNewPeriod(id, newAssignmentDto)
            .map(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Asignación renovada exitosamente para nuevo período académico");
                response.put("previousAssignment", result.get("previousAssignment"));
                response.put("newAssignment", result.get("newAssignment"));
                response.put("academicPeriod", newAssignmentDto.getAcademicPeriod());
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
                response.put("message", "Error interno del servidor al renovar asignación");
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
            });
    }
}