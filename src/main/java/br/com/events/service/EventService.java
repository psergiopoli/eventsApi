package br.com.events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.events.exception.CannotAcceptInviteException;
import br.com.events.exception.InvalidEndOrStartDayOfEventException;
import br.com.events.model.Event;
import br.com.events.model.User;
import br.com.events.repository.EventRepository;
import br.com.events.repository.UserRepository;

@Service
public class EventService {
	
	private EventRepository eventRepository;
	
	private UserRepository userRepository;
	
	@Autowired
	public EventService(EventRepository eventRepository,UserRepository userRepository){
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
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
	
	public Event editEvent(Event event){
		Event eventToUpdate = eventRepository.findOne(event.getId());
		eventToUpdate.setDescription(event.getDescription());
		eventToUpdate.setEvent_end(event.getEvent_end());
		eventToUpdate.setEvent_start(event.getEvent_start());
		eventRepository.save(eventToUpdate);
		return eventToUpdate;
	}
	
	public void removeEvent(Event event){
		eventRepository.removeEvent(event.getId());		
	}
	
	public Page<Event> listEvents(Integer page, Integer size, String email){
		User user = userRepository.findByEmail(email);
		Pageable pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrueByUser(user.getId(),pr);		
	}
	
	public Page<Event> listAllEvents(Integer page, Integer size, String email){
		User user = userRepository.findByEmail(email);
		Pageable pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllCreatedAndInvited(user.getId(),pr);		
	}
	
	public Page<Event> listEvents(Integer page, Integer size){
		PageRequest pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrue(pr);		
	}
}
