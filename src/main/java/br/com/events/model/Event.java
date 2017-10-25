package br.com.events.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Event {
	
	@Id
	@SequenceGenerator(name = "EVENT_ID", sequenceName = "EVENT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ID")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="user_all_id")
	private User creator;
	
	private String description;
	
	private Date event_start;
	
	private Date event_end;
	
	private boolean active;
	
	@OneToMany(mappedBy="event",fetch = FetchType.LAZY)
	private List<Invite> invites;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEvent_start() {
		return event_start;
	}

	public void setEvent_start(Date event_start) {
		this.event_start = event_start;
	}

	public Date getEvent_end() {
		return event_end;
	}

	public void setEvent_end(Date event_end) {
		this.event_end = event_end;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<Invite> getInvites() {
		return invites;
	}

	public void setInvites(List<Invite> invites) {
		this.invites = invites;
	}

}
