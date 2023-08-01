package examen.test.services;

import java.util.List;

import examen.test.entities.User;

public interface IUserServices {
	
	User CreateUser (User User) ;
	
	User UpdateUser (User newUser , Long id) ;
	
	List<User> GetAllUser () ;
	
	User getUserbyId (Long id) ;
	
	void RemoveUser(Long id ) ;

}
