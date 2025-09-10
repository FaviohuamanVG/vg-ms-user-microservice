package pe.edu.vallegrande.vgmsuser.application.service;

import pe.edu.vallegrande.vgmsuser.domain.model.UserInstitutionRelation;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.AssignInstitutionRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.dto.UpdateRoleRequest;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserInstitutionService {
    
    /**
     * Obtener todas las relaciones usuario-institución
     */
    Flux<UserInstitutionRelation> getAllUserInstitutionRelations();
    
    /**
     * Asignar un usuario a una institución
     */
    Mono<UserInstitutionRelation> assignUserToInstitution(String userId, AssignInstitutionRequest request);
    
    /**
     * Obtener usuarios por institución
     */
    Flux<UserInstitutionRelation> getUsersByInstitution(String institutionId);
    
    /**
     * Actualizar rol de usuario en una institución
     */
    Mono<UserInstitutionRelation> updateUserRoleInInstitution(String userId, String institutionId, UpdateRoleRequest request);
    
    /**
     * Eliminación lógica - cambiar estado de asignación a INACTIVE
     */
    Mono<UserInstitutionRelation> deactivateUserInstitutionAssignment(String userId, String institutionId);
    
    /**
     * Restaurar asignación - cambiar estado a ACTIVE
     */
    Mono<UserInstitutionRelation> activateUserInstitutionAssignment(String userId, String institutionId);
    
    /**
     * Eliminación física - eliminar completamente la relación usuario-institución
     */
    Mono<Void> deleteUserInstitutionRelation(String userId);
    
    /**
     * Eliminación física - eliminar asignación específica de una institución
     */
    Mono<Void> deleteSpecificInstitutionAssignment(String userId, String institutionId);
    
    /**
     * Obtener relación específica por usuario e institución
     */
    Mono<UserInstitutionRelation> getUserInstitutionRelation(String userId, String institutionId);
    
    /**
     * Obtener todas las relaciones de un usuario
     */
    Mono<UserInstitutionRelation> getUserInstitutionRelations(String userId);
    
    /**
     * Cambiar estado general de la relación
     */
    Mono<UserInstitutionRelation> changeRelationStatus(String userId, UserStatus status);

    Mono<UserInstitutionRelation> deactivateAllUserInstitutionAssignments(String userId);

    Mono<UserInstitutionRelation> activateAllUserInstitutionAssignments(String userId);
}
