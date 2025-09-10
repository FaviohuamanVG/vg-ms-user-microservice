package pe.edu.vallegrande.vgmsuser.infraestructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.vgmsuser.domain.model.UserInstitutionRelation;
import pe.edu.vallegrande.vgmsuser.domain.model.enums.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserInstitutionRelationRepository extends ReactiveMongoRepository<UserInstitutionRelation, String> {
    
    Mono<UserInstitutionRelation> findByUserId(String userId);
    
    @Query("{'institutionAssignments.institutionId': ?0}")
    Flux<UserInstitutionRelation> findByInstitutionId(String institutionId);
    
    @Query("{'institutionAssignments.institutionId': ?0, 'institutionAssignments.status': ?1}")
    Flux<UserInstitutionRelation> findByInstitutionIdAndAssignmentStatus(String institutionId, String status);
    
    Flux<UserInstitutionRelation> findByStatus(UserStatus status);
    
    @Query("{'userId': ?0, 'institutionAssignments.institutionId': ?1}")
    Mono<UserInstitutionRelation> findByUserIdAndInstitutionId(String userId, String institutionId);
}
