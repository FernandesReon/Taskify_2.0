package com.reon.userservice.controller;

import com.reon.userservice.dto.LoginDTO;
import com.reon.userservice.dto.UserRegisterDTO;
import com.reon.userservice.dto.UserResponseDTO;
import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.jwt.JwtService;
import com.reon.userservice.mapper.UserMapper;
import com.reon.userservice.model.User;
import com.reon.userservice.model.task_ms.TaskResponse;
import com.reon.userservice.repository.UserRepository;
import com.reon.userservice.service.TaskClient;
import com.reon.userservice.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TaskClient taskClient;

    public UserController(UserServiceImpl userService, JwtService jwtService,
                          AuthenticationManager authenticationManager, UserRepository userRepository,
                          TaskClient taskClient) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.taskClient = taskClient;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO){
        logger.info("Registration controller :: registration for user: " + registerDTO.getEmail() + " ongoing.");
        UserResponseDTO newUser =  userService.registerNewUser(registerDTO);
        logger.info("User details: " + newUser.toString());     // only for debugging.
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

    // Login using JWT
    @PostMapping("/login")
    public String loginViaJWT(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(loginDTO.getEmail(), userDetails.getAuthorities());
        }
        else {
            throw new UserNotFoundException("Invalid user request !");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email)
        );
        UserResponseDTO response = UserMapper.responseToUser(user);
        List<TaskResponse> tasks = taskClient.getTasksByUserId(user.getId());
        response.setTasks(tasks);
        return ResponseEntity.ok().body(response);
    }
}
