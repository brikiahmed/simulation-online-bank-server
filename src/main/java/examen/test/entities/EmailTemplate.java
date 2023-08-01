package examen.test.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
public class EmailTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;

	@Column(unique = true) // Ensure uniqueness for the identifier
	private String identifier;

	private String subject;

	@Column(columnDefinition = "TEXT")
	private String body;
	
	

	public EmailTemplate(Long id, String identifier, String subject, String body) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.subject = subject;
		this.body = body;
	}
	
	

	public EmailTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	
	
	
	

}
