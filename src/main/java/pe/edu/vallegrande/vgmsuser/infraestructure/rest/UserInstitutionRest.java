package pe.edu.vallegrande.vgmsuser.infraestructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsuser.application.service.IUserInstitutionService;
import pe.edu.vallegrande.vgmsuser.domain.model.UserInstitutionRelation;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.AssignInstitutionRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.UpdateRoleRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api/v1/user-institution")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserInstitutionRest {

    private final IUserInstitutionService userInstitutionService;

    @GetMapping
    public Flux<UserInstitutionRelation> getAllUserInstitutionRelations() {
        log.info("Getting all user-institution relations");
        
        return userInstitutionService.getAllUserInstitutionRelations()
                .doOnError(error -> log.error("Error getting all user-institution relations: {}", error.getMessage()));
    }

    @PostMapping("/users/{userId}/assign-institution")
    public Mono<ResponseEntity<UserInstitutionRelation>> assignUserToInstitution(
            @PathVariable String userId,
            @Valid @RequestBody AssignInstitutionRequest request) {
        log.info("Solicitando asignación del usuario con ID: {} a la institución: {} con rol: {}", 
                userId, request.getInstitutionId(), request.getRole());
        
        return userInstitutionService.assignUserToInstitution(userId, request)
                .map(relation -> {
                    try {
                        log.info("Asignación exitosa creada para usuario ID: {}", userId);
                        return ResponseEntity.created(new URI("/api/v1/user-institution/users/" + userId + "/institutions"))
                                .body(relation);
                    } catch (URISyntaxException e) {
                        log.error("Error creando URI para la respuesta: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(relation);
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error asignando usuario a institución: {}", error.getMessage());
                    if (error.getMessage().contains("no encontrado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                    }
                    if (error.getMessage().contains("no está activo")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                    }
                    if (error.getMessage().contains("ya está asignado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping("/users/by-institution/{institutionId}")
    public Flux<UserInstitutionRelation> getUsersByInstitution(@PathVariable String institutionId) {
        log.info("Getting users by institution: {}", institutionId);
        
        return userInstitutionService.getUsersByInstitution(institutionId)
                .doOnError(error -> log.error("Error getting users by institution: {}", error.getMessage()));
    }

    @PutMapping("/users/{userId}/institutions/{institutionId}/roles")
    public Mono<ResponseEntity<UserInstitutionRelation>> updateUserRoleInInstitution(
            @PathVariable String userId,
            @PathVariable String institutionId,
            @Valid @RequestBody UpdateRoleRequest request) {
        log.info("Updating role for user {} in institution {} to {} with assignmentDate: {} and endDate: {}", 
                userId, institutionId, request.getNewRole(), request.getAssignmentDate(), request.getEndDate());
        
        return userInstitutionService.updateUserRoleInInstitution(userId, institutionId, request)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error updating user role: {}", error.getMessage());
                    if (error.getMessage().contains("not found")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PatchMapping("/users/{userId}/institutions/{institutionId}/deactivate")
    public Mono<ResponseEntity<UserInstitutionRelation>> deactivateUserInstitutionAssignment(
            @PathVariable String userId,
            @PathVariable String institutionId) {
        log.info("Deactivating assignment for user {} in institution {}", userId, institutionId);
        
        return userInstitutionService.deactivateUserInstitutionAssignment(userId, institutionId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error deactivating assignment: {}", error.getMessage());
                    if (error.getMessage().contains("not found")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PatchMapping("/users/{userId}/institutions/{institutionId}/activate")
    public Mono<ResponseEntity<UserInstitutionRelation>> activateUserInstitutionAssignment(
            @PathVariable String userId,
            @PathVariable String institutionId) {
        log.info("Activating assignment for user {} in institution {}", userId, institutionId);
        
        return userInstitutionService.activateUserInstitutionAssignment(userId, institutionId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error activating assignment: {}", error.getMessage());
                    if (error.getMessage().contains("not found")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @DeleteMapping("/users/{userId}/institutions/all")
    public Mono<ResponseEntity<String>> deleteUserInstitutionRelation(@PathVariable String userId) {
        log.info("Solicitando eliminación física completa de relaciones para usuario ID: {}", userId);
        
        return userInstitutionService.deleteUserInstitutionRelation(userId)
                .then(Mono.just(ResponseEntity.ok()
                        .body("✅ Todas las relaciones usuario-institución han sido eliminadas exitosamente para el usuario: " + userId)))
                .onErrorResume(error -> {
                    log.error("Error eliminando relaciones usuario-institución: {}", error.getMessage());
                    
                    if (error.getMessage().contains("Usuario no encontrado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ Usuario no encontrado con ID: " + userId));
                    }
                    if (error.getMessage().contains("No se encontraron relaciones")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ No existen relaciones usuario-institución para el usuario: " + userId));
                    }
                    
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("❌ Error interno del servidor: " + error.getMessage()));
                });
    }

    @DeleteMapping("/users/{userId}/institutions/{institutionId}")
    public Mono<ResponseEntity<String>> deleteSpecificInstitutionAssignment(
            @PathVariable String userId,
            @PathVariable String institutionId) {
        log.info("Solicitando eliminación física de asignación específica - Usuario: {}, Institución: {}", userId, institutionId);
        
        return userInstitutionService.deleteSpecificInstitutionAssignment(userId, institutionId)
                .then(Mono.just(ResponseEntity.ok()
                        .body("✅ Asignación eliminada exitosamente para usuario: " + userId + " en institución: " + institutionId)))
                .onErrorResume(error -> {
                    log.error("Error eliminando asignación específica: {}", error.getMessage());
                    
                    if (error.getMessage().contains("Usuario no encontrado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ Usuario no encontrado con ID: " + userId));
                    }
                    if (error.getMessage().contains("No se encontraron relaciones")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ No existen relaciones para el usuario: " + userId));
                    }
                    if (error.getMessage().contains("No se encontró asignación")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ No existe asignación para la institución: " + institutionId));
                    }
                    
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("❌ Error interno del servidor: " + error.getMessage()));
                });
    }

    @GetMapping("/users/{userId}/institutions/{institutionId}")
    public Mono<ResponseEntity<UserInstitutionRelation>> getUserInstitutionRelation(
            @PathVariable String userId,
            @PathVariable String institutionId) {
        log.info("Getting relation for user {} and institution {}", userId, institutionId);
        
        return userInstitutionService.getUserInstitutionRelation(userId, institutionId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(error -> {
                    log.error("Error getting user-institution relation: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/users/{userId}/institutions")
    public Mono<ResponseEntity<UserInstitutionRelation>> getUserInstitutionRelations(@PathVariable String userId) {
        log.info("Getting all relations for user: {}", userId);
        
        return userInstitutionService.getUserInstitutionRelations(userId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(error -> {
                    log.error("Error getting user-institution relations: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/users/{userId}/institutions/exists")
    public Mono<ResponseEntity<String>> checkUserInstitutionRelationsExist(@PathVariable String userId) {
        log.info("Verificando si existen relaciones para usuario: {}", userId);
        
        return userInstitutionService.getUserInstitutionRelations(userId)
                .map(relation -> ResponseEntity.ok()
                        .body("✅ El usuario tiene " + relation.getInstitutionAssignments().size() + " asignaciones activas"))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ No se encontraron relaciones para el usuario: " + userId)))
                .onErrorResume(error -> {
                    log.error("Error verificando relaciones: {}", error.getMessage());
                    if (error.getMessage().contains("Usuario no encontrado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("❌ Usuario no encontrado con ID: " + userId));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("❌ Error interno: " + error.getMessage()));
                });
    }

    @PatchMapping("/users/{userId}/institutions/status")
    public Mono<ResponseEntity<UserInstitutionRelation>> changeRelationStatus(
            @PathVariable String userId,
            @RequestParam UserStatus status) {
        log.info("Cambiando estado GENERAL de relación para usuario {} a {}", userId, status);
        
        return userInstitutionService.changeRelationStatus(userId, status)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error cambiando estado general de relación: {}", error.getMessage());
                    if (error.getMessage().contains("no encontrado")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PatchMapping("/users/{userId}/institutions/deactivate-all")
    public Mono<ResponseEntity<UserInstitutionRelation>> deactivateAllUserInstitutionAssignments(@PathVariable String userId) {
        log.info("Desactivando TODAS las asignaciones para usuario: {}", userId);
        
        return userInstitutionService.deactivateAllUserInstitutionAssignments(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error desactivando todas las asignaciones: {}", error.getMessage());
                    if (error.getMessage().contains("no encontrado")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PatchMapping("/users/{userId}/institutions/activate-all")
    public Mono<ResponseEntity<UserInstitutionRelation>> activateAllUserInstitutionAssignments(@PathVariable String userId) {
        log.info("Activando TODAS las asignaciones para usuario: {}", userId);
        
        return userInstitutionService.activateAllUserInstitutionAssignments(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error activando todas las asignaciones: {}", error.getMessage());
                    if (error.getMessage().contains("no encontrado")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
