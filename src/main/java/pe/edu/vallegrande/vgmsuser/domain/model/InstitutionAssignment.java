package pe.edu.vallegrande.vgmsuser.domain.model;

import lombok.*;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.AssignmentStatus;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.InstitutionRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstitutionAssignment {
    
    private String institutionId;
    
    private InstitutionRole role;
    
    private LocalDateTime assignmentDate;
    
    private LocalDateTime endDate;
    
    @Builder.Default
    private List<AssignmentMovement> movements = new java.util.ArrayList<>();
    
    @Builder.Default
    private AssignmentStatus status = AssignmentStatus.ACTIVE;
}
