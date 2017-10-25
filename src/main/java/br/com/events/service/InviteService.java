package br.com.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.events.exception.CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException;
import br.com.events.exception.InviteAlreadySendException;
import br.com.events.model.Event;
import br.com.events.model.Invite;
import br.com.events.model.User;
import br.com.events.repository.EventRepository;
import br.com.events.repository.InviteRepository;
import br.com.events.repository.UserRepository;

@Service
public class InviteService {
	
	private EventRepository eventRepository;

	private UserRepository userRepository;
	
	private InviteRepository inviteRepository;
	
	@Autowired
	public InviteService(EventRepository eventRepository,UserRepository userRepository,InviteRepository inviteRepository) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.inviteRepository = inviteRepository;
	}
	
	
	public void sendInvite(Long eventId, String invitedEmail,Long whoInvited) throws InviteAlreadySendException{
		User user = userRepository.findByEmail(invitedEmail);
		Invite invite = inviteRepository.findByEventAndInvitedAndWhoInvited(eventId, user.getId(), whoInvited);
		if(invite!=null){
			throw new InviteAlreadySendException("This invite is already send.");
		}else{
			User whoInvitedUser = userRepository.findOne(whoInvited);
			Event eventToSendInvite = eventRepository.findOne(eventId);
			Invite newInvite = new Invite();
			newInvite.setInvited(user);
			newInvite.setEvent(eventToSendInvite);
			newInvite.setWhoInvited(whoInvitedUser);
			newInvite.setAccepted(false);
			inviteRepository.save(newInvite);
		}
	}
	
	public void acceptInvite(Long inviteId, Long whoInvited) throws CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException{
		Invite invite = inviteRepository.findOne(inviteId);
		Event eventTest = eventRepository.findEventBetweenDate(invite.getEvent().getEvent_start(), invite.getEvent().getEvent_end());		
		if(eventTest!=null){
			throw new CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException("This invite cannot be accepted because have another in same date.Event_id:"+eventTest.getId());
		}else{
			invite.setAccepted(true);
			inviteRepository.save(invite);
		}		
	}

}
