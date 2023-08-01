package examen.test.services;

import examen.test.entities.ConstraintRequest;
import examen.test.entities.Request;

import java.util.List;

public interface IConstraintRequestServices {

    ConstraintRequest CreateConstraintRequest (ConstraintRequest User) ;

    List<ConstraintRequest> getAllConstraintRequests () ;


}
