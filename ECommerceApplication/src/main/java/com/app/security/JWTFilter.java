package com.app.security;

import java.io.IOException;

import com.app.services.JWTService;
import com.app.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import com.app.entites.User;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Service
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private JWTService jwtService;

	private UserDetailsServiceImpl userService;

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = requestTokenHeader.split("Bearer ")[1];
			Long userId = jwtService.getUserIdFromToken(token);

			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				User user = userService.getUserById(userId);
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			handlerExceptionResolver.resolveException(request, response, null, ex);
		}
		
		filterChain.doFilter(request, response);
	}
}