package pe.edu.vallegrande.vgmsuser.domain.model;

import lombok.*;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.AssignmentAction;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.AssignmentStatus;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.InstitutionRole;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentMovement {
    
    private LocalDateTime date;
    
    private AssignmentAction action;
    
    private InstitutionRole oldRole;
    
    private InstitutionRole newRole;
    
    private AssignmentStatus oldStatus;
    
    private AssignmentStatus newStatus;
    
    private String description;
}