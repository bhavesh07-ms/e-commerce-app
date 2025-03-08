package com.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.config.UserInfoConfig;
import com.app.entites.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.repositories.UserRepo;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		
		return user.map(UserInfoConfig::new).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
	}


	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id "+ userId +
				" not found"));
	}
}