package com.reon.taskservice.exception;

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
    public ResponseEntity<Map<String, String>> handleValidationException (
            MethodArgumentNotValidException exception
    ){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskException(TaskNotFoundException taskException){
        logger.warn("Task not found: " + taskException.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "Task not found.");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TitleAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTitleException(TitleAlreadyExistsException titleExistException){
        logger.warn("Title already exists: " + titleExistException.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("message", "Title already exists.");
        return ResponseEntity.badRequest().body(error);
    }
}
