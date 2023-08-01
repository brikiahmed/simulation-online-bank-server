package examen.test.controllers;

import examen.test.entities.ConstraintRequest;
import examen.test.entities.Request;
import examen.test.repositories.ConstraintRequestRepository;
import examen.test.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/constraint-request")
public class ConstraintRequestController {

	@Autowired
	IConstraintRequestServices iConstraintRequestServices;

	@Autowired
	ConstraintRequestRepository constraintRequestRepository;


	@PostMapping("/create-request-constraint")
	public ResponseEntity<ConstraintRequest> createConstraintRequest(@RequestBody ConstraintRequest constraintRequest) {
		constraintRequest.setCreationDate(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
		ConstraintRequest createdConstraintRequest = iConstraintRequestServices.CreateConstraintRequest(constraintRequest);
		return ResponseEntity.ok(createdConstraintRequest);
	}

	@GetMapping("/get-by-request-id/{requestId}")
	public ResponseEntity<ConstraintRequest> getByRequestId(@PathVariable Long requestId) {
		ConstraintRequest constraintRequest = (ConstraintRequest) constraintRequestRepository.findAllByRequestId(requestId);
		if (constraintRequest != null) {
			return ResponseEntity.ok(constraintRequest);
		} else {
			return ResponseEntity.ok(null);
		}
	}

	@GetMapping("/get-all-constraint-requests")
	public ResponseEntity<List<ConstraintRequest>> getAllConstraintRequests() {
		List<ConstraintRequest> constraintRequests = iConstraintRequestServices.getAllConstraintRequests();
		return ResponseEntity.ok(constraintRequests);
	}

}
