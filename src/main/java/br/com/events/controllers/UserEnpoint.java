package br.com.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.events.exception.UserAlreadyExistException;
import br.com.events.model.User;
import br.com.events.service.UserService;
import br.com.events.util.ResponseUtil;

@RestController
public class UserEnpoint {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private UserService userService;

	@Autowired
	public UserEnpoint(BCryptPasswordEncoder bCryptPasswordEncoder,
			UserService userService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
	}

	@RequestMapping(value = "/sign-in", method = RequestMethod.POST)
	public ResponseEntity<ResponseUtil> signUp(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		try {
			userService.createUser(user);
		} catch (UserAlreadyExistException e) {
			new ResponseEntity<ResponseUtil>(new ResponseUtil(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseUtil>(new ResponseUtil("Signup success."), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(Authentication authentication) {
		User user = userService.findUserByEmail(authentication.getName());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

}
