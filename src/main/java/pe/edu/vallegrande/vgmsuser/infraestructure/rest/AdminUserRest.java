package pe.edu.vallegrande.vgmsuser.infraestructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsuser.application.service.IAdminUserService;
import pe.edu.vallegrande.vgmsuser.domain.model.User;
import pe.edu.vallegrande.vgmsuser.domain.model.UserProfile;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api/v1/user-admin")
@RequiredArgsConstructor
public class AdminUserRest {

    private final IAdminUserService adminUserService;

    @PostMapping("/users")
    public Mono<ResponseEntity<String>> createAdminUser(@Valid @RequestBody User user) {
        log.info("Creating admin/director user with username: {}", user.getUsername());
        
        return adminUserService.createAdminUser(user)
                .map(response -> {
                    try {
                        if (response.contains("exitosamente")) {
                            return ResponseEntity.created(new URI("/api/v1/user-admin/users/"))
                                    .body(response);
                        } else {
                            return ResponseEntity.badRequest().body(response);
                        }
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error creating URI: " + e.getMessage());
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error creating admin/director user: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("Error: " + error.getMessage()));
                });
    }

    @GetMapping("/users")
    public Flux<UserProfile> getAllAdminUsers() {
        log.info("Getting all admin/director users");
        
        return adminUserService.getAllAdminUsers()
                .doOnError(error -> log.error("Error getting all admin/director users: {}", error.getMessage()));
    }

    @GetMapping("/users/keycloak/{keycloakId}")
    public Mono<ResponseEntity<UserProfile>> getAdminUserByKeycloakId(@PathVariable String keycloakId) {
        log.info("Getting admin user by keycloakId: {}", keycloakId);
        
        return adminUserService.getAdminUserByKeycloakId(keycloakId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(error -> {
                    log.error("Error getting admin user by keycloakId: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/users/username/{username}")
    public Mono<ResponseEntity<UserProfile>> getAdminUserByUsername(@PathVariable String username) {
        log.info("Getting admin user by username: {}", username);
        
        return adminUserService.getAdminUserByUsername(username)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(error -> {
                    log.error("Error getting admin user by username: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PutMapping("/users/{keycloakId}")
    public Mono<ResponseEntity<String>> updateAdminUser(
            @PathVariable String keycloakId, 
            @Valid @RequestBody User user) {
        log.info("Updating admin user with keycloakId: {}", keycloakId);
        
        return adminUserService.updateAdminUser(keycloakId, user)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(error -> {
                    log.error("Error updating admin user: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("Error: " + error.getMessage()));
                });
    }

    @DeleteMapping("/users/{keycloakId}")
    public Mono<ResponseEntity<String>> deleteAdminUser(@PathVariable String keycloakId) {
        log.info("Deleting admin user with keycloakId: {}", keycloakId);
        
        return adminUserService.deleteAdminUser(keycloakId)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(error -> {
                    log.error("Error deleting admin user: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("Error: " + error.getMessage()));
                });
    }

    @PatchMapping("/users/{keycloakId}/status")
    public Mono<ResponseEntity<UserProfile>> changeAdminUserStatus(
            @PathVariable String keycloakId, 
            @RequestParam UserStatus status) {
        log.info("Changing admin user status to {} for keycloakId: {}", status, keycloakId);
        
        return adminUserService.changeAdminUserStatus(keycloakId, status)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error changing admin user status: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PatchMapping("/users/{keycloakId}/activate")
    public Mono<ResponseEntity<UserProfile>> activateAdminUser(@PathVariable String keycloakId) {
        log.info("Activating admin user with keycloakId: {}", keycloakId);
        
        return adminUserService.activateAdminUser(keycloakId)
                .map(userProfile -> {
                    log.info("Admin user activated successfully: {}", keycloakId);
                    return ResponseEntity.ok(userProfile);
                })
                .onErrorResume(error -> {
                    log.error("Error activating admin user: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PatchMapping("/users/{keycloakId}/deactivate")
    public Mono<ResponseEntity<UserProfile>> deactivateAdminUser(@PathVariable String keycloakId) {
        log.info("Deactivating admin user with keycloakId: {}", keycloakId);
        
        return adminUserService.deactivateAdminUser(keycloakId)
                .map(userProfile -> {
                    log.info("Admin user deactivated successfully: {}", keycloakId);
                    return ResponseEntity.ok(userProfile);
                })
                .onErrorResume(error -> {
                    log.error("Error deactivating admin user: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/users/status/{status}")
    public Flux<UserProfile> getAdminUsersByStatus(@PathVariable UserStatus status) {
        log.info("Getting admin users by status: {}", status);
        
        return adminUserService.getAdminUsersByStatus(status)
                .doOnError(error -> log.error("Error getting admin users by status: {}", error.getMessage()));
    }
}
