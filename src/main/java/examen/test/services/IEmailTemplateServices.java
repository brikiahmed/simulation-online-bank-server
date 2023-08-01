package examen.test.services;

import java.util.List;

import examen.test.entities.EmailTemplate;

public interface IEmailTemplateServices {
    EmailTemplate updateEmailTemplate(EmailTemplate newEmailTemplate, Long id);

    EmailTemplate getEmailTemplateById(Long id);

    EmailTemplate getEmailTemplateBySubject(String subject);
}
