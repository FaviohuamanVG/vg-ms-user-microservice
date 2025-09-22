package vallegrande.edu.pe.vgmsacademic.infraestructure.rest;

import vallegrande.edu.pe.vgmsacademic.application.service.PeriodService;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodRequestDto;
import vallegrande.edu.pe.vgmsacademic.domain.model.dto.PeriodResponseDto;

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
@RequestMapping("/api/v1/periods")
@Validated
@CrossOrigin(origins = "*")
public class PeriodRest {
    
    @Autowired
    private PeriodService periodService;
    
    /**
     * Crear un nuevo período académico
     * POST /api/v1/periods
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createPeriod(@Valid @RequestBody PeriodRequestDto periodRequestDto) {
        return periodService.createPeriod(periodRequestDto)
            .map(period -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Período creado exitosamente");
                response.put("data", period);
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
     * Obtener todos los períodos
     * GET /api/v1/periods
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getAllPeriods() {
        return periodService.getAllPeriods()
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener período por ID
     * GET /api/v1/periods/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodById(@PathVariable String id) {
        return periodService.getPeriodById(id)
            .map(period -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Período encontrado");
                response.put("data", period);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Período no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }));
    }
    
    /**
     * Obtener períodos por institución
     * GET /api/v1/periods/by-institution/{institutionId}
     */
    @GetMapping("/by-institution/{institutionId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodsByInstitution(@PathVariable String institutionId) {
        return periodService.getPeriodsByInstitution(institutionId)
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener períodos por institución y nivel
     * GET /api/v1/periods/by-institution/{institutionId}/level/{level}
     */
    @GetMapping("/by-institution/{institutionId}/level/{level}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodsByInstitutionAndLevel(
            @PathVariable String institutionId, 
            @PathVariable String level) {
        return periodService.getPeriodsByInstitutionAndLevel(institutionId, level)
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener períodos por año académico
     * GET /api/v1/periods/academic-year/{academicYear}
     */
    @GetMapping("/academic-year/{academicYear}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodsByAcademicYear(@PathVariable String academicYear) {
        return periodService.getPeriodsByAcademicYear(academicYear)
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
                return ResponseEntity.ok(response);
            });
    }
    
    /**
     * Obtener períodos por tipo
     * GET /api/v1/periods/type/{periodType}
     */
    @GetMapping("/type/{periodType}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodsByType(@PathVariable String periodType) {
        return periodService.getPeriodsByType(periodType)
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
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
     * Obtener períodos por estado
     * GET /api/v1/periods/status/{status}
     */
    @GetMapping("/status/{status}")
    public Mono<ResponseEntity<Map<String, Object>>> getPeriodsByStatus(@PathVariable String status) {
        return periodService.getPeriodsByStatus(status)
            .collectList()
            .map(periods -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Períodos obtenidos exitosamente");
                response.put("data", periods);
                response.put("total", periods.size());
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
     * Actualizar un período
     * PUT /api/v1/periods/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updatePeriod(@PathVariable String id, 
                                                                 @Valid @RequestBody PeriodRequestDto periodRequestDto) {
        return periodService.updatePeriod(id, periodRequestDto)
            .map(period -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Período actualizado exitosamente");
                response.put("data", period);
                return ResponseEntity.ok(response);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Período no encontrado");
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
     * PATCH /api/v1/periods/{id}/delete
     */
    @PatchMapping("/{id}/delete")
    public Mono<ResponseEntity<Map<String, Object>>> logicalDelete(@PathVariable String id) {
        return periodService.logicalDelete(id)
            .map(deleted -> {
                Map<String, Object> response = new HashMap<>();
                if (deleted) {
                    response.put("success", true);
                    response.put("message", "Período eliminado exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Período no encontrado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Restaurar período - cambiar estado a 'A'
     * PATCH /api/v1/periods/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<Map<String, Object>>> restorePeriod(@PathVariable String id) {
        return periodService.restorePeriod(id)
            .map(restored -> {
                Map<String, Object> response = new HashMap<>();
                if (restored) {
                    response.put("success", true);
                    response.put("message", "Período restaurado exitosamente");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Período no encontrado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            });
    }
    
    /**
     * Verificar si existe un período con los datos especificados
     * GET /api/v1/periods/exists?institutionId={}&level={}&period={}&academicYear={}
     */
    @GetMapping("/exists")
    public Mono<ResponseEntity<Map<String, Object>>> existsPeriod(
            @RequestParam String institutionId,
            @RequestParam String level,
            @RequestParam String period,
            @RequestParam String academicYear) {
        return periodService.existsPeriod(institutionId, level, period, academicYear)
            .map(exists -> {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("exists", exists);
                response.put("message", exists ? "Período existe" : "Período no existe");
                return ResponseEntity.ok(response);
            });
    }
}