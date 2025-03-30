package com.reon.userservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailException(EmailAlreadyExistsException emailException){
        logger.warn("Email Exception: {}", emailException.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "User with this email already exists.");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameException(UserNameAlreadyExistsException usernameException){
        logger.warn("Username Exception: {}", usernameException.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "User with this username already exists.");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException userNotFoundException
    ){
        logger.warn("User Exception: {}", userNotFoundException.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "User not found");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException invalid){
        logger.warn("Invalid credentials: {}", invalid.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid credentials");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException invalidToken){
        logger.warn("Invalid Token: {}", invalidToken.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid Token");
        return ResponseEntity.badRequest().body(error);
    }
}
