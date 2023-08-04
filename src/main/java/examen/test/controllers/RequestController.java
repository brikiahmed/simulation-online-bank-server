package examen.test.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import examen.test.entities.EmailTemplate;
import examen.test.repositories.RequestRepository;
import examen.test.services.EmailService;
import examen.test.services.IEmailTemplateServices;
import examen.test.services.RequestServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import examen.test.entities.Request;
import examen.test.services.IRequestServices;
@CrossOrigin
@RestController
@RequestMapping("/request")
public class RequestController {

	@Autowired
	IRequestServices iRequestServices;

	@Autowired
	IEmailTemplateServices iEmailTemplateServices;

	@Autowired
	RequestServices requestServices;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	EmailService emailService;

	@PostMapping("/create-request")
	public ResponseEntity<String> createRequest(@RequestBody Request request) {
		String email = request.getEmail();
		String firstName = request.getFirstName();
		String typeRequest = request.getTypeRequest();

		boolean isDuplicate = iRequestServices.isDuplicateRequest(email, firstName, typeRequest);
		if (isDuplicate) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate request detected.");
		}
		request.setStatus("new");
		request.setCreationDate(String.valueOf(LocalDateTime.now()));
		Request createRequest =iRequestServices.CreateRequest(request);

		if (createRequest != null) {
			// formating date
			LocalDateTime dateTime = LocalDateTime.parse(request.getFirstName(), DateTimeFormatter.ISO_DATE_TIME);
			String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			EmailTemplate emailTemplate = this.iEmailTemplateServices.getEmailTemplateById(1L);
			// send mail to the user for notification
			String to = "briki.ahmed21@gmail.com";
			String subject = emailTemplate.getSubject();
			String body = emailTemplate.getBody();
			emailService.sendEmail(to, subject, body);

			return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create");
		}
	}

	@PutMapping("/update-request/{id}")
	public ResponseEntity<Request> UpdateRequest (@RequestBody Request request, @PathVariable("id") Long id) {

		Request updatedRequest = iRequestServices.UpdateRequest(request, id);
	    if (updatedRequest != null) {
	        return ResponseEntity.ok(updatedRequest);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@GetMapping("/get/{id}")
	public Request GetRequestById (@PathVariable("id") Long id) {
		return iRequestServices.getRequestbyId(id);
	}

	@GetMapping("/all")
	public List<Request> GetAllRequest() {
		return iRequestServices.GetAllRequest();
	}

	@GetMapping("/archived")
	public List<Request> GetAllArchivedRequest() {
		return iRequestServices.GetAllArchivedRequest();
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable("id") Long id) {
        iRequestServices.RemoveRequest(id);
        return ResponseEntity.ok("Request with ID " + id + " deleted successfully.");
    }

	@GetMapping("/status")
	public ResponseEntity<Map<String, Integer>> getRequestStatusCount() {
		Map<String, Integer> statusCounts = requestServices.getRequestStatusCount();
		return ResponseEntity.ok(statusCounts);
	}

	@GetMapping("/creation-date")
	public ResponseEntity<Map<String, Map<String, Integer>>> getRequestStatusCountByDateAndType() {
		Map<String, Map<String, Integer>> DateCounts = requestServices.getRequestStatusCountByDateAndType();
		return ResponseEntity.ok(DateCounts);
	}

	@PutMapping("/refuse/{requestId}")
	public ResponseEntity<String> refuseRequest(@RequestBody Request request, @PathVariable("requestId") Long requestId) {

		// 2. Update the status of the request to "Refused"
		request.setStatus("refused");

		// 3. Archive the request (perform any necessary operations to mark it as archived)
		request.setArchived(true);
		request.setReasonOfRefuse(request.getReasonOfRefuse());
		// 4. Save the updated request in the database
		iRequestServices.UpdateRequest(request, requestId);

		EmailTemplate emailTemplate = this.iEmailTemplateServices.getEmailTemplateById(2L);
		// send mail to the user for notification
		String to = "briki.ahmed21@gmail.com";
		String subject = emailTemplate.getSubject();
		String body = emailTemplate.getBody();
		emailService.sendEmail(to, subject, body);

		return ResponseEntity.ok("Request refused and archived successfully.");
	}

	@PutMapping("/archive/{requestId}")
	public ResponseEntity<String> archiveRequest(@RequestBody Request request, @PathVariable("requestId") Long requestId) {

		// 3. Archive the request (perform any necessary operations to mark it as archived)
		request.setArchived(true);
		// 4. Save the updated request in the database
		iRequestServices.UpdateRequest(request, requestId);

		return ResponseEntity.ok("Request archived successfully.");
	}

	@GetMapping("/export")
	public ResponseEntity<byte[]> exportRequests(
			@RequestParam(name = "typeRequest") String typeRequest,
			@RequestParam(name = "creationDate") String date,
			@RequestParam(name = "status") String status
			) {
		// Implement logic to fetch requests based on the specified parameters

		// Assuming you have a service class called RequestService
		List<Request> requests = requestServices.searchRequests(typeRequest, date, status);
		System.out.println(requests);
		// Generate Excel file using a library like Apache POI
		byte[] excelFileBytes = requestServices.generateExcelFile((List<Request>) requests);

		// Set response headers for Excel file
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "requests.xlsx");

		// Return the Excel file as a byte array in the response
		return ResponseEntity.ok()
				.headers(headers)
				.body(excelFileBytes);
	}

}
