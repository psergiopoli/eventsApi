package br.com.events.model;


import javax.persistence.*;

@Entity
public class Role {
	
	@Id
	@SequenceGenerator(name = "ROLE_ID", sequenceName = "ROLE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID")
    private Long id;

    private String roleName;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}