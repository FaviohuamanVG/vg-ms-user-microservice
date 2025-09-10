package pe.edu.vallegrande.vgmsuser.application.service;

import pe.edu.vallegrande.vgmsuser.domain.model.User;
import pe.edu.vallegrande.vgmsuser.domain.model.UserProfile;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAdminUserService {
    
    /**
     * Crear usuario admin/director (Keycloak + MongoDB) con roles admin o director
     */
    Mono<String> createAdminUser(User user);
    
    /**
     * Obtener usuario admin/director completo con información de MongoDB
     */
    Mono<UserProfile> getAdminUserByKeycloakId(String keycloakId);
    
    /**
     * Obtener usuario admin/director completo por username
     */
    Mono<UserProfile> getAdminUserByUsername(String username);
    
    /**
     * Actualizar usuario admin/director completo
     */
    Mono<String> updateAdminUser(String keycloakId, User user);
    
    /**
     * Eliminar usuario admin/director completo (Keycloak + MongoDB)
     */
    Mono<String> deleteAdminUser(String keycloakId);
    
    /**
     * Listar todos los usuarios admin/director con información completa
     */
    Flux<UserProfile> getAllAdminUsers();
    
    /**
     * Cambiar estado del usuario admin/director
     */
    Mono<UserProfile> changeAdminUserStatus(String keycloakId, UserStatus status);
    
    /**
     * Activar usuario admin/director (MongoDB: ACTIVE, Keycloak: enabled = true)
     */
    Mono<UserProfile> activateAdminUser(String keycloakId);
    
    /**
     * Desactivar usuario admin/director (MongoDB: INACTIVE, Keycloak: enabled = false)
     */
    Mono<UserProfile> deactivateAdminUser(String keycloakId);
    
    /**
     * Buscar usuarios admin/director por estado
     */
    Flux<UserProfile> getAdminUsersByStatus(UserStatus status);
}
