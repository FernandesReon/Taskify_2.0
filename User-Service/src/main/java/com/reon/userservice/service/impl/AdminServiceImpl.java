package com.reon.userservice.service.impl;

import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.exception.EmailAlreadyExistsException;
import com.reon.userservice.exception.UserNameAlreadyExistsException;
import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.mapper.UserMapper;
import com.reon.userservice.model.Role;
import com.reon.userservice.model.User;
import com.reon.userservice.model.task_ms.TaskResponse;
import com.reon.userservice.repository.UserRepository;
import com.reon.userservice.service.AdminService;
import com.reon.userservice.service.TaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;
    private final TaskClient taskClient;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository, TaskClient taskClient, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskClient = taskClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Cacheable(value = "userCache", key = "'userInfo'")
    public List<UserResponseDTO> fetchAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(
                user -> {
                    UserResponseDTO response = UserMapper.responseToUser(user);
                    List<TaskResponse> tasks = taskClient.getTasksByUserId(user.getId());
                    response.setTasks(tasks);
                    return response;
                }
        ).collect(Collectors.toList());
    }
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO fetchById(String id) {
        logger.info("Fetching user with id: " + id);
        User optionalUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found!")
        );
        UserResponseDTO response = UserMapper.responseToUser(optionalUser);
        List<TaskResponse> tasks = taskClient.getTasksByUserId(id);
        response.setTasks(tasks);
        return response;
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponseDTO fetchByEmail(String email) {
        logger.info("Fetching user with email: " + email);
        User optionalUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " not found!")
        );
        UserResponseDTO response = UserMapper.responseToUser(optionalUser);
        List<TaskResponse> tasks = taskClient.getTasksByUserId(optionalUser.getId());
        response.setTasks(tasks);
        return response;
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserResponseDTO fetchByUsername(String username) {
        logger.info("Fetching user with username: " + username);
        User optinalUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username: " + username + " not found!")
        );
        UserResponseDTO response = UserMapper.responseToUser(optinalUser);
        List<TaskResponse> tasks = taskClient.getTasksByUserId(optinalUser.getId());
        response.setTasks(tasks);
        return response;
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public UserResponseDTO updateUserRole(String id, Role role) {
        logger.info("Accessing Administrative Service for promoting user with id: " + id + " for the role of: " + role);
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found!")
        );
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        return UserMapper.responseToUser(savedUser);
    }

    @Override
    @CacheEvict(value = "userCache", key = "'userInfo'")
    public List<UserResponseDTO> registerMultipleUsers(List<UserRegisterDTO> registerDTOs) {
        logger.info("Attempting to register {} users", registerDTOs.size());

        // Validate input
        if (registerDTOs == null || registerDTOs.isEmpty()) {
            throw new IllegalArgumentException("User list cannot be null or empty");
        }

        // Check for duplicate emails and usernames in the input
        Set<String> emails = new HashSet<>();
        Set<String> usernames = new HashSet<>();
        for (UserRegisterDTO dto : registerDTOs) {
            if (!emails.add(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Duplicate email in input: " + dto.getEmail());
            }
            if (!usernames.add(dto.getUsername())) {
                throw new UserNameAlreadyExistsException("Duplicate username in input: " + dto.getUsername());
            }
        }

        // Check for existing emails and usernames in the database
        for (UserRegisterDTO dto : registerDTOs) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("A user with this email already exists: " + dto.getEmail());
            }
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new UserNameAlreadyExistsException("A user with this username already exists: " + dto.getUsername());
            }
        }

        // Process and save users
        List<UserResponseDTO> savedUsers = new ArrayList<>();
        for (UserRegisterDTO dto : registerDTOs) {
            logger.info("Creating user with email: {}", dto.getEmail());
            User user = UserMapper.mapToUser(dto);

            // Generate a unique ID
            String id = UUID.randomUUID().toString();
            user.setId(id);

            // Encode the password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Set default role and timestamps
            user.setRoles(EnumSet.of(Role.USER));
            user.setCreatedAt(LocalDateTime.now());
            user.setAccountEnabled(true); // As per existing code, set to true for testing
            user.setEmailVerified(true);  // As per existing code

            // Save user
            User savedUser = userRepository.save(user);
            logger.info("User saved with id: {}", id);

            // Map to response DTO
            savedUsers.add(UserMapper.responseToUser(savedUser));
        }

        logger.info("Successfully registered {} users", savedUsers.size());
        return savedUsers;
    }
}
