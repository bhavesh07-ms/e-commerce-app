package com.app.controllers;

import java.util.Map;

import com.app.payloads.LoginDTO;
import com.app.repositories.UserRepo;
import com.app.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exceptions.UserNotFoundException;
import com.app.payloads.UserDTO;
import com.app.security.JWTUtil;
import com.app.services.UserService;
import com.app.payloads.LoginResponseDto;
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

	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerHandler(@Valid @RequestBody UserDTO user) throws UserNotFoundException {

		UserDTO userDTO = userService.registerUser(user);

		return new ResponseEntity<>(userDTO,
				HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> loginHandler(@Valid @RequestBody LoginDTO loginDto,HttpServletRequest request,
											HttpServletResponse response) {

		LoginResponseDto loginResponseDto = authService.login(loginDto);

		Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
		cookie.setHttpOnly(true);
		//cookie.setSecure("production".equals(deployEnv));
		response.addCookie(cookie);

		return new ResponseEntity<>(loginResponseDto,
		HttpStatus.OK);
	}
}