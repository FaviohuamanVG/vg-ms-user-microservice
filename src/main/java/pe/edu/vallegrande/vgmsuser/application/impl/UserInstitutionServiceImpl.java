package pe.edu.vallegrande.vgmsuser.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsuser.application.service.IUserInstitutionService;
import pe.edu.vallegrande.vgmsuser.domain.model.*;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.AssignInstitutionRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.UpdateRoleRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.*;
import pe.edu.vallegrande.vgmsuser.infraestructure.repository.UserInstitutionRelationRepository;
import pe.edu.vallegrande.vgmsuser.infraestructure.repository.UserProfileRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInstitutionServiceImpl implements IUserInstitutionService {

    private final UserInstitutionRelationRepository relationRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Flux<UserInstitutionRelation> getAllUserInstitutionRelations() {
        log.info("Getting all user-institution relations");
        return relationRepository.findAll();
    }

    @Override
    public Mono<UserInstitutionRelation> assignUserToInstitution(String userId, AssignInstitutionRequest request) {
        log.info("Assigning user {} to institution {}", userId, request.getInstitutionId());
        
        // Primero validar que el usuario existe y está activo
        return userProfileRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(userProfile -> {
                    // Validar que el usuario está activo
                    if (userProfile.getStatus() != UserStatus.ACTIVE) {
                        return Mono.error(new RuntimeException("El usuario no está activo. Estado actual: " + userProfile.getStatus()));
                    }
                    
                    log.info("Usuario validado: {} - {} ({})", userProfile.getUsername(), 
                            userProfile.getFirstname() + " " + userProfile.getLastname(), userProfile.getStatus());
                    
                    // Continuar con la asignación
                    return relationRepository.findByUserId(userId)
                            .switchIfEmpty(createNewRelation(userId))
                            .flatMap(relation -> {
                                // Verificar si ya existe una asignación a esta institución
                                boolean alreadyAssigned = relation.getInstitutionAssignments().stream()
                                        .anyMatch(assignment -> assignment.getInstitutionId().equals(request.getInstitutionId())
                                                && assignment.getStatus() == AssignmentStatus.ACTIVE);
                                
                                if (alreadyAssigned) {
                                    return Mono.error(new RuntimeException("El usuario ya está asignado a esta institución"));
                                }
                                
                                // Crear nueva asignación
                                InstitutionAssignment newAssignment = InstitutionAssignment.builder()
                                        .institutionId(request.getInstitutionId())
                                        .role(request.getRole())
                                        .assignmentDate(request.getAssignmentDate() != null ? request.getAssignmentDate() : LocalDateTime.now())
                                        .endDate(request.getEndDate())
                                        .status(AssignmentStatus.ACTIVE)
                                        .movements(new ArrayList<>())
                                        .build();
                                
                                // Agregar movimiento inicial con información del usuario
                                AssignmentMovement initialMovement = AssignmentMovement.builder()
                                        .date(LocalDateTime.now())
                                        .action(AssignmentAction.ASSIGNED)
                                        .newRole(request.getRole())
                                        .newStatus(AssignmentStatus.ACTIVE)
                                        .description(request.getDescription() != null ? request.getDescription() : 
                                                "Asignación inicial de " + userProfile.getFirstname() + " " + userProfile.getLastname() + 
                                                " como " + request.getRole())
                                        .build();
                                
                                newAssignment.getMovements().add(initialMovement);
                                relation.getInstitutionAssignments().add(newAssignment);
                                relation.setUpdatedAt(LocalDateTime.now());
                                
                                log.info("Asignación creada exitosamente para usuario: {} en institución: {} con rol: {}", 
                                        userProfile.getUsername(), request.getInstitutionId(), request.getRole());
                                
                                return relationRepository.save(relation);
                            });
                });
    }

    @Override
    public Flux<UserInstitutionRelation> getUsersByInstitution(String institutionId) {
        log.info("Getting users by institution: {}", institutionId);
        return relationRepository.findByInstitutionId(institutionId);
    }

    @Override
    public Mono<UserInstitutionRelation> updateUserRoleInInstitution(String userId, String institutionId, UpdateRoleRequest request) {
        log.info("Updating role for user {} in institution {} to {}", userId, institutionId, request.getNewRole());
        
        // Validar que el usuario existe primero
        return userProfileRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(userProfile -> {
                    log.info("Actualizando rol para usuario: {} - {}", userProfile.getUsername(), 
                            userProfile.getFirstname() + " " + userProfile.getLastname());
                    
                    return relationRepository.findByUserIdAndInstitutionId(userId, institutionId)
                            .switchIfEmpty(Mono.error(new RuntimeException("Relación Usuario-Institución no encontrada")))
                            .flatMap(relation -> {
                                // Buscar la asignación activa actual
                                InstitutionAssignment currentAssignment = relation.getInstitutionAssignments().stream()
                                        .filter(a -> a.getInstitutionId().equals(institutionId) && a.getStatus() == AssignmentStatus.ACTIVE)
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Asignación activa no encontrada"));
                                
                                InstitutionRole oldRole = currentAssignment.getRole();
                                
                                // 1. CAMBIAR EL ESTADO A INACTIVE (temporalmente)
                                currentAssignment.setStatus(AssignmentStatus.INACTIVE);
                                
                                // Agregar movimiento de terminación del rol anterior
                                AssignmentMovement terminationMovement = AssignmentMovement.builder()
                                        .date(LocalDateTime.now())
                                        .action(AssignmentAction.STATUS_CHANGED)
                                        .oldRole(oldRole)
                                        .newRole(null)
                                        .oldStatus(AssignmentStatus.ACTIVE)
                                        .newStatus(AssignmentStatus.INACTIVE)
                                        .description("Rol " + oldRole + " terminado para cambio a " + request.getNewRole())
                                        .build();
                                
                                currentAssignment.getMovements().add(terminationMovement);
                                
                                // 2. ACTUALIZAR EL ROL Y REACTIVAR
                                currentAssignment.setRole(request.getNewRole());
                                currentAssignment.setStatus(AssignmentStatus.ACTIVE);
                                
                                // Agregar movimiento de cambio de rol (con nueva activación)
                                AssignmentMovement roleChangeMovement = AssignmentMovement.builder()
                                        .date(LocalDateTime.now())
                                        .action(AssignmentAction.ROLE_CHANGED)
                                        .oldRole(oldRole)
                                        .newRole(request.getNewRole())
                                        .oldStatus(AssignmentStatus.INACTIVE)
                                        .newStatus(AssignmentStatus.ACTIVE)
                                        .description(request.getDescription() != null ? request.getDescription() : 
                                                "Cambio de rol de " + userProfile.getFirstname() + " " + userProfile.getLastname() + 
                                                " de " + oldRole + " a " + request.getNewRole())
                                        .build();
                                
                                currentAssignment.getMovements().add(roleChangeMovement);
                                relation.setUpdatedAt(LocalDateTime.now());
                                
                                log.info("Rol actualizado exitosamente para usuario: {} de {} a {} (misma asignación)", 
                                        userProfile.getUsername(), oldRole, request.getNewRole());
                                
                                return relationRepository.save(relation);
                            });
                });
    }

    @Override
    public Mono<UserInstitutionRelation> deactivateUserInstitutionAssignment(String userId, String institutionId) {
        log.info("Deactivating assignment for user {} in institution {}", userId, institutionId);
        
        return updateAssignmentStatus(userId, institutionId, AssignmentStatus.INACTIVE, "Assignment deactivated");
    }

    @Override
    public Mono<UserInstitutionRelation> activateUserInstitutionAssignment(String userId, String institutionId) {
        log.info("Activating assignment for user {} in institution {}", userId, institutionId);
        
        return updateAssignmentStatus(userId, institutionId, AssignmentStatus.ACTIVE, "Assignment activated");
    }

    @Override
    public Mono<Void> deleteUserInstitutionRelation(String userId) {
        log.info("Iniciando eliminación física completa de relaciones usuario-institución para usuario: {}", userId);
        
        // Primero validar que el usuario existe
        return userProfileRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(userProfile -> {
                    log.info("Usuario encontrado: {} - {} ({})", userProfile.getUsername(), 
                            userProfile.getFirstname() + " " + userProfile.getLastname(), userProfile.getStatus());
                    
                    // Buscar todas las relaciones del usuario
                    return relationRepository.findByUserId(userId)
                            .switchIfEmpty(Mono.error(new RuntimeException("No se encontraron relaciones usuario-institución para el usuario: " + userId)))
                            .flatMap(relation -> {
                                log.info("Eliminando {} asignaciones de instituciones para usuario: {}", 
                                        relation.getInstitutionAssignments().size(), userProfile.getUsername());
                                
                                // Eliminar completamente el documento de la base de datos
                                return relationRepository.delete(relation)
                                        .doOnSuccess(unused -> 
                                            log.info("✅ Relaciones usuario-institución eliminadas exitosamente para usuario: {} (ID: {})", 
                                                    userProfile.getUsername(), userId))
                                        .doOnError(error -> 
                                            log.error("❌ Error eliminando relaciones para usuario: {} - Error: {}", 
                                                    userProfile.getUsername(), error.getMessage()));
                            });
                })
                .onErrorMap(error -> {
                    log.error("Error en eliminación física: {}", error.getMessage());
                    return error;
                });
    }

    @Override
    public Mono<Void> deleteSpecificInstitutionAssignment(String userId, String institutionId) {
        log.info("Eliminando asignación específica para usuario: {} en institución: {}", userId, institutionId);
        
        return userProfileRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(userProfile -> {
                    return relationRepository.findByUserId(userId)
                            .switchIfEmpty(Mono.error(new RuntimeException("No se encontraron relaciones para el usuario: " + userId)))
                            .flatMap(relation -> {
                                // Filtrar las asignaciones para remover la institución específica
                                boolean assignmentRemoved = relation.getInstitutionAssignments()
                                        .removeIf(assignment -> assignment.getInstitutionId().equals(institutionId));
                                
                                if (!assignmentRemoved) {
                                    return Mono.error(new RuntimeException("No se encontró asignación para la institución: " + institutionId));
                                }
                                
                                // Si no quedan más asignaciones, eliminar todo el documento
                                if (relation.getInstitutionAssignments().isEmpty()) {
                                    log.info("No quedan más asignaciones, eliminando documento completo para usuario: {}", userProfile.getUsername());
                                    return relationRepository.delete(relation);
                                } else {
                                    // Actualizar el documento sin la asignación eliminada
                                    relation.setUpdatedAt(LocalDateTime.now());
                                    log.info("Asignación eliminada, actualizando documento para usuario: {}", userProfile.getUsername());
                                    return relationRepository.save(relation).then();
                                }
                            });
                })
                .doOnSuccess(unused -> 
                    log.info("✅ Asignación específica eliminada exitosamente para usuario: {} en institución: {}", userId, institutionId))
                .doOnError(error -> 
                    log.error("❌ Error eliminando asignación específica: {}", error.getMessage()));
    }

    @Override
    public Mono<UserInstitutionRelation> getUserInstitutionRelation(String userId, String institutionId) {
        log.info("Getting relation for user {} and institution {}", userId, institutionId);
        
        return relationRepository.findByUserIdAndInstitutionId(userId, institutionId)
                .switchIfEmpty(Mono.error(new RuntimeException("User-Institution relation not found")));
    }

    @Override
    public Mono<UserInstitutionRelation> getUserInstitutionRelations(String userId) {
        log.info("Getting all relations for user: {}", userId);
        
        return relationRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User-Institution relations not found")));
    }

    @Override
    public Mono<UserInstitutionRelation> changeRelationStatus(String userId, UserStatus status) {
        log.info("Changing relation status for user {} to {}", userId, status);
        
        return relationRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User-Institution relation not found")))
                .flatMap(relation -> {
                    relation.setStatus(status);
                    relation.setUpdatedAt(LocalDateTime.now());
                    return relationRepository.save(relation);
                });
    }

    @Override
    public Mono<UserInstitutionRelation> deactivateAllUserInstitutionAssignments(String userId) {
        log.info("Desactivando TODAS las asignaciones para usuario: {}", userId);
        
        return relationRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User-Institution relation not found")))
                .flatMap(relation -> {
                    // Desactivar TODAS las asignaciones individuales (sin historial)
                    relation.getInstitutionAssignments().forEach(assignment -> {
                        assignment.setStatus(AssignmentStatus.INACTIVE);
                    });
                    
                    relation.setUpdatedAt(LocalDateTime.now());
                    return relationRepository.save(relation);
                });
    }

    @Override
    public Mono<UserInstitutionRelation> activateAllUserInstitutionAssignments(String userId) {
        log.info("Activando TODAS las asignaciones para usuario: {}", userId);
        
        return relationRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User-Institution relation not found")))
                .flatMap(relation -> {
                    // Activar TODAS las asignaciones individuales (sin historial)
                    relation.getInstitutionAssignments().forEach(assignment -> {
                        assignment.setStatus(AssignmentStatus.ACTIVE);
                    });
                    
                    relation.setUpdatedAt(LocalDateTime.now());
                    return relationRepository.save(relation);
                });
    }

    private Mono<UserInstitutionRelation> createNewRelation(String userId) {
        UserInstitutionRelation newRelation = UserInstitutionRelation.builder()
                .userId(userId)
                .institutionAssignments(new ArrayList<>())
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
        
        return Mono.just(newRelation);
    }

    private Mono<UserInstitutionRelation> updateAssignmentStatus(String userId, String institutionId, 
                                                                AssignmentStatus newStatus, String description) {
        // Validar que el usuario existe primero
        return userProfileRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(userProfile -> {
                    log.info("Actualizando estado de asignación para usuario: {} - {}", userProfile.getUsername(), 
                            userProfile.getFirstname() + " " + userProfile.getLastname());
                    
                    return relationRepository.findByUserIdAndInstitutionId(userId, institutionId)
                            .switchIfEmpty(Mono.error(new RuntimeException("Relación Usuario-Institución no encontrada")))
                            .flatMap(relation -> {
                                InstitutionAssignment assignment = relation.getInstitutionAssignments().stream()
                                        .filter(a -> a.getInstitutionId().equals(institutionId))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
                                
                                AssignmentStatus oldStatus = assignment.getStatus();
                                
                                // Solo actualizar el estado, sin movimientos adicionales
                                assignment.setStatus(newStatus);
                                relation.setUpdatedAt(LocalDateTime.now());
                                
                                log.info("Estado de asignación actualizado para usuario: {} de {} a {} (sin historial adicional)", 
                                        userProfile.getUsername(), oldStatus, newStatus);
                                
                                return relationRepository.save(relation);
                            });
                });
    }
}
