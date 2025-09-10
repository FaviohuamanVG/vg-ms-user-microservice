package pe.edu.vallegrande.vgmsuser.application.service;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import pe.edu.vallegrande.vgmsuser.domain.model.User;
import reactor.core.publisher.Mono;

public interface IKeycloakService {
    Mono<List<UserRepresentation>> findAllUsers();
    Mono<List<UserRepresentation>> searchUserByUsername(String username);
    Mono<String> createUser(User userDTO);
    /**
     * Eliminar usuario de Keycloak
     */
    Mono<String> deleteUser(String keycloakId);
    
    /**
     * Cambiar contraseña de usuario en Keycloak
     */
    Mono<Void> changePassword(String keycloakId, String newPassword);
    Mono<Void> updateUser(String userId, User userDTO);
    
    /**
     * Activar usuario en Keycloak (enabled = true)
     */
    Mono<Void> enableUser(String keycloakId);
    
    /**
     * Desactivar usuario en Keycloak (enabled = false) - Eliminado lógico
     */
    Mono<Void> disableUser(String keycloakId);
}