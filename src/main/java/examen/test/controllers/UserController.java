package examen.test.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import examen.test.entities.ERole;
import examen.test.entities.Role;
import examen.test.entities.User;
import examen.test.repositories.RoleRepository;
import examen.test.services.IUserServices;

@CrossOrigin
@RestController
@RequestMapping("/User")
public class UserController {
	@Autowired
	IUserServices iUserServices ;
	
	@Autowired
	RoleRepository roleRepository ;
	
	@PostMapping("/CreateUser")
	public User CreateGabarits (@RequestBody User user) {
		return iUserServices.CreateUser(user);
	}
	
	@PutMapping("/UpdateUser/{id}")
	public ResponseEntity<User> UpdateUser (@RequestBody User user , @PathVariable("id") Long id) {

		user.setRoles(user.getRoles());
		User updatedUser = iUserServices.UpdateUser(user, id);

	    if (updatedUser != null) {
	        return ResponseEntity.ok(updatedUser);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@GetMapping("/get/{id}")
	public User GetUserById (@PathVariable("id") Long id) {
		return iUserServices.getUserbyId(id);
	}
	
	@GetMapping("/all")
	public List<User> GetAllUser() {
		return iUserServices.GetAllUser();
	}
	
	
	@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        iUserServices.RemoveUser(id);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
    }

}
