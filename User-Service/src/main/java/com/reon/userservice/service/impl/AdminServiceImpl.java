package com.reon.userservice.service.impl;

import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.mapper.UserMapper;
import com.reon.userservice.model.Role;
import com.reon.userservice.model.User;
import com.reon.userservice.repository.UserRepository;
import com.reon.userservice.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> fetchAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper :: responseToUser).toList();
    }
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO fetchById(String id) {
        logger.info("Fetching user with id: " + id);
        User optionalUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found!")
        );
        return UserMapper.responseToUser(optionalUser);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponseDTO fetchByEmail(String email) {
        logger.info("Fetching user with email: " + email);
        User optionalUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " not found!")
        );
        return UserMapper.responseToUser(optionalUser);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserResponseDTO fetchByUsername(String username) {
        logger.info("Fetching user with username: " + username);
        User optinalUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username: " + username + " not found!")
        );
        return UserMapper.responseToUser(optinalUser);
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
}
