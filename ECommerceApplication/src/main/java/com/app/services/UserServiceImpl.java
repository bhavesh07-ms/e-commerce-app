package com.app.services;

import com.app.entites.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.UserDTO;
import com.app.payloads.UserResponse;
import com.app.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if(user.isPresent()) {
            throw new BadCredentialsException("User with email already exits "+ userDTO.getEmail());
        }

        User toBeCreatedUser = modelMapper.map(userDTO, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public String deleteUser(Long userId) {
        return "";
    }

    @Override
    public User getUserById2(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id "+ userId +
                " not found"));
    }
}
