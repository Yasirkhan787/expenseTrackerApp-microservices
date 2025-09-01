package com.yasir.authService.services;

import com.yasir.authService.models.UserDto;
import com.yasir.authService.requests.AuthRequest;
import com.yasir.authService.requests.SignUpRequest;
import com.yasir.authService.response.AuthResponse;
import com.yasir.authService.response.RegisterResponse;

public interface UserService {
    RegisterResponse registerUser(SignUpRequest request);

    AuthResponse authenticateUser(AuthRequest request);

}
