package br.com.events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.events.exception.CannotAcceptInviteException;
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
	
	
	public void sendInvite(Long eventId, Long userId,String emailInvited) throws InviteAlreadySendException{
		User user = userRepository.findOne(userId);
		User whoInvitedUser = userRepository.findByEmail(emailInvited);
		Invite invite = inviteRepository.findByEventAndInvitedAndWhoInvited(eventId, user.getId(), whoInvitedUser.getId());
		if(invite!=null){
			throw new InviteAlreadySendException("This invite is already send.");
		}else{
			Event eventToSendInvite = eventRepository.findOne(eventId);
			Invite newInvite = new Invite();
			newInvite.setInvited(user);
			newInvite.setEvent(eventToSendInvite);
			newInvite.setWhoInvited(whoInvitedUser);
			newInvite.setAccepted(false);
			inviteRepository.save(newInvite);
		}
	}
	
	public void acceptInvite(Long inviteId) throws CannotAcceptInviteException{
		Invite invite = inviteRepository.findOne(inviteId);
		if(invite.isAccepted()){
			throw new CannotAcceptInviteException("Invite is already accepted");
		}
		
		List<Invite> inviteTest = inviteRepository.findEventBetweenDate(invite.getEvent().getEvent_start(), invite.getEvent().getEvent_end(),invite.getInvited().getId());
		List<Event> eventTest = eventRepository.findEventBetweenDate(invite.getEvent().getEvent_start(), invite.getEvent().getEvent_end(),invite.getInvited().getId());
		if(inviteTest.size() > 0|| eventTest.size()>0 ){
			throw new CannotAcceptInviteException("This invite cannot be accepted because have another in same date");
		}else{
			invite.setAccepted(true);
			inviteRepository.save(invite);
		}		
	}
	
	public void unacceptInvite(Long inviteId){
		Invite invite = inviteRepository.findOne(inviteId);
		invite.setAccepted(false);
		inviteRepository.save(invite);	
	}
	
	public List<Invite> listInvites(String emailInvited){
		User whoInvitedUser = userRepository.findByEmail(emailInvited);
		List<Invite> invites = inviteRepository.findByInvited(whoInvitedUser);
		return invites;
	}
	
	public Invite getInviteById(Long inviteId){
		return inviteRepository.findOne(inviteId);
	}


	public void unacceptInvite(Long eventId, Long userIdInvited, String emailWhoInvite) {
		User userInvite = userRepository.findOne(userIdInvited);
		User userWhoInvite = userRepository.findByEmail(emailWhoInvite);
		Event event = eventRepository.findOne(userIdInvited);
		Invite invite = inviteRepository.findByInvitedAndWhoInvitedAndEvent(userInvite,userWhoInvite,event);
		invite.setAccepted(false);
		inviteRepository.save(invite);		
	}

}
