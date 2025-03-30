package com.reon.userservice.controller;

import com.reon.userservice.dto.LoginDTO;
import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.jwt.JwtService;
import com.reon.userservice.service.impl.AdminServiceImpl;
import com.reon.userservice.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;
    private final AdminServiceImpl adminService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public UserController(UserServiceImpl userService, AdminServiceImpl adminService,
                          JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO){
        logger.info("Registration controller :: registration for user: " + registerDTO.getEmail() + " ongoing.");
        UserResponseDTO newUser =  userService.registerNewUser(registerDTO);
        logger.info("A new user registered with emailId: " + registerDTO.getEmail());
        return ResponseEntity.ok().body(newUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String id, @RequestBody UserRegisterDTO updateDTO){
        logger.info("Update controller :: updating user with id: " + id);
        UserResponseDTO updatedUser = userService.updateExistingUser(id, updateDTO);
        logger.info("User with id: " + id + " updated successfully.");
        return ResponseEntity.ok().body(updatedUser);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.info("Delete controller :: deleting user with id: " + id);
        userService.deleteUser(id);
        logger.info("User with id: " + id + " deleted successfully.");
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserResponseDTO> loginUser (@RequestBody LoginDTO loginDTO){
//        logger.info("Login Controller :: fetching info for user with email: " + loginDTO.getEmail());
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDTO.getEmail(),
//                        loginDTO.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserResponseDTO loggedInUser = userService.accessUser(loginDTO);
//        logger.info("User loggedIn");
//        return ResponseEntity.ok().body(loggedInUser);
//    }

    // Login using JWT
    @PostMapping("/login")
    public String loginViaJWT(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(loginDTO.getEmail());
        }
        else {
            throw new UserNotFoundException("Invalid user request !");
        }
    }
}
