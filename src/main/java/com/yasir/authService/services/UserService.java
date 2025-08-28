package com.yasir.authService.services;

import com.yasir.authService.models.UserDto;
import com.yasir.authService.requests.AuthRequest;

public interface UserService {
    void registerUser(UserDto user);

    void authenticateUser(AuthRequest request);

}
