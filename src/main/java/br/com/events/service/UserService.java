package br.com.events.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.events.exception.UserAlreadyExistException;
import br.com.events.model.Event;
import br.com.events.model.Invite;
import br.com.events.model.Role;
import br.com.events.model.User;
import br.com.events.repository.EventRepository;
import br.com.events.repository.RoleRepository;
import br.com.events.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

	private UserRepository userRepository;
	
	private EventRepository eventRepository;
	
	private RoleRepository roleRepository;
	
	@Autowired
	public UserService(UserRepository userRepository,RoleRepository roleRepository,EventRepository eventRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.eventRepository = eventRepository;
	}
	
	public void createUser(User user) throws UserAlreadyExistException{
		User userTest = userRepository.findByEmail(user.getEmail());
		if(userTest!=null){
			throw new UserAlreadyExistException("E-mail already in use.");
		}
		List<Role> role = roleRepository.findByRoleName("ROLE_USER");
		user.setRoles(role);
		userRepository.save(user);
		
	}
	
	public User findUserByEmail(String email){
		 User user = userRepository.findByEmail(email);
		 return user;		
	}
	
	public List<User> getAllUsers(){
		List<User> users = userRepository.getAllUsers();
		return users;		
	}
	
	public List<User> getAllUsersNotInvitedForEvent(Long eventId,String email){
		User user = userRepository.findByEmail(email);
		List<User> users = userRepository.getAllUsers();
		Event event = eventRepository.findOne(eventId);
		List<User> userInvited = new ArrayList<User>();
		
		for (Invite invite : event.getInvites()) {
			userInvited.add(invite.getInvited());
		}
		users.removeAll(userInvited);
		users.remove(user);
		return users;		
	}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("The email %s doesn't exist", email));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        return userDetails;
    }
}
