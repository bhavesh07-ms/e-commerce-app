package com.app.config;

import com.app.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.security.JWTFilter;


import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JWTFilter jwtFilter;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers(AppConstants.PUBLIC_URLS).permitAll()
			.requestMatchers("/api/products/**").hasAnyRole("SHOPKEEPER", "ADMIN")
			.requestMatchers("/api/orders/**").hasAnyRole("CUSTOMER","ADMIN","SHOPKEEPER","DELIVERY_AGENT")
			.requestMatchers("/api/delivery/**").hasRole("DELIVERY_AGENT")
			.requestMatchers("/api/admin/**").hasRole("ADMIN")
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(
					(request, response, authException) -> 
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.authenticationProvider(daoAuthenticationProvider());
		
		DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
		
		return defaultSecurityFilterChain;
		/*http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(AbstractHttpConfigurer::disable);
		return http.build();*/
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsServiceImpl);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}