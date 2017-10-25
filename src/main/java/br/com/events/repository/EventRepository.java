package br.com.events.repository;

import java.util.Date;

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
	
	@Query("FROM Event e WHERE e.active = TRUE")
	public Page<Event> findAllWhereActiveTrue(Pageable pageable);
	
	@Query("FROM Event e WHERE e.active = TRUE AND e.creator.id=:id")
	public Page<Event> findAllWhereActiveTrueByUser(@Param(value = "id") Long id,Pageable pageable);
	
	@Query("FROM Event e WHERE (e.event_start BETWEEN :start AND :end) OR (e.event_end BETWEEN :start AND :end)")
	public Event findEventBetweenDate(@Param(value = "start") Date start,@Param(value = "end") Date end);
	
	@Modifying
	@Query("UPDATE Event e SET e.active = FALSE WHERE e.id=:id")
	public void removeEvent(@Param(value = "id") Long id);
	
	@Modifying
	@Query("UPDATE Event e SET e.active = TRUE WHERE e.id=:id")
	public void reactiveEvent(@Param(value = "id") Long id);
	


}
