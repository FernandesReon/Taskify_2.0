package com.reon.userservice.service.impl;

import com.reon.userservice.model.User;
import com.reon.userservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    @Value("${app.verification}")
    private static String verificationUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(User user, String token) {
        logger.info("Sending verification email to user with email: " + user.getEmail());
        String recipientAddress = user.getEmail();
        String subject = "Email Verification from Taskify";
        String confirmationUrl = verificationUrl + "?token=" + token;
        String message =
                "Thank you for registering at Taskify. Please click the link given below to verify " +
                        "your email:\n" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
