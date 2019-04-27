package com.appNgeek.swaggerjwtdemo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.appNgeek.swaggerjwtdemo.exception.AuthException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenService {

	// NOTE : this has to come from app properties file
	private static String SECRET_KEY = "very-difficult-to-crack";

	// NOTE : this has to come from app properties file
	private static Integer validityInMinutes = 60; // 1h

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}

	public String createToken(String email) {

		Claims claims = Jwts.claims().setSubject(email);

		Instant now = new Date().toInstant();
		Date validity = Date.from(now.plus(validityInMinutes, ChronoUnit.MINUTES));

		return Jwts.builder().setClaims(claims).setIssuedAt(Date.from(now)).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPrincipalFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUserPrincipalFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) throws AuthException {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new AuthException("Expired or invalid JWT token");
		}
	}

}
