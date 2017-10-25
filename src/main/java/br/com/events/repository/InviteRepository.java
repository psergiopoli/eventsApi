package br.com.events.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.events.model.Invite;

public interface InviteRepository extends CrudRepository<Invite, Long>{
	
	@Query("FROM Invite i WHERE i.event.id=:event_id AND i.invited.id=:invited_id AND i.whoInvited=:whoInvited_id")
	public Invite findByEventAndInvitedAndWhoInvited(@Param(value = "event_id") Long event_id,@Param(value = "invited_id") Long invited_id,@Param(value = "whoInvited_id") Long whoInvited_id);
	
}