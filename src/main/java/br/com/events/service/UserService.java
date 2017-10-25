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

import br.com.events.model.User;
import br.com.events.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

	UserRepository pr;
	
	@Autowired
	public UserService(UserRepository pr) {
		this.pr = pr;
	}
	
	public void createUser(User user){
		pr.save(user);
	}
	
	public User findUserByEmail(String email){
		 User user = pr.findByEmail(email);
		 return user;		
	}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = pr.findByEmail(email);

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
