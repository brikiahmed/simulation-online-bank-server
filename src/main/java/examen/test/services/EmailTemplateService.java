package examen.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import examen.test.entities.EmailTemplate;
import examen.test.repositories.EmailTemplateRepository;

@Service
public class EmailTemplateService implements IEmailTemplateServices{

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	public EmailTemplate updateEmailTemplate(EmailTemplate newEmailTemplate, Long id) {
		EmailTemplate emailTemplate = emailTemplateRepository.findById(id).orElse(null);

		if (emailTemplate != null) {
			emailTemplate.setIdentifier(newEmailTemplate.getIdentifier());
			emailTemplate.setSubject(newEmailTemplate.getSubject());
			emailTemplate.setBody(newEmailTemplate.getBody());
			emailTemplateRepository.save(emailTemplate);
		}

		return emailTemplate;
	}

	public EmailTemplate getEmailTemplateById(Long id) {
		return emailTemplateRepository.findById(id).orElse(null);
	}

	public EmailTemplate getEmailTemplateBySubject(String subject) {
		return emailTemplateRepository.findBySubject(subject);
	}
}
