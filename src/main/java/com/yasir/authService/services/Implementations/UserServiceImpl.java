package com.yasir.authService.services.Implementations;

import com.yasir.authService.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register new user

}
