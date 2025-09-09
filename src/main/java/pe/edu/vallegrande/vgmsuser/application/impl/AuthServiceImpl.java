package pe.edu.vallegrande.vgmsuser.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsuser.application.service.IAuthService;
import pe.edu.vallegrande.vgmsuser.application.service.IEmailService;
import pe.edu.vallegrande.vgmsuser.application.service.IKeycloakService;
import pe.edu.vallegrande.vgmsuser.application.service.IUserProfileService;
import pe.edu.vallegrande.vgmsuser.domain.model.UserProfile;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.PasswordStatus;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserProfileService userProfileService;
    private final IKeycloakService keycloakService;
    private final IEmailService emailService;

    @Override
    public Mono<String> generatePasswordResetToken(String keycloakId) {
        log.info("Generating password reset token for keycloakId: {}", keycloakId);
        
        String resetToken = UUID.randomUUID().toString();
        
        return userProfileService.findByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(userProfile -> {
                    userProfile.setPasswordResetToken(resetToken);
                    userProfile.setUpdatedAt(LocalDateTime.now());
                    
                    return userProfileService.save(userProfile)
                            .then(emailService.sendPasswordResetEmail(
                                    userProfile.getEmail(), 
                                    userProfile.getUsername(), 
                                    resetToken))
                            .thenReturn(resetToken);
                })
                .doOnError(error -> log.error("Error generating reset token: {}", error.getMessage()));
    }

    @Override
    public Mono<String> resetPassword(String token, String newPassword) {
        log.info("Resetting password with token: {} (length: {})", token, token != null ? token.length() : 0);
        
        return userProfileService.findByPasswordResetToken(token)
                .doOnNext(profile -> log.info("Found user profile for token: {} - User: {}", token, profile.getUsername()))
                .switchIfEmpty(Mono.error(new RuntimeException("Token inválido o expirado")))
                .flatMap(userProfile -> {
                    // Cambiar contraseña en Keycloak
                    return keycloakService.changePassword(userProfile.getKeycloakId(), newPassword)
                            .then(Mono.defer(() -> {
                                // Actualizar estado en MongoDB
                                userProfile.setPasswordStatus(PasswordStatus.PERMANENT);
                                userProfile.setPasswordCreatedAt(LocalDateTime.now());
                                userProfile.setPasswordResetToken(null); // Limpiar token
                                userProfile.setUpdatedAt(LocalDateTime.now());
                                
                                return userProfileService.save(userProfile)
                                        .then(emailService.sendPasswordChangeConfirmationEmail(
                                                userProfile.getEmail(), 
                                                userProfile.getUsername()))
                                        .thenReturn("Contraseña cambiada exitosamente");
                            }));
                })
                .doOnError(error -> log.error("Error resetting password: {}", error.getMessage()));
    }

    @Override
    public Mono<String> forcePasswordChange(String keycloakId, String currentPassword, String newPassword) {
        log.info("Forcing password change for keycloakId: {}", keycloakId);
        
        return userProfileService.findByKeycloakId(keycloakId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(userProfile -> {
                    // Verificar que la contraseña actual sea temporal
                    if (userProfile.getPasswordStatus() != PasswordStatus.TEMPORARY) {
                        return Mono.error(new RuntimeException("La contraseña ya ha sido cambiada"));
                    }
                    
                    // Cambiar contraseña en Keycloak
                    return keycloakService.changePassword(keycloakId, newPassword)
                            .then(Mono.defer(() -> {
                                // Actualizar estado en MongoDB
                                userProfile.setPasswordStatus(PasswordStatus.PERMANENT);
                                userProfile.setPasswordCreatedAt(LocalDateTime.now());
                                userProfile.setPasswordResetToken(null);
                                userProfile.setUpdatedAt(LocalDateTime.now());
                                
                                return userProfileService.save(userProfile)
                                        .then(emailService.sendPasswordChangeConfirmationEmail(
                                                userProfile.getEmail(), 
                                                userProfile.getUsername()))
                                        .thenReturn("Contraseña cambiada exitosamente. Tu cuenta está ahora activa.");
                            }));
                })
                .doOnError(error -> log.error("Error forcing password change: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> isPasswordTemporary(String keycloakId) {
        log.info("Checking if password is temporary for keycloakId: {}", keycloakId);
        
        return userProfileService.findByKeycloakId(keycloakId)
                .map(userProfile -> userProfile.getPasswordStatus() == PasswordStatus.TEMPORARY)
                .defaultIfEmpty(false)
                .doOnError(error -> log.error("Error checking password status: {}", error.getMessage()));
    }
}
