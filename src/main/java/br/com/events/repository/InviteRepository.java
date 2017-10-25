package br.com.events.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.events.model.Invite;
import br.com.events.model.User;

public interface InviteRepository extends CrudRepository<Invite, Long>{
	
	@Query("FROM Invite i WHERE i.event.id=:event_id AND i.invited.id=:invited_id AND i.whoInvited.id=:whoInvited_id")
	public Invite findByEventAndInvitedAndWhoInvited(@Param(value = "event_id") Long event_id,@Param(value = "invited_id") Long invited_id,@Param(value = "whoInvited_id") Long whoInvited_id);
	
	@Query("FROM Invite i WHERE ((i.event.event_start BETWEEN :start AND :end) OR (i.event.event_end BETWEEN :start AND :end)) AND i.invited.id=:userid AND i.accepted = TRUE")
	public List<Invite> findEventBetweenDate(@Param(value = "start") Date start,@Param(value = "end") Date end,@Param(value = "userid") Long userIdInvited);
	
	public List<Invite> findByInvited(User user);
	
}