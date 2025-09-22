package vallegrande.edu.pe.vgmsacademic.infraestructure.rest;

import vallegrande.edu.pe.vgmsacademic.application.service.ClassroomService;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.ClassroomResponseDto;

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
@RequestMapping("/api/v1/classrooms")
@Validated
@CrossOrigin(origins = "*")
public class ClassroomRest {
    
    @Autowired
    private ClassroomService classroomService;
    
    /**
     * Crear una nueva aula
     * POST /api/v1/classrooms
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createClassroom(@Valid @RequestBody ClassroomRequestDto classroomRequestDto) {
        return classroomService.createClassroom(classroomRequestDto)
            .map(classroom -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aula creada exitosamente");
                response.put("data", classroom);
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
     * Obtener todas las aulas
     * GET /api/v1/classrooms
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getAllClassrooms() {
        return classroomService.getAllClassrooms()
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aula por ID
     * GET /api/v1/classrooms/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomById(@PathVariable String id) {
        return classroomService.getClassroomById(id)
            .map(classroom -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aula encontrada");
                response.put("data", classroom);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Aula no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }));
    }
    
    /**
     * Obtener aulas por sede
     * GET /api/v1/classrooms/by-headquarter/{headquarterId}
     */
    @GetMapping("/by-headquarter/{headquarterId}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByHeadquarter(@PathVariable String headquarterId) {
        return classroomService.getClassroomsByHeadquarter(headquarterId)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aulas por período
     * GET /api/v1/classrooms/by-period/{periodId}
     */
    @GetMapping("/by-period/{periodId}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByPeriod(@PathVariable String periodId) {
        return classroomService.getClassroomsByPeriod(periodId)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aulas por sede y período
     * GET /api/v1/classrooms/by-headquarter/{headquarterId}/period/{periodId}
     */
    @GetMapping("/by-headquarter/{headquarterId}/period/{periodId}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByHeadquarterAndPeriod(
            @PathVariable String headquarterId, 
            @PathVariable String periodId) {
        return classroomService.getClassroomsByHeadquarterAndPeriod(headquarterId, periodId)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aulas por grado
     * GET /api/v1/classrooms/by-grade/{grade}
     */
    @GetMapping("/by-grade/{grade}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByGrade(@PathVariable Integer grade) {
        return classroomService.getClassroomsByGrade(grade)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aulas por turno
     * GET /api/v1/classrooms/by-shift/{shift}
     */
    @GetMapping("/by-shift/{shift}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByShift(@PathVariable String shift) {
        return classroomService.getClassroomsByShift(shift)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
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
     * Obtener aulas por estado
     * GET /api/v1/classrooms/status/{status}
     */
    @GetMapping("/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByStatus(@PathVariable String status) {
        return classroomService.getClassroomsByStatus(status)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
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
     * Obtener aulas por sede, período y grado
     * GET /api/v1/classrooms/by-headquarter/{headquarterId}/period/{periodId}/grade/{grade}
     */
    @GetMapping("/by-headquarter/{headquarterId}/period/{periodId}/grade/{grade}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByHeadquarterPeriodAndGrade(
            @PathVariable String headquarterId, 
            @PathVariable String periodId,
            @PathVariable Integer grade) {
        return classroomService.getClassroomsByHeadquarterPeriodAndGrade(headquarterId, periodId, grade)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener aulas por sede, período y turno
     * GET /api/v1/classrooms/by-headquarter/{headquarterId}/period/{periodId}/shift/{shift}
     */
    @GetMapping("/by-headquarter/{headquarterId}/period/{periodId}/shift/{shift}")
    public Mono<ResponseEntity<Map<String, Object>>> getClassroomsByHeadquarterPeriodAndShift(
            @PathVariable String headquarterId, 
            @PathVariable String periodId,
            @PathVariable String shift) {
        return classroomService.getClassroomsByHeadquarterPeriodAndShift(headquarterId, periodId, shift)
            .collectList()
            .map(classrooms -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aulas obtenidas exitosamente");
                response.put("data", classrooms);
                response.put("total", classrooms.size());
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
     * Actualizar un aula
     * PUT /api/v1/classrooms/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateClassroom(@PathVariable String id, 
                                                                    @Valid @RequestBody ClassroomRequestDto classroomRequestDto) {
        return classroomService.updateClassroom(id, classroomRequestDto)
            .map(classroom -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aula actualizada exitosamente");
                response.put("data", classroom);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Aula no encontrada");
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
     * PATCH /api/v1/classrooms/{id}/delete
     */
    @PatchMapping("/{id}/delete")
    public Mono<ResponseEntity<Map<String, Object>>> logicalDelete(@PathVariable String id) {
        return classroomService.logicalDelete(id)
            .map(deleted -> {
                Map<String, Object> response = new HashMap<>();
                if (deleted) {
                    response.put("success", true);
                    response.put("message", "Aula eliminada exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Aula no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Restaurar aula - cambiar estado a 'A'
     * PATCH /api/v1/classrooms/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<Map<String, Object>>> restoreClassroom(@PathVariable String id) {
        return classroomService.restoreClassroom(id)
            .map(restored -> {
                Map<String, Object> response = new HashMap<>();
                if (restored) {
                    response.put("success", true);
                    response.put("message", "Aula restaurada exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Aula no encontrada");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Verificar si existe un aula con sede, período, grado y sección específicos
     * GET /api/v1/classrooms/exists/{headquarterId}/{periodId}/{grade}/{section}
     */
    @GetMapping("/exists/{headquarterId}/{periodId}/{grade}/{section}")
    public Mono<ResponseEntity<Map<String, Object>>> existsByHeadquarterPeriodGradeAndSection(
            @PathVariable String headquarterId,
            @PathVariable String periodId,
            @PathVariable Integer grade,
            @PathVariable String section) {
        return classroomService.existsByHeadquarterPeriodGradeAndSection(headquarterId, periodId, grade, section)
            .map(exists -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("exists", exists);
                response.put("message", exists ? "Aula existe" : "Aula no existe");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar aulas por sede y período
     * GET /api/v1/classrooms/count/by-headquarter/{headquarterId}/period/{periodId}
     */
    @GetMapping("/count/by-headquarter/{headquarterId}/period/{periodId}")
    public Mono<ResponseEntity<Map<String, Object>>> countByHeadquarterAndPeriod(
            @PathVariable String headquarterId,
            @PathVariable String periodId) {
        return classroomService.countByHeadquarterAndPeriod(headquarterId, periodId)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de aulas obtenido exitosamente");
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Contar aulas por estado
     * GET /api/v1/classrooms/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> countByStatus(@PathVariable String status) {
        return classroomService.countByStatus(status)
            .map(count -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("count", count);
                response.put("message", "Conteo de aulas obtenido exitosamente");
                return ResponseEntity.ok(response);
            })
            .onErrorResume(IllegalArgumentException.class, e -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(response));
            });
    }
}