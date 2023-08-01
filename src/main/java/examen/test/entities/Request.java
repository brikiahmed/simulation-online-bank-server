package examen.test.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;

	private String reference;

	private String typeRequest ;
	
	private String firstName ;
	private String lastName ;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	private String birthDate ;
	private Long cin ;
	private String address ;
	private String comment ;

	private String status ;

	private Boolean archived;

	private String reasonOfRefuse;

	private String creationDate;

	private String updatedDate;

	private String nextService;

	private Boolean complaintRequestAllowed;

	@OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ConstraintRequest> constraintRequests = new ArrayList<>();

	public Request() {
		// TODO Auto-generated constructor stub
	}

	public Request(Long Id, String reference, String typeRequest, String firstName, String lastName, String email,
				   String birthDate, Long cin, String address, String comment, String status, Boolean archived,
				   String reasonOfRefuse, String updatedDate, String creationDate, String nextService,
				   Boolean complaintRequestAllowed, List<ConstraintRequest> constraintRequests) {
		id = Id;
		this.reference = reference;
		this.typeRequest = typeRequest;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
		this.cin = cin;
		this.address = address;
		this.comment = comment;
		this.status = status;
		this.archived = archived;
		this.reasonOfRefuse = reasonOfRefuse;
		this.updatedDate = updatedDate;
		this.creationDate = creationDate;
		this.nextService = nextService;
		this.complaintRequestAllowed = complaintRequestAllowed;
		this.constraintRequests = constraintRequests;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long Id) {
		id = Id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTypeRequest() {
		return typeRequest;
	}

	public void setTypeRequest(String type_request) {
		this.typeRequest = type_request;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first_name) {
		this.firstName = first_name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_name) {
		this.lastName = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birth_date) {
		this.birthDate = birth_date;
	}

	public Long getCin() {
		return cin;
	}

	public void setCin(Long cin) {
		this.cin = cin;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public String getReasonOfRefuse() {
		return reasonOfRefuse;
	}

	public void setReasonOfRefuse(String reasonOfRefuse) {
		this.reasonOfRefuse = reasonOfRefuse;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getNextService() {
		return nextService;
	}

	public void setNextService(String nextService) {
		this.nextService = nextService;
	}

	public Boolean getComplaintRequestAllowed() {
		return complaintRequestAllowed;
	}

	public void setComplaintRequestAllowed(Boolean complaintRequestAllowed) {
		this.complaintRequestAllowed = complaintRequestAllowed;
	}

	public void addConstraintRequest(ConstraintRequest constraintRequest) {
		constraintRequests.add(constraintRequest);
		constraintRequest.setRequest(this);
	}

	public void removeConstraintRequest(ConstraintRequest constraintRequest) {
		constraintRequests.remove(constraintRequest);
		constraintRequest.setRequest(null);
	}

}
