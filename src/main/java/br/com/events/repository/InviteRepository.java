package br.com.events.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.events.model.Event;
import br.com.events.model.Invite;
import br.com.events.model.User;

public interface InviteRepository extends CrudRepository<Invite, Long>{
	
	public static final String BETWEEN_DATE_OUT_INVITE = "(:start BETWEEN i.event.event_start AND i.event.event_end) OR (:end BETWEEN i.event.event_start AND i.event.event_end)";
	public static final String BETWEEN_DATE_IN_INVITE = "(i.event.event_start BETWEEN :start AND :end) OR (i.event.event_end BETWEEN :start AND :end)";
	
	@Query("FROM Invite i WHERE i.event.id=:event_id AND i.invited.id=:invited_id AND i.whoInvited.id=:whoInvited_id")
	public Invite findByEventAndInvitedAndWhoInvited(@Param(value = "event_id") Long event_id,@Param(value = "invited_id") Long invited_id,@Param(value = "whoInvited_id") Long whoInvited_id);
	
	@Query("FROM Invite i WHERE ("+BETWEEN_DATE_OUT_INVITE+" OR "+BETWEEN_DATE_IN_INVITE+") AND i.invited.id=:userid AND i.accepted = TRUE AND i.event.active=TRUE")
	public List<Invite> findEventBetweenDate(@Param(value = "start") Date start,@Param(value = "end") Date end,@Param(value = "userid") Long userIdInvited);
	
	public List<Invite> findByInvited(User user);
	
	@Query("FROM Invite i WHERE i.invited=:invited AND i.whoInvited=:whoinvited AND i.event=:event")
	public Invite findByInvitedAndWhoInvitedAndEvent(@Param(value = "invited")User invited,@Param(value = "whoinvited") User whoInvited,@Param(value = "event") Event event);
	
}