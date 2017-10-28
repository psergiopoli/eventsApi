package br.com.events.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Event implements Comparable<Event>{
	
	@Id
	@SequenceGenerator(name = "EVENT_ID", sequenceName = "EVENT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ID")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="user_all_id")
	@JsonIgnoreProperties({"events","inviteSends","receivedInvites"})
	private User creator;
	
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date event_start;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date event_end;
	
	private boolean active;
	
	@OneToMany(mappedBy="event",fetch = FetchType.LAZY)
	private List<Invite> invites;	
	
	@Transient
	private boolean invitedEvent = false;
	
	@Transient
	private Long invite = null;

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

	public boolean isInvitedEvent() {
		return invitedEvent;
	}

	public void setInvitedEvent(boolean invitedEvent) {
		this.invitedEvent = invitedEvent;
	}

	@Override
	public int compareTo(Event o) {
		return getEvent_end().compareTo(o.getEvent_end());
		//return o.getEvent_end().compareTo(getEvent_end());
	}

	public Long getInvite() {
		return invite;
	}

	public void setInvite(Long invite) {
		this.invite = invite;
	}

}
