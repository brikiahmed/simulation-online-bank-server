package examen.test.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import examen.test.entities.User;
import examen.test.repositories.UserRepository;

@Service
public class UserServices implements IUserServices {
	
	@Autowired 
	UserRepository userRepository ;

	@Override
	public User CreateUser(User user) {
		
		return userRepository.save(user) ;	
	}

	@Override
	public User UpdateUser(User newUser, Long id) {
		User u = userRepository.findById(id).orElse(null);
		if(u != null) {
			u.setFirstName(newUser.getFirstName());
			u.setLastName(newUser.getLastName());
			u.setEmail(newUser.getEmail());
			u.setMatricule(newUser.getMatricule());
			u.setEmail(newUser.getEmail());
			u.setRoles(newUser.getRoles());
			u.setPassword(newUser.getPassword());
			u.setAddress(newUser.getAddress());
			u.setPhone(newUser.getPhone());
			userRepository.save(u);
			userRepository.flush();
		}
		return u;
	}

	@Override
	public List<User> GetAllUser() {
		
		return userRepository.findAll() ;
	}

	@Override
	public User getUserbyId(Long id) {
		
		return userRepository.findById(id).orElse(null) ;
	}

	@Override
	public void RemoveUser(Long id) {
		userRepository.deleteById(id);
		
	}

	public User getUserByVerificationToken(String verificationToken) {
		return userRepository.findByToken(verificationToken);
	}

	public void saveUser(User user)
	{
		userRepository.save(user);
	}

}
