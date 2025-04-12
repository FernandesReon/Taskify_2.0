package com.reon.userservice.service.impl;

import com.reon.userservice.exception.InvalidTokenException;
import com.reon.userservice.model.EmailVerificationToken;
import com.reon.userservice.model.User;
import com.reon.userservice.repository.EmailRepository;
import com.reon.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {
    private final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);
    private static final int TOKEN_EXPIRATION_TIME = 1;

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final EmailServiceImpl emailService;

    public EmailVerificationService(UserRepository userRepository, EmailRepository emailRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.emailService = emailService;
    }

    public void createAndSendVerificationToken(User user){
        // Token creation
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_TIME);
        EmailVerificationToken verifyToken = new EmailVerificationToken(token, user, expiryDate);

        // Token saved in database for further verification
        emailRepository.save(verifyToken);

        // Email send the respective user (newly registered user).
        emailService.sendVerificationEmail(user, token);
    }

    public void verifyToken(String token){
        // Check if the token exists or not.
        EmailVerificationToken optionalToken = emailRepository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("Token not found. Invalid Token."));

        // If exists then check whether the token is valid or not.
        if (optionalToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new InvalidTokenException("Token has been expired.");
        }

        // Fetch the user to whom the token is assigned
        User user = optionalToken.getUser();
        // now set the values to true.
        user.setAccountEnabled(true);
        user.setEmailVerified(true);
        // save the user.
        userRepository.save(user);

        // delete the token once verified.
        emailRepository.delete(optionalToken);
    }
}
