package examen.test.services;

import java.util.List;

import examen.test.entities.Request;


public interface IRequestServices {
	
	Request CreateRequest (Request request) ;
	
	Request UpdateRequest (Request newRequest, Long id) ;
	
	List<Request> GetAllRequest () ;
	List<Request> GetAllArchivedRequest () ;
	
	Request getRequestbyId (Long id) ;
	
	void RemoveRequest(Long id ) ;

	boolean isDuplicateRequest(String email, String firstName, String typeRequest);
}
