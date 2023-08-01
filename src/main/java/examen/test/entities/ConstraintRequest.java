package examen.test.entities;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class ConstraintRequest  implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id ;

    private String constraintBody;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_request")
    private Request request;

    private String creationDate;

    public ConstraintRequest() {
        // TODO Auto-generated constructor stub
    }

    public ConstraintRequest(Long id, String constraintBody, Request request, String creationDate) {
        Id = id;
        this.constraintBody = constraintBody;
        this.request = request;
        this.creationDate = creationDate;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getConstraintBody() {
        return constraintBody;
    }

    public void setConstraintBody(String constraintBody) {
        this.constraintBody = constraintBody;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}
