package com.reon.userservice.service;

import com.reon.userservice.dto.LoginDTO;
import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    // CRUD operations
    UserResponseDTO registerNewUser(UserRegisterDTO registerDTO);
    UserResponseDTO updateExistingUser(String userId, UserRegisterDTO updateDTO);
    void deleteUser(String userId);
    UserResponseDTO accessUser(LoginDTO loginDTO);
}
