package pe.edu.vallegrande.vgmsuser.domain.model.dto;

import lombok.*;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.InstitutionRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignInstitutionRequest {
    
    @NotBlank(message = "Institution ID is required")
    private String institutionId;
    
    @NotNull(message = "Role is required")
    private InstitutionRole role;
    
    private LocalDateTime assignmentDate;
    
    private LocalDateTime endDate;
    
    private String description;
}
