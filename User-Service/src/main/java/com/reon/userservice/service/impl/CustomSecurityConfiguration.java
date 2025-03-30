package com.reon.userservice.service.impl;

import com.reon.userservice.exception.UserNotFoundException;
import com.reon.userservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomSecurityConfiguration implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomSecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " not found!")
        );
    }
}
