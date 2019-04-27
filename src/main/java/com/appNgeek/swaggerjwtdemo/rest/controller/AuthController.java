package com.appNgeek.swaggerjwtdemo.rest.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appNgeek.swaggerjwtdemo.domain.User;
import com.appNgeek.swaggerjwtdemo.exception.AuthException;
import com.appNgeek.swaggerjwtdemo.repo.UserRepository;
import com.appNgeek.swaggerjwtdemo.security.JwtTokenService;
import com.appNgeek.swaggerjwtdemo.security.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public User create(@RequestBody User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User userFromDB =  userRepository.save(user);
		userFromDB.setPassword("**** Hidden ****");
		return userFromDB;
	}

	@PostMapping("/login")
	public String signin(@RequestBody LoginRequest request) throws AuthException {

		try {
			String email = request.getEmail();
			String password = request.getPassword();

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			String token = jwtTokenService.createToken(email);
			return "JWT token = Bearer " + token;

		} catch (AuthenticationException e) {
			throw new AuthException("Invalid username/password supplied");
		}
	}

}
