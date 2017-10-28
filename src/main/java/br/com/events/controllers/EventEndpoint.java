package br.com.events.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.exception.CannotAcceptInviteException;
import br.com.events.exception.InvalidEndOrStartDayOfEventException;
import br.com.events.model.Event;
import br.com.events.service.EventService;

@Secured("ROLE_USER")
@RestController
public class EventEndpoint {
	
	private EventService eventService;
	
	@Autowired
	public EventEndpoint(EventService eventService) {
		this.eventService = eventService;
	}
	
    @RequestMapping(value = "/event", method=RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event,Authentication authentication) {
    	try {
			return new ResponseEntity<Event>(eventService.createEvent(event,authentication.getName()),HttpStatus.OK);
		} catch (CannotAcceptInviteException e) {
			return new ResponseEntity<Event>(HttpStatus.CONFLICT);
		} catch (InvalidEndOrStartDayOfEventException e) {
			return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
		}
    }
    
    @RequestMapping(value = "/event/{eventId}", method=RequestMethod.GET)
    public ResponseEntity<Event> getEvent(@PathVariable(name="eventId") Long eventId) {
    	Event event = eventService.getEventById(eventId);
    	return new ResponseEntity<Event>(event,HttpStatus.OK);		
    }
	
    @RequestMapping(value = "/event/{eventId}", method=RequestMethod.DELETE)
    public void removeEvent(@PathVariable(name="eventId") Long eventId) {
		eventService.removeEvent(eventId);		
    }

    @RequestMapping(value = "/event", method=RequestMethod.PUT)
    public ResponseEntity<Event> editEvent(@RequestBody Event event,Authentication authentication) {
    	try {
			return new ResponseEntity<Event>(eventService.editEvent(event,authentication.getName()),HttpStatus.OK);
		} catch (InvalidEndOrStartDayOfEventException e) {
			return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
		} catch (CannotAcceptInviteException e) {
			return new ResponseEntity<Event>(HttpStatus.CONFLICT);
		}
    }
	
    @RequestMapping(value = "/event/user", method=RequestMethod.GET)
    public ResponseEntity<Page<Event>> listUserEvents(@RequestParam(name="page")Integer page,@RequestParam(name="size")Integer size,Authentication authentication) {
		Page<Event> pr = eventService.listEvents(page, size, authentication.getName());
    	return new ResponseEntity<Page<Event>>(pr,HttpStatus.OK);
    }
	
    @RequestMapping(value = "/event", method=RequestMethod.GET)
    public ResponseEntity<Page<Event>> listAllEvents(@RequestParam(name="page")Integer page,@RequestParam(name="size")Integer size) {
    	return new ResponseEntity<Page<Event>>(eventService.listEvents(page, size),HttpStatus.OK);
    }
	
    @RequestMapping(value = "/event/all", method=RequestMethod.GET)
    public ResponseEntity<List<?>> listAllEventsInvitedAndCreated(Authentication authentication) {
		List<?> eventsInvites = eventService.listAllEvents(authentication.getName());
    	return new ResponseEntity<List<?>>(eventsInvites,HttpStatus.OK);
    }

}
