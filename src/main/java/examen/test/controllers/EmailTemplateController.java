package examen.test.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import examen.test.entities.EmailTemplate;
import examen.test.services.EmailService;
import examen.test.services.IEmailTemplateServices;
@CrossOrigin
@RestController
@RequestMapping("/email_template")
public class EmailTemplateController {

	@Autowired
	private IEmailTemplateServices emailTemplateService; // Use the interface

	@PutMapping("update-email/{id}")
	public ResponseEntity<EmailTemplate> updateEmailTemplate(@PathVariable Long id, @RequestBody EmailTemplate newEmailTemplate) {
		EmailTemplate updatedEmailTemplate = emailTemplateService.updateEmailTemplate(newEmailTemplate, id);
		if (updatedEmailTemplate != null) {
			return new ResponseEntity<>(updatedEmailTemplate, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/template/{id}")
	public ResponseEntity<EmailTemplate> getEmailTemplateById(@PathVariable Long id) {
		EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateById(id);
		if (emailTemplate != null) {
			return new ResponseEntity<>(emailTemplate, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/subject/{subject}")
	public ResponseEntity<EmailTemplate> getEmailTemplateBySubject(@PathVariable String subject) {
		EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateBySubject(subject);
		if (emailTemplate != null) {
			return new ResponseEntity<>(emailTemplate, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
