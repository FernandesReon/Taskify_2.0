package com.reon.userservice.controller;

import com.reon.userservice.service.impl.EmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class VerificationController {
    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final EmailVerificationService emailVerificationService;

    public VerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){
        logger.info("(Controller) :: Verification of email.");
        emailVerificationService.verifyToken(token);
        return ResponseEntity.ok("Email verification successful. You can now login.");
    }
}
