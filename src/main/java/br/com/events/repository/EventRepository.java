package br.com.events.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.events.model.Event;

@Transactional
@Repository
public interface EventRepository extends CrudRepository<Event, Long>{
	
	public static final String BETWEEN_DATE_OUT_EVENT = "(:start BETWEEN e.event_start AND e.event_end) OR (:end BETWEEN e.event_start AND e.event_end)";
	public static final String BETWEEN_DATE_IN_EVENT = "(e.event_start BETWEEN :start AND :end) OR (e.event_end BETWEEN :start AND :end)";
	
	public static final String BETWEEN_DATE_OUT_INVITE = "(:start BETWEEN i.event.event_start AND i.event.event_end) OR (:end BETWEEN i.event.event_start AND i.event.event_end)";
	public static final String BETWEEN_DATE_IN_INVITE = "(i.event.event_start BETWEEN :start AND :end) OR (i.event.event_end BETWEEN :start AND :end)";
	
	@Query("FROM Event e WHERE e.active = TRUE")
	public Page<Event> findAllWhereActiveTrue(Pageable pageable);
	
	@Query("FROM Event e WHERE e.active = TRUE AND e.creator.id=:id")
	public Page<Event> findAllWhereActiveTrueByUser(@Param(value = "id") Long id,Pageable pageable);
	
	@Query("FROM Event e WHERE e.active = TRUE AND e.creator.id=:id")
	public List<Event> findAllWhereActiveTrueByUser(@Param(value = "id") Long id);
	
	@Query("FROM Event e WHERE ("+BETWEEN_DATE_OUT_EVENT+" OR "+BETWEEN_DATE_IN_EVENT+") AND e.active=TRUE AND e.creator.id=:userid")
	public List<Event> findEventBetweenDate(@Param(value = "start") Date start,@Param(value = "end") Date end,@Param(value = "userid") Long userid);
	
	@Query("FROM Event e LEFT JOIN e.invites i WHERE (i.event.active=TRUE AND ("+BETWEEN_DATE_OUT_INVITE+" OR "+ BETWEEN_DATE_IN_INVITE+") AND i.invited.id=:userid AND i.accepted=TRUE)")
	public List<Event> findEventBetweenDateInvited(@Param(value = "start") Date start,@Param(value = "end") Date end,@Param(value = "userid") Long userid);
	
	@Modifying
	@Query("UPDATE Event e SET e.active = FALSE WHERE e.id=:id")
	public void removeEvent(@Param(value = "id") Long id);
	
	@Modifying
	@Query("UPDATE Event e SET e.active = TRUE WHERE e.id=:id")
	public void reactiveEvent(@Param(value = "id") Long id);
}
