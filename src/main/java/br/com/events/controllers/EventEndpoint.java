package br.com.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.model.Event;
import br.com.events.repository.EventRepository;

@RestController
@CrossOrigin(origins = "*")
public class EventEndpoint {
	
	private EventRepository eventRepository;
	
	@Autowired
	public EventEndpoint(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event", method=RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
		eventRepository.save(event);    	
    	return new ResponseEntity<Event>(event,HttpStatus.OK);
    }

}
