package com.reon.userservice.service;

import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.model.Role;

import java.util.List;

public interface AdminService {
    List<UserResponseDTO> fetchAllUsers();
    UserResponseDTO fetchById(String id);
    UserResponseDTO fetchByEmail(String email);
    UserResponseDTO fetchByUsername(String username);
    UserResponseDTO updateUserRole(String id, Role role);
    List<UserResponseDTO> registerMultipleUsers(List<UserRegisterDTO> registerDTOs);
}
