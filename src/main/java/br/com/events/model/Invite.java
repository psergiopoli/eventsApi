package br.com.events.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Invite {
	
	@Id
	@SequenceGenerator(name = "INVITE_ID", sequenceName = "INVITE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVITE_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="event_id")
	@JsonIgnoreProperties({"creator","invites"})
	private Event event;
	
	@ManyToOne
	@JoinColumn(name="invited_id")
	@JsonIgnoreProperties({"events","inviteSends","receivedInvites"})
	private User invited;
	
	@ManyToOne
	@JoinColumn(name="whoInvited_id")
	@JsonIgnoreProperties({"events","inviteSends","receivedInvites"})
	private User whoInvited;
	
	private boolean accepted;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getInvited() {
		return invited;
	}

	public void setInvited(User invited) {
		this.invited = invited;
	}

	public User getWhoInvited() {
		return whoInvited;
	}

	public void setWhoInvited(User whoInvited) {
		this.whoInvited = whoInvited;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
