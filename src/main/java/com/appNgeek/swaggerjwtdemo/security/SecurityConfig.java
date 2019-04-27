package com.appNgeek.swaggerjwtdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenService jwtTokenService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow swagger and other end points to be accessed without authentication
		web.ignoring().antMatchers("/static/**")
					   .antMatchers("/v2/api-docs")
		               .antMatchers("/swagger-resources/**")
				       .antMatchers("/swagger-ui.html")
				       .antMatchers("/configuration/**")
				       .antMatchers("/webjars/**")
				       .antMatchers("/public")
				       .antMatchers("/resources/**")

				// H2 Database - allow access without auth
				.and().ignoring().antMatchers("/h2-console/**/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disable CSRF (cross site request forgery)
		http.csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/h2-console/**/**").permitAll()
				// Disallow everything else..
				.anyRequest().authenticated();

		http.apply(new JwtTokenFilterConfig(jwtTokenService));

	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

}
