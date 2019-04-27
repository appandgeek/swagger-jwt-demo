package com.appNgeek.swaggerjwtdemo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.appNgeek.swaggerjwtdemo.exception.AuthException;

public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtTokenService jwtTokenService;

	public JwtTokenFilter(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtTokenService.resolveToken(httpServletRequest);
		try {
			if (token != null && jwtTokenService.validateToken(token)) {
				Authentication auth = jwtTokenService.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (AuthException ex) {
			SecurityContextHolder.clearContext();
			httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
			return;
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}
