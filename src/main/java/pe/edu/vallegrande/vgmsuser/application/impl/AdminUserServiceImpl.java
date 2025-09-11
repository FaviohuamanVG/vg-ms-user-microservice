package pe.edu.vallegrande.vgmsuser.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsuser.application.service.IAdminUserService;
import pe.edu.vallegrande.vgmsuser.application.service.IKeycloakService;
import pe.edu.vallegrande.vgmsuser.application.service.IUserProfileService;
import pe.edu.vallegrande.vgmsuser.application.service.IEmailService;
import pe.edu.vallegrande.vgmsuser.domain.model.User;
import pe.edu.vallegrande.vgmsuser.domain.model.UserProfile;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.Role;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.PasswordStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements IAdminUserService {

    private final IKeycloakService keycloakService;
    private final IUserProfileService userProfileService;
    private final IEmailService emailService;

    @Override
    public Mono<String> createAdminUser(User user) {
        log.info("Creating admin/director user with username: {}", user.getUsername());
        
        // Validar que solo se puedan asignar roles admin o director
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            // Si no tiene roles, asignar admin por defecto
            user.setRoles(Set.of(Role.admin.name()));
        } else {
            // Validar que solo contenga roles admin o director
            boolean hasValidRoles = user.getRoles().stream()
                    .allMatch(role -> role.equals(Role.admin.name()) || role.equals(Role.director.name()));
            
            if (!hasValidRoles) {
                return Mono.error(new RuntimeException("Solo se permiten roles admin o director en este endpoint"));
            }
        }
        
        // Establecer contraseña temporal como el DNI
        String temporaryPassword = user.getDocumentNumber();
        user.setPassword(temporaryPassword);
        
        return keycloakService.createUser(user)
                .flatMap(keycloakUserId -> {
                    log.info("Admin/Director user created in Keycloak with ID: {}", keycloakUserId);
                    
                    // Generar token de reseteo
                    String resetToken = java.util.UUID.randomUUID().toString();
                    
                    // Crear perfil en MongoDB (CON firstname y lastname)
                    UserProfile userProfile = UserProfile.builder()
                            .keycloakId(keycloakUserId)
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .documentType(user.getDocumentType())
                            .documentNumber(user.getDocumentNumber())
                            .phone(user.getPhone())
                            .status(user.getStatus() != null ? user.getStatus() : UserStatus.A)
                            .passwordStatus(PasswordStatus.TEMPORARY)
                            .passwordCreatedAt(LocalDateTime.now())
                            .passwordResetToken(resetToken)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    return userProfileService.createUserProfile(userProfile)
                            .then(emailService.sendTemporaryCredentialsEmail(
                                    user.getEmail(), 
                                    buildFullName(user.getFirstname(), user.getLastname(), user.getUsername()), 
                                    temporaryPassword, 
                                    resetToken))
                            .thenReturn("Usuario admin/director creado exitosamente. Se ha enviado un email con las credenciales temporales a: " + user.getEmail());
                })
                .doOnError(error -> log.error("Error creating admin/director user: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> getAdminUserByKeycloakId(String keycloakId) {
        log.info("Getting admin/director user by keycloakId: {}", keycloakId);
        
        return userProfileService.findByKeycloakId(keycloakId)
                .filter(profile -> isAdminOrDirectorUser(profile))
                .doOnNext(profile -> log.info("Admin/Director user found: {}", profile.getUsername()))
                .doOnError(error -> log.error("Error getting admin/director user by keycloakId: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> getAdminUserByUsername(String username) {
        log.info("Getting admin/director user by username: {}", username);
        
        return userProfileService.findByUsername(username)
                .filter(profile -> isAdminOrDirectorUser(profile))
                .doOnNext(profile -> log.info("Admin/Director user found: {}", profile.getUsername()))
                .doOnError(error -> log.error("Error getting admin/director user by username: {}", error.getMessage()));
    }

    @Override
    public Mono<String> updateAdminUser(String keycloakId, User user) {
        log.info("Updating admin/director user with keycloakId: {}", keycloakId);
        
        // Validar que solo se puedan asignar roles admin o director
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            boolean hasValidRoles = user.getRoles().stream()
                    .allMatch(role -> role.equals(Role.admin.name()) || role.equals(Role.director.name()));
            
            if (!hasValidRoles) {
                return Mono.error(new RuntimeException("Solo se permiten roles admin o director en este endpoint"));
            }
        }
        
        return getAdminUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin/Director user not found")))
                .flatMap(existingProfile -> {
                    // Actualizar en Keycloak
                    return keycloakService.updateUser(keycloakId, user)
                            .then(Mono.defer(() -> {
                                // Actualizar perfil en MongoDB (CON firstname y lastname)
                                UserProfile updatedProfile = UserProfile.builder()
                                        .id(existingProfile.getId())
                                        .keycloakId(keycloakId)
                                        .username(user.getUsername())
                                        .email(user.getEmail())
                                        .firstname(user.getFirstname())
                                        .lastname(user.getLastname())
                                        .documentType(user.getDocumentType())
                                        .documentNumber(user.getDocumentNumber())
                                        .phone(user.getPhone())
                                        .status(user.getStatus() != null ? user.getStatus() : existingProfile.getStatus())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile)
                                        .thenReturn("Usuario admin/director actualizado exitosamente");
                            }));
                })
                .doOnError(error -> log.error("Error updating admin/director user: {}", error.getMessage()));
    }

    @Override
    public Mono<String> deleteAdminUser(String keycloakId) {
        log.info("Deleting admin/director user with keycloakId: {}", keycloakId);
        
        return getAdminUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin/Director user not found")))
                .flatMap(profile -> {
                    // Eliminar de Keycloak
                    return keycloakService.deleteUser(keycloakId)
                            .then(userProfileService.deleteUserProfile(keycloakId))
                            .thenReturn("Usuario admin/director eliminado exitosamente");
                })
                .doOnError(error -> log.error("Error deleting admin/director user: {}", error.getMessage()));
    }

    @Override
    public Flux<UserProfile> getAllAdminUsers() {
        log.info("Getting all admin/director users");
        
        return userProfileService.findAllUserProfiles()
                .filter(this::isAdminOrDirectorUser)
                .doOnError(error -> log.error("Error getting all admin/director users: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> changeAdminUserStatus(String keycloakId, UserStatus status) {
        log.info("Changing admin/director user status to {} for keycloakId: {}", status, keycloakId);
        
        return userProfileService.updateUserStatus(keycloakId, status)
                .doOnError(error -> log.error("Error changing admin/director user status: {}", error.getMessage()));
    }

    @Override
    public Flux<UserProfile> getAdminUsersByStatus(UserStatus status) {
        log.info("Getting admin/director users by status: {}", status);
        
        return userProfileService.findByStatus(status)
                .filter(this::isAdminOrDirectorUser)
                .doOnError(error -> log.error("Error getting admin/director users by status: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> activateAdminUser(String keycloakId) {
        log.info("Activating admin/director user with keycloakId: {}", keycloakId);
        
        return getAdminUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin/Director user not found")))
                .flatMap(existingProfile -> {
                    // Activar en Keycloak (enabled = true)
                    return keycloakService.enableUser(keycloakId)
                            .then(Mono.defer(() -> {
                                // Actualizar estado en MongoDB a ACTIVE
                                UserProfile updatedProfile = UserProfile.builder()
                                        .id(existingProfile.getId())
                                        .keycloakId(existingProfile.getKeycloakId())
                                        .username(existingProfile.getUsername())
                                        .email(existingProfile.getEmail())
                                        .firstname(existingProfile.getFirstname())
                                        .lastname(existingProfile.getLastname())
                                        .documentType(existingProfile.getDocumentType())
                                        .documentNumber(existingProfile.getDocumentNumber())
                                        .phone(existingProfile.getPhone())
                                                                                .status(UserStatus.A)
                                        .passwordStatus(existingProfile.getPasswordStatus())
                                        .passwordCreatedAt(existingProfile.getPasswordCreatedAt())
                                        .passwordResetToken(existingProfile.getPasswordResetToken())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile);
                            }));
                })
                .doOnSuccess(profile -> log.info("Admin/Director user activated successfully: {}", keycloakId))
                .doOnError(error -> log.error("Error activating admin/director user: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> deactivateAdminUser(String keycloakId) {
        log.info("Deactivating admin/director user with keycloakId: {}", keycloakId);
        
        return getAdminUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin/Director user not found")))
                .flatMap(existingProfile -> {
                    // Desactivar en Keycloak (enabled = false) - Eliminado lógico
                    return keycloakService.disableUser(keycloakId)
                            .then(Mono.defer(() -> {
                                // Actualizar estado en MongoDB a INACTIVE
                                UserProfile updatedProfile = UserProfile.builder()
                                        .id(existingProfile.getId())
                                        .keycloakId(existingProfile.getKeycloakId())
                                        .username(existingProfile.getUsername())
                                        .email(existingProfile.getEmail())
                                        .firstname(existingProfile.getFirstname())
                                        .lastname(existingProfile.getLastname())
                                        .documentType(existingProfile.getDocumentType())
                                        .documentNumber(existingProfile.getDocumentNumber())
                                        .phone(existingProfile.getPhone())
                                        .status(UserStatus.I)
                                        .passwordStatus(existingProfile.getPasswordStatus())
                                        .passwordCreatedAt(existingProfile.getPasswordCreatedAt())
                                        .passwordResetToken(existingProfile.getPasswordResetToken())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile);
                            }));
                })
                .doOnSuccess(profile -> log.info("Admin/Director user deactivated successfully: {}", keycloakId))
                .doOnError(error -> log.error("Error deactivating admin/director user: {}", error.getMessage()));
    }

    /**
     * Verifica si un perfil de usuario corresponde a un admin o director
     * (Esta lógica puede expandirse según tus necesidades)
     */
    private boolean isAdminOrDirectorUser(UserProfile profile) {
        // Por ahora, asumimos que todos los usuarios creados por este servicio son admin o director
        // En el futuro podrías agregar un campo "role" o "userType" al UserProfile
        return true;
    }

    /**
     * Construye el nombre completo del usuario
     */
    private String buildFullName(String firstname, String lastname, String username) {
        if (firstname != null && lastname != null && !firstname.trim().isEmpty() && !lastname.trim().isEmpty()) {
            return firstname.trim() + " " + lastname.trim();
        } else if (firstname != null && !firstname.trim().isEmpty()) {
            return firstname.trim();
        } else if (lastname != null && !lastname.trim().isEmpty()) {
            return lastname.trim();
        } else {
            return username; // Fallback al username si no hay nombres
        }
    }
}
