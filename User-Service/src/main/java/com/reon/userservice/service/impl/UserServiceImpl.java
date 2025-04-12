package com.reon.userservice.service.impl;

import com.reon.userservice.dto.LoginDTO;
import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.exception.EmailAlreadyExistsException;
import com.reon.userservice.exception.InvalidCredentialsException;
import com.reon.userservice.exception.UserNameAlreadyExistsException;
import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.mapper.UserMapper;
import com.reon.userservice.model.Role;
import com.reon.userservice.model.User;
import com.reon.userservice.repository.UserRepository;
import com.reon.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.reon.userservice.mapper.UserMapper.mapToUser;
import static com.reon.userservice.mapper.UserMapper.responseToUser;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, EmailVerificationService emailVerificationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @CacheEvict(value = "userCache", key = "'userInfo'")
    public UserResponseDTO registerNewUser(UserRegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())){
            throw new EmailAlreadyExistsException("A user with this email already exists: " + registerDTO.getEmail());
        }
        if (userRepository.existsByUsername(registerDTO.getUsername())){
            throw new UserNameAlreadyExistsException("A user with this username already exists: " + registerDTO.getUsername());
        }
        logger.info("Creating a user with email: " + registerDTO.getEmail());
        User user = mapToUser(registerDTO);

        // Generate a random and unique id.
        String id = UUID.randomUUID().toString();
        user.setId(id);

        // encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role to user
        user.setRoles(EnumSet.of(Role.USER));
        user.setCreatedAt(LocalDateTime.now());

        // Set emailVerified and accountEnabled to false at registration
//        user.setAccountEnabled(false);
//        user.setEmailVerified(false);

        User saveUser = userRepository.save(user);
        logger.info("User saved.");

        // Once user is saved to database send the User a verification email.
//        emailVerificationService.createAndSendVerificationToken(user);
        return UserMapper.responseToUser(saveUser);
    }

    @Override
    @Caching(
            put = @CachePut(value = "users", key = "#userId"),
            evict = @CacheEvict(value = "userCache", key = "'userInfo'")
    )
    public UserResponseDTO updateExistingUser(String userId, UserRegisterDTO updateDTO) {
        logger.info("Updating an user with id: " + userId);
        User exisitingUser = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + userId));
        UserMapper.applyUpdates(exisitingUser, updateDTO);
        exisitingUser.setUpdatedOn(LocalDateTime.now());
        User savedUser = userRepository.save(exisitingUser);
        logger.info("User updated successfully [id]: " + userId);
        return UserMapper.responseToUser(savedUser);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#userId"),
                    @CacheEvict(value = "userCache", key = "'userInfo'")
            }
    )
    public void deleteUser(String userId) {
        logger.info("Deleting user with id: " + userId);
        userRepository.deleteById(userId);
    }

    // Only for authentication without JWT.
    @Override
    public UserResponseDTO accessUser(LoginDTO loginDTO) {
        logger.info("Fetching user data with email: " + loginDTO.getEmail() + " login request.");
        User searchUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email: " + loginDTO.getEmail() + " not found!")
        );

        if (!passwordEncoder.matches(loginDTO.getPassword(), searchUser.getPassword())){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        return responseToUser(searchUser);
    }
}
