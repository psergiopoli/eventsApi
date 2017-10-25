package br.com.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
public class EventService {
	
	private EventRepository eventRepository;

	private UserRepository userRepository;
	
	private InviteRepository inviteRepository;
	
	@Autowired
	public EventService(EventRepository eventRepository,UserRepository userRepository,InviteRepository inviteRepository){
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.inviteRepository = inviteRepository;
	}
	
	public void createEvent(Event event){
		event.setActive(true);
		eventRepository.save(event);
	}
	
	public void editEvent(Event event){
		Event eventToUpdate = eventRepository.findOne(event.getId());
		eventToUpdate.setActive(event.isActive());
		eventToUpdate.setDescription(event.getDescription());
		eventToUpdate.setEvent_end(event.getEvent_end());
		eventToUpdate.setEvent_start(event.getEvent_start());
		eventRepository.save(eventToUpdate);
	}
	
	public void removeEvent(Event event){
		eventRepository.removeEvent(event.getId());		
	}
	
	public Page<Event> listEvents(Integer page, Integer size, Long userId){
		Pageable pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrueByUser(userId,pr);		
	}
	
	public Page<Event> listEvents(Integer page, Integer size){
		PageRequest pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrue(pr);		
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
	
	public Page<Event> listUsersSendedInviteAndNotAccepted(Long eventId,Integer page, Integer size){
		return null;
	}
	
	public Page<Event> listUsersSendedInviteAndAccepted(Long eventId,Integer page, Integer size){
		return null;
	}
}
