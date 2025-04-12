package com.reon.userservice.mapper;

import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.model.User;


public class UserMapper {
    public static User mapToUser(UserRegisterDTO registerDTO){
        // data that comes from frontend get stored in database
        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public static UserResponseDTO responseToUser(User user){
        // only necessary data from the database is displayed to user
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setUsername(user.getEntityUsername());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles());
        response.setAccountEnabled(user.isAccountEnabled());
        response.setEmailVerified(user.isEmailVerified());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedOn(user.getUpdatedOn());
        response.setProvider(user.getProvider());
        return response;
    }

    public static void applyUpdates(User exisitingUser, UserRegisterDTO updateDTO) {
        if (updateDTO.getName() != null && !updateDTO.getName().isBlank()){
            exisitingUser.setName(updateDTO.getName());
        }
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isBlank()) {
            exisitingUser.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
            exisitingUser.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            exisitingUser.setPassword(updateDTO.getPassword());
        }
    }
}
