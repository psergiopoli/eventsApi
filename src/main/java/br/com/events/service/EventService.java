package br.com.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.events.model.Event;
import br.com.events.repository.EventRepository;

@Service
public class EventService {
	
	private EventRepository eventRepository;
	
	@Autowired
	public EventService(EventRepository eventRepository){
		this.eventRepository = eventRepository;
	}
	
	public Event createEvent(Event event){
		event.setActive(true);
		eventRepository.save(event);
		return event;
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
	
	public Page<Event> listEvents(Integer page, Integer size, Long userId){
		Pageable pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrueByUser(userId,pr);		
	}
	
	public Page<Event> listEvents(Integer page, Integer size){
		PageRequest pr = new PageRequest(page, size,Direction.ASC,"event_end");
		return eventRepository.findAllWhereActiveTrue(pr);		
	}
}
