package pe.edu.vallegrande.vgmsuser.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsuser.application.service.IEmailService;
import pe.edu.vallegrande.vgmsuser.application.service.IKeycloakService;
import pe.edu.vallegrande.vgmsuser.application.service.IUserManagementService;
import pe.edu.vallegrande.vgmsuser.application.service.IUserProfileService;
import pe.edu.vallegrande.vgmsuser.domain.model.User;
import pe.edu.vallegrande.vgmsuser.domain.model.UserProfile;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.PasswordStatus;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.Role;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements IUserManagementService {

    private final IKeycloakService keycloakService;
    private final IUserProfileService userProfileService;
    private final IEmailService emailService;

    @Override
    public Mono<String> createCompleteUser(User user) {
        log.info("Creating complete user with username: {}", user.getUsername());
        
        // Validar que solo se puedan asignar roles teacher, auxiliary o secretary
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            // Si no tiene roles, asignar teacher por defecto
            user.setRoles(Set.of("teacher"));
            log.info("No roles provided, setting default role: teacher");
        } else {
            // Validar que solo contenga roles teacher, auxiliary o secretary
            boolean hasValidRoles = user.getRoles().stream()
                    .allMatch(role -> role.equals("teacher") || 
                                     role.equals("auxiliary") || 
                                     role.equals("secretary"));
            
            if (!hasValidRoles) {
                log.error("Invalid roles provided: {}. Only teacher, auxiliary, secretary allowed", user.getRoles());
                return Mono.error(new RuntimeException("Solo se permiten roles teacher, auxiliary o secretary en este endpoint"));
            }
            log.info("Valid roles provided: {}", user.getRoles());
        }
        
        // Establecer contraseña temporal basada en documentNumber
        String temporaryPassword = user.getDocumentNumber();
        user.setPassword(temporaryPassword);
        log.info("Setting temporary password for user: {}", user.getUsername());
        
        return keycloakService.createUser(user)
                .flatMap(keycloakResponse -> {
                    log.info("Keycloak response received: {}", keycloakResponse);
                    
                    // Extraer el ID de Keycloak de la respuesta
                    String keycloakId = extractKeycloakIdFromResponse(keycloakResponse);
                    log.info("Extracted Keycloak ID: {}", keycloakId);
                    
                    if (keycloakId != null && !keycloakResponse.contains("Error")) {
                        // Generar token de reseteo
                        String resetToken = java.util.UUID.randomUUID().toString();
                        log.info("Generated reset token for user: {} - Token: {}", user.getUsername(), resetToken);
                        
                        // Crear perfil en MongoDB con datos completos
                        UserProfile userProfile = UserProfile.builder()
                                .keycloakId(keycloakId)
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .documentType(user.getDocumentType())
                                .documentNumber(user.getDocumentNumber())
                                .phone(user.getPhone())
                                .status(user.getStatus() != null ? user.getStatus() : UserStatus.ACTIVE)
                                .passwordStatus(PasswordStatus.TEMPORARY)
                                .passwordResetToken(resetToken)
                                .passwordCreatedAt(LocalDateTime.now())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();
                        
                        log.info("Attempting to save user profile to MongoDB for keycloakId: {}", keycloakId);
                        return userProfileService.createUserProfile(userProfile)
                                .doOnNext(savedProfile -> log.info("Successfully saved user profile to MongoDB with ID: {}", savedProfile.getId()))
                                .flatMap(savedProfile -> {
                                    // Enviar correo con credenciales temporales
                                    log.info("Preparing to send temporary credentials email to: {}", user.getEmail());
                                    String fullName = buildFullName(user.getFirstname(), user.getLastname());
                                    
                                    return emailService.sendTemporaryCredentialsEmail(
                                            user.getEmail(),
                                            fullName,
                                            temporaryPassword,
                                            resetToken
                                    )
                                    .doOnSuccess(emailResult -> log.info("Email sent successfully to: {}", user.getEmail()))
                                    .doOnError(emailError -> log.error("Error sending email: {}", emailError.getMessage()))
                                    .then(Mono.fromCallable(() -> 
                                        "Usuario creado exitosamente. Keycloak ID: " + keycloakId + ", MongoDB ID: " + savedProfile.getId()
                                    ));
                                })
                                .onErrorResume(mongoError -> {
                                    log.error("Error creating user profile in MongoDB, rolling back Keycloak user: {}", mongoError.getMessage());
                                    mongoError.printStackTrace(); // Ver el stack trace completo
                                    // Rollback: eliminar usuario de Keycloak
                                    return keycloakService.deleteUser(keycloakId)
                                            .then(Mono.error(new RuntimeException("Error creating user profile: " + mongoError.getMessage())));
                                });
                    } else {
                        log.error("Failed to extract Keycloak ID or error in response: {}", keycloakResponse);
                        return Mono.error(new RuntimeException("Error creating user in Keycloak: " + keycloakResponse));
                    }
                })
                .doOnSuccess(result -> log.info("Complete user creation finished: {}", result))
                .doOnError(error -> log.error("Error creating complete user: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> getCompleteUserByKeycloakId(String keycloakId) {
        log.debug("Getting complete user by keycloakId: {}", keycloakId);
        return userProfileService.findByKeycloakId(keycloakId);
    }

    @Override
    public Mono<UserProfile> getCompleteUserByUsername(String username) {
        log.debug("Getting complete user by username: {}", username);
        return userProfileService.findByUsername(username);
    }

    @Override
    public Mono<String> updateCompleteUser(String keycloakId, User user) {
        log.info("Updating complete user with keycloakId: {}", keycloakId);
        
        // Validar que solo se puedan asignar roles teacher, auxiliary o secretary
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            boolean hasValidRoles = user.getRoles().stream()
                    .allMatch(role -> role.equals(Role.teacher.name()) || 
                                     role.equals(Role.auxiliary.name()) || 
                                     role.equals(Role.secretary.name()));
            
            if (!hasValidRoles) {
                return Mono.error(new RuntimeException("Solo se permiten roles teacher, auxiliary o secretary en este endpoint"));
            }
        }
        
        return userProfileService.findByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
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
                                        .firstname(user.getFirstname())    // ← AGREGADO
                                        .lastname(user.getLastname())      // ← AGREGADO
                                        .documentType(user.getDocumentType())
                                        .documentNumber(user.getDocumentNumber())
                                        .phone(user.getPhone())
                                        .status(user.getStatus() != null ? user.getStatus() : existingProfile.getStatus())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile);
                            }))
                            .map(updatedProfile -> "Usuario actualizado exitosamente. Keycloak ID: " + keycloakId);
                })
                .doOnSuccess(result -> log.info("Complete user update finished: {}", result))
                .doOnError(error -> log.error("Error updating complete user: {}", error.getMessage()));
    }

    @Override
    public Mono<String> deleteCompleteUser(String keycloakId) {
        log.info("Deleting complete user with keycloakId: {}", keycloakId);
        
        return userProfileService.findByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(userProfile -> {
                    // Eliminar de Keycloak primero
                    return keycloakService.deleteUser(keycloakId)
                            .then(userProfileService.deleteUserProfile(keycloakId))
                            .map(v -> "Usuario eliminado exitosamente. Keycloak ID: " + keycloakId);
                })
                .doOnSuccess(result -> log.info("Complete user deletion finished: {}", result))
                .doOnError(error -> log.error("Error deleting complete user: {}", error.getMessage()));
    }

    @Override
    public Flux<UserProfile> getAllCompleteUsers() {
        log.debug("Getting all complete users");
        return userProfileService.findAllUserProfiles();
    }

    @Override
    public Mono<UserProfile> changeUserStatus(String keycloakId, UserStatus status) {
        log.info("Changing user status to {} for keycloakId: {}", status, keycloakId);
        return userProfileService.updateUserStatus(keycloakId, status);
    }

    @Override
    public Flux<UserProfile> getUsersByStatus(UserStatus status) {
        log.debug("Getting users by status: {}", status);
        return userProfileService.findByStatus(status);
    }

    @Override
    public Mono<UserProfile> activateUser(String keycloakId) {
        log.info("Activating user with keycloakId: {}", keycloakId);
        
        return getCompleteUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
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
                                        .status(UserStatus.ACTIVE)
                                        .passwordStatus(existingProfile.getPasswordStatus())
                                        .passwordCreatedAt(existingProfile.getPasswordCreatedAt())
                                        .passwordResetToken(existingProfile.getPasswordResetToken())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile);
                            }));
                })
                .doOnSuccess(profile -> log.info("User activated successfully: {}", keycloakId))
                .doOnError(error -> log.error("Error activating user: {}", error.getMessage()));
    }

    @Override
    public Mono<UserProfile> deactivateUser(String keycloakId) {
        log.info("Deactivating user with keycloakId: {}", keycloakId);
        
        return getCompleteUserByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
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
                                        .status(UserStatus.INACTIVE)
                                        .passwordStatus(existingProfile.getPasswordStatus())
                                        .passwordCreatedAt(existingProfile.getPasswordCreatedAt())
                                        .passwordResetToken(existingProfile.getPasswordResetToken())
                                        .createdAt(existingProfile.getCreatedAt())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                
                                return userProfileService.updateUserProfile(keycloakId, updatedProfile);
                            }));
                })
                .doOnSuccess(profile -> log.info("User deactivated successfully: {}", keycloakId))
                .doOnError(error -> log.error("Error deactivating user: {}", error.getMessage()));
    }

    private String extractKeycloakIdFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Caso 1: Respuesta con mensaje completo
            if (response.contains("Usuario creado exitosamente con ID: ")) {
                String[] parts = response.split("Usuario creado exitosamente con ID: ");
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
            
            // Caso 2: Respuesta es solo el ID (UUID format)
            String trimmedResponse = response.trim();
            if (trimmedResponse.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                log.info("Response is a valid UUID, using as Keycloak ID: {}", trimmedResponse);
                return trimmedResponse;
            }
            
            log.warn("Unable to extract Keycloak ID from response: {}", response);
            return null;
            
        } catch (Exception e) {
            log.error("Error extracting Keycloak ID from response: {}", e.getMessage());
            return null;
        }
    }

    private String buildFullName(String firstname, String lastname) {
        if (firstname != null && lastname != null) {
            return firstname + " " + lastname;
        } else if (firstname != null) {
            return firstname;
        } else if (lastname != null) {
            return lastname;
        } else {
            return "Usuario";
        }
    }
}
