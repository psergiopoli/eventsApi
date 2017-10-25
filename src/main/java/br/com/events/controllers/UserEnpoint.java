package br.com.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.model.User;
import br.com.events.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserEnpoint {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserService userService;

	@Autowired
	public UserEnpoint(BCryptPasswordEncoder bCryptPasswordEncoder,UserService userService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
	}
	
    @RequestMapping(value = "/sign-in", method=RequestMethod.POST)
    public void signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.createUser(user);
    }

}
