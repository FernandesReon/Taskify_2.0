package com.reon.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Username is required.")
    @Size(max = 10, message = "Username can be maximum of 10 characters.")
    private String username;

    @NotBlank(message = "Email Address is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 10, max = 16, message = "Password has to be in range of 10 to 16 characters.")
    private String password;
}