package examen.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import examen.test.entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    long countByEmailAndFirstNameAndTypeRequest(String email, String first_name, String type_request);

    List<Request> getByTypeRequestOrCreationDateOrStatus(String typeRequest, String creationDate, String status);

    List<Request> getByArchived(Boolean archived);
    List<Request> findByArchivedOrArchivedIsNull(Boolean archived);





}
