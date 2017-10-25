package br.com.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.model.Event;
import br.com.events.service.EventService;

@RestController
public class EventEndpoint {
	
	private EventService eventService;
	
	@Autowired
	public EventEndpoint(EventService eventService) {
		this.eventService = eventService;
	}
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event", method=RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
    	return new ResponseEntity<Event>(eventService.createEvent(event),HttpStatus.OK);
    }
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event", method=RequestMethod.DELETE)
    public void removeEvent(@RequestBody Event event) {
		eventService.removeEvent(event);		
    }
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event", method=RequestMethod.PUT)
    public ResponseEntity<Event> editEvent(@RequestBody Event event) {
    	return new ResponseEntity<Event>(eventService.editEvent(event),HttpStatus.OK);
    }
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event/user/{userId}", method=RequestMethod.GET)
    public ResponseEntity<Page<Event>> listUserEvents(@PathVariable(name="userId")Long userId,@RequestParam(name="page")Integer page,@RequestParam(name="size")Integer size) {
    	return new ResponseEntity<Page<Event>>(eventService.listEvents(page, size, userId),HttpStatus.OK);
    }
	
	@Secured("ROLE_USER")
    @RequestMapping(value = "/event", method=RequestMethod.GET)
    public ResponseEntity<Page<Event>> listAllEvents(@RequestParam(name="page")Integer page,@RequestParam(name="size")Integer size) {
    	return new ResponseEntity<Page<Event>>(eventService.listEvents(page, size),HttpStatus.OK);
    }

}
