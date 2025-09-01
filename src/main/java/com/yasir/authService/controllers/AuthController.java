package com.yasir.authService.controllers;

import com.yasir.authService.entities.User;
import com.yasir.authService.models.UserDto;
import com.yasir.authService.requests.AuthRequest;
import com.yasir.authService.requests.SignUpRequest;
import com.yasir.authService.response.AuthResponse;
import com.yasir.authService.response.RegisterResponse;
import com.yasir.authService.services.JwtService;
import com.yasir.authService.services.RefreshTokenService;
import com.yasir.authService.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    public AuthController(UserService userService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    // ✅ Register new User
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody SignUpRequest request) {
        RegisterResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
    }

    // ✅ Authenticate User
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticateUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    // ✅ Refresh JWT using refresh token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            // extract username from refresh token owner
            User user = refreshTokenService.getUserFromRefreshToken(refreshToken);
            String newJwt = jwtService.generateJwtToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .jwtToken(newJwt)
                            .refreshToken(refreshToken) // reuse existing refresh token
                            .build()
            );
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
