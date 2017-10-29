package br.com.events.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.exception.CannotAcceptInviteException;
import br.com.events.exception.InviteAlreadySendException;
import br.com.events.model.Invite;
import br.com.events.service.InviteService;
import br.com.events.util.ResponseUtil;

@Secured("ROLE_USER")
@RestController
public class InviteEndpoint {
	
	private InviteService inviteService;
	
	public InviteEndpoint(InviteService inviteService) {
		this.inviteService = inviteService;
	}
	
    @RequestMapping(value = "/invite", method=RequestMethod.PUT)
    public ResponseEntity<ResponseUtil> createInvite(@RequestParam(name="event")Long eventId,@RequestParam(name="user")Long userId,Authentication authentication) {
    	try {
			inviteService.sendInvite(eventId, userId, authentication.getName());
		} catch (InviteAlreadySendException e) {
			return new ResponseEntity<ResponseUtil>(new ResponseUtil(e.getMessage()),HttpStatus.CONFLICT);
		}
    	return new ResponseEntity<ResponseUtil>(new ResponseUtil("invite send"),HttpStatus.OK);
    }
    
    @RequestMapping(value = "/invite/cancel", method=RequestMethod.DELETE)
    public ResponseEntity<ResponseUtil> uninvite(@RequestParam(name="event")Long eventId,@RequestParam(name="user")Long userId,Authentication authentication) {
		inviteService.cancelInvite(eventId, userId, authentication.getName());
		return new ResponseEntity<ResponseUtil>(new ResponseUtil("invite canceled"),HttpStatus.OK);
    }
    
    @RequestMapping(value = "/invite/{inviteId}", method=RequestMethod.GET)
    public ResponseEntity<Invite> getInvite(@PathVariable(name="inviteId")Long inviteId,Authentication authentication) {
    	Invite invite = inviteService.getInviteById(inviteId);
    	return new ResponseEntity<Invite>(invite,HttpStatus.OK);
    }
	
    @RequestMapping(value = "/invite/{inviteId}", method=RequestMethod.PATCH)
    public ResponseEntity<ResponseUtil> acceptInvite(@PathVariable(name="inviteId")Long inviteId) {
		try {
			inviteService.acceptInvite(inviteId);
		} catch (CannotAcceptInviteException e) {
			return new ResponseEntity<ResponseUtil>(new ResponseUtil(e.getMessage()),HttpStatus.CONFLICT);
		}
    	return new ResponseEntity<ResponseUtil>(new ResponseUtil("invite accepted"),HttpStatus.OK);
    }
	
    @RequestMapping(value = "/invite/{inviteId}", method=RequestMethod.DELETE)
    public ResponseEntity<ResponseUtil> unacceptInvite(@PathVariable(name="inviteId")Long inviteId,Authentication authentication) {
		inviteService.unacceptInvite(inviteId);
		return new ResponseEntity<ResponseUtil>(new ResponseUtil("invite unaccepted"),HttpStatus.OK);
    }
    
    @RequestMapping(value = "/invite", method=RequestMethod.DELETE)
    public ResponseEntity<ResponseUtil> unacceptInvite(@RequestParam(name="event")Long eventId,@RequestParam(name="user")Long userId,Authentication authentication) {
		inviteService.unacceptInvite(eventId,userId,authentication.getName());
		return new ResponseEntity<ResponseUtil>(new ResponseUtil("invite unaccepted"),HttpStatus.OK);
    }
	
    @RequestMapping(value = "/invite/list", method=RequestMethod.GET)
    public ResponseEntity<List<Invite>> listInvitesByUser(Authentication authentication) {
		List<Invite> invites = inviteService.listInvites(authentication.getName());
    	return new ResponseEntity<List<Invite>>(invites,HttpStatus.OK);
    }

}
