package examen.test.repositories;

import examen.test.entities.ConstraintRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConstraintRequestRepository extends JpaRepository<ConstraintRequest, Long> {
    @Query("SELECT cr FROM ConstraintRequest cr WHERE cr.request.id = :requestId")
    ConstraintRequest findAllByRequestId(@Param("requestId") Long requestId);

    @Query("SELECT cr FROM ConstraintRequest cr")
    List<ConstraintRequest> getAll();
}
