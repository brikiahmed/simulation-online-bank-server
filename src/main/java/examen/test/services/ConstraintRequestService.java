package examen.test.services;

import examen.test.entities.ConstraintRequest;
import examen.test.repositories.ConstraintRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstraintRequestService implements IConstraintRequestServices{

    @Autowired
    ConstraintRequestRepository constraintRequestRepository;

    @Override
    public ConstraintRequest CreateConstraintRequest(ConstraintRequest constraintRequest) {

        return constraintRequestRepository.save(constraintRequest);
    }

    public List<ConstraintRequest> getAllConstraintRequests() {
        return constraintRequestRepository.getAll();
    }
}
