package com.app.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.app.entites.User;
import com.app.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exceptions.UserNotFoundException;
import com.app.payloads.LoginCredentials;
import com.app.payloads.UserDTO;
import com.app.security.JWTUtil;
import com.app.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@RequiredArgsConstructor
public class AuthController {


	private UserService userService;

	private JWTUtil jwtUtil;

	private AuthenticationManager authenticationManager;

	private PasswordEncoder passwordEncoder;

	private UserRepo userRepo;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerHandler(@Valid @RequestBody UserDTO user) throws UserNotFoundException {



		UserDTO userDTO = userService.registerUser(user);

		//String token = jwtUtil.generateToken(userDTO.getEmail());

		return new ResponseEntity<UserDTO>(userDTO,
				HttpStatus.OK);
	}

	@PostMapping("/login")
	public Map<String, Object> loginHandler(@Valid @RequestBody LoginCredentials credentials) {

		UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
				credentials.getEmail(), credentials.getPassword());

		authenticationManager.authenticate(authCredentials);

		String token = jwtUtil.generateToken(credentials.getEmail());

		return Collections.singletonMap("jwt-token", token);
	}
}