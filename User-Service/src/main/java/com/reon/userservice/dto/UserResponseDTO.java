package com.reon.userservice.dto;

import com.reon.userservice.model.Provider;
import com.reon.userservice.model.Role;
import com.reon.userservice.model.task_ms.TaskResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private String id;
    private String name;
    private String username;
    private String email;
    private Set<Role> roles;
    private boolean accountEnabled;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedOn;
    private Provider provider;
    private List<TaskResponse> tasks;
}