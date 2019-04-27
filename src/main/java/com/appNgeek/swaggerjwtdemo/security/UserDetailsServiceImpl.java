package com.appNgeek.swaggerjwtdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.appNgeek.swaggerjwtdemo.domain.User;
import com.appNgeek.swaggerjwtdemo.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
		final User user = userRepository.findByEmail(principal);

		if (user == null) {
			throw new UsernameNotFoundException("User with email '" + principal + "' not found");
		}

		return org.springframework.security.core.userdetails.User
				.withUsername(principal)
				.authorities(AuthorityUtils.NO_AUTHORITIES)
				.password(user.getPassword())
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false).build();
	}

}
