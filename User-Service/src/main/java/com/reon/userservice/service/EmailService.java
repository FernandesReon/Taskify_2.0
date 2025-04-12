package com.reon.userservice.service;

import com.reon.userservice.model.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
}
