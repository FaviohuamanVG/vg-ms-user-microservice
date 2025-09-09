package pe.edu.vallegrande.vgmsuser.domain.model.dto;

import lombok.*;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.InstitutionRole;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoleRequest {
    
    @NotNull(message = "New role is required")
    private InstitutionRole newRole;
    
    private String description;
}
