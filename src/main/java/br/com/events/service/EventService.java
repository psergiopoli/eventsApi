package br.com.events.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.events.exception.CannotAcceptInviteException;
import br.com.events.exception.InvalidEndOrStartDayOfEventException;
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
	
	public Event createEvent(Event event,String email) throws CannotAcceptInviteException, InvalidEndOrStartDayOfEventException{
		User user = userRepository.findByEmail(email);
		event.setCreator(user);
		if(event.getEvent_end().equals(event.getEvent_start())){
			throw new InvalidEndOrStartDayOfEventException("Events have the same end and start");
		}else if(event.getEvent_end().before(event.getEvent_start())){
			throw new InvalidEndOrStartDayOfEventException("The start of event is major of end");
		}		

		List<Event> eventTest = eventRepository.findEventBetweenDate(event.getEvent_start(), event.getEvent_end(),event.getCreator().getId());
		List<Event> eventTest2 = eventRepository.findEventBetweenDateInvited(event.getEvent_start(), event.getEvent_end(),event.getCreator().getId());
		if(eventTest.size() > 0 || eventTest2.size() > 0){
			throw new CannotAcceptInviteException("Cannot create event because have another in same time");
		}else{
			event.setActive(true);
			eventRepository.save(event);
			return event;
		}
	}
	
	public Event getEventById(Long eventId){
		return eventRepository.findOne(eventId);
	}
	
	public Event editEvent(Event event,String email) throws InvalidEndOrStartDayOfEventException, CannotAcceptInviteException{
		User user = userRepository.findByEmail(email);
		Event eventToExclude = eventRepository.findOne(event.getId());
		event.setCreator(user);
		if(event.getEvent_end().equals(event.getEvent_start())){
			throw new InvalidEndOrStartDayOfEventException("Events have the same end and start");
		}else if(event.getEvent_end().before(event.getEvent_start())){
			throw new InvalidEndOrStartDayOfEventException("The start of event is major of end");
		}
		List<Event> eventTest = eventRepository.findEventBetweenDate(event.getEvent_start(), event.getEvent_end(),event.getCreator().getId());
		List<Event> eventTest2 = eventRepository.findEventBetweenDateInvited(event.getEvent_start(), event.getEvent_end(),event.getCreator().getId());
		eventTest.remove(eventToExclude);
		eventTest2.remove(eventToExclude);
		if(eventTest.size() > 0 || eventTest2.size() > 0){
			throw new CannotAcceptInviteException("Cannot create event because have another in same time");
		}else{		
			Event eventToUpdate = eventRepository.findOne(event.getId());
			eventToUpdate.setDescription(event.getDescription());
			eventToUpdate.setEvent_end(event.getEvent_end());
			eventToUpdate.setEvent_start(event.getEvent_start());
			eventRepository.save(eventToUpdate);
			return eventToUpdate;
		}
	}
	
	public void removeEvent(Long id){
		eventRepository.removeEvent(id);		
	}
	
	public Page<Event> listEvents(Integer page, Integer size, String email){
		User user = userRepository.findByEmail(email);
		Pageable pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrueByUser(user.getId(),pr);		
	}
	
	public List<Event> listAllEvents(String email){
		User user = userRepository.findByEmail(email);
		List<Event> events = eventRepository.findAllWhereActiveTrueByUser(user.getId());
		List<Invite> invites = inviteRepository.findByInvited(user);
		List<Event> eventsInvites = new ArrayList<Event>();
		eventsInvites.addAll(events);
		
		for (Invite invite : invites) {
			Event tempEvent = invite.getEvent();
			tempEvent.setInvitedEvent(true);
			tempEvent.setInvitedEventAccepted(invite.isAccepted());
			tempEvent.setInvite(invite.getId());
			eventsInvites.add(tempEvent);
		}		
		Set<Event> eventsSet = new HashSet<Event>();
		eventsSet.addAll(eventsInvites);
		eventsInvites.clear();
		eventsInvites.addAll(eventsSet);
		Collections.sort(eventsInvites);
		return eventsInvites;
	}
	
	public Page<Event> listEvents(Integer page, Integer size){
		PageRequest pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrue(pr);		
	}
}
