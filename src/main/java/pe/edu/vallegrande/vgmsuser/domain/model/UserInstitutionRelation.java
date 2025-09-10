package pe.edu.vallegrande.vgmsuser.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_institution_relations")
public class UserInstitutionRelation {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId; // referencia al usuario (ObjectId del UserProfile)
    
    @Builder.Default
    private List<InstitutionAssignment> institutionAssignments = new java.util.ArrayList<>();
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE; // estado general de la transacción
}
