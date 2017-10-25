package br.com.events.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.events.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
	
	public List<Role> findByRoleName(String roleName);

}
