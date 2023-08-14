package examen.test.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import examen.test.services.EmailService;
import examen.test.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import examen.test.entities.ERole;
import examen.test.entities.Role;
import examen.test.entities.User;
import examen.test.payload.request.LoginRequest;
import examen.test.payload.request.SignupRequest;
import examen.test.payload.response.JwtResponse;
import examen.test.payload.response.MessageResponse;
import examen.test.repositories.RoleRepository;
import examen.test.repositories.UserRepository;
import examen.test.security.jwt.JwtUtils;
import examen.test.security.services.UserDetailsImpl;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository ;

	@Autowired
	UserServices userService ;

	@Autowired
	RoleRepository roleRepository ;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils ;

	@Autowired
	EmailService emailService ;

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam("token") String token) {
		// 1. Retrieve the user associated with the token from the database
		User user = userService.getUserByVerificationToken(token);

		// 2. Verify that the token is valid and has not expired
		if (user == null) {
			// Token is invalid or expired
			return "Email verification failed. Invalid token.";
		}

		// 3. Update the user's email verification status
		user.setEmailVerified(true);
		userService.saveUser(user);
		return "Email verification successful!";
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Check if email is verified
		if (!userDetails.isEmailVerified()) {
			return ResponseEntity.badRequest().body("Email not verified");
		}
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(null, signUpRequest.getUsername(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()), null, null, null, null, null, null, false, null);

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		// Generate verification token
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		user.setRoles(roles);
		userRepository.save(user);

		// Send verification email
		sendVerificationEmail(user, token);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestBody) {
		String email = requestBody.get("email");
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}

		// Generate a password reset token and send the email to the user
		String resetToken = UUID.randomUUID().toString();
		user.setToken(resetToken);
		userService.saveUser(user);
		sendResetPasswordEmail(user, resetToken);

		return ResponseEntity.ok("An email has been sent to reset your password.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		String newPassword = request.get("newPassword");

		User user = userRepository.findByToken(token);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Invalid.");
		}

		user.setPassword(encoder.encode(newPassword));
		userService.saveUser(user);

		return ResponseEntity.ok().body("Le mot de passe a été réinitialisé avec succès.");
	}

	private void sendVerificationEmail(User user, String token) {
		// Build the verification URL with the token
		String verificationUrl = "http://localhost:4200/verify-email?token=" + token;

		String to = "mahdighofrane01@gmail.com";
		String subject = "Email Verification";
		String body = "Bienvenue dans notre application \n" +
				"Pour confirmer votre compte, veuillez cliquer sur ce lien: " + verificationUrl +"\n Merci d'avance!";
		System.out.println(body);
		emailService.sendEmail(to, subject, body);
	}

	private void sendResetPasswordEmail(User user, String token) {
		// Build the verification URL with the token
		String verificationUrl = "http://localhost:4200/reset-password?token=" + token;

		String to = "mahdighofrane01@gmail.com";
		String subject = "Reset password verification";
		String body = "Bienvenue dans notre application \n" +
				"Pour réinitialiser votre mot de passe, veuillez cliquer sur ce lien: " + verificationUrl +"\n Merci d'avance!";
		System.out.println(body);
		emailService.sendEmail(to, subject, body);
	}
}
