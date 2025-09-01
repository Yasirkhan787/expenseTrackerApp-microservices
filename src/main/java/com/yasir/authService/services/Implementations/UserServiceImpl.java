package com.yasir.authService.services.Implementations;

import ch.qos.logback.classic.Logger;
import com.yasir.authService.entities.Role;
import com.yasir.authService.entities.User;
import com.yasir.authService.exceptions.UserAlreadyExistsException;
import com.yasir.authService.exceptions.UserRegistrationFailedException;
import com.yasir.authService.models.UserDto;
import com.yasir.authService.repositories.UserRepository;
import com.yasir.authService.requests.AuthRequest;
import com.yasir.authService.requests.SignUpRequest;
import com.yasir.authService.response.AuthResponse;
import com.yasir.authService.response.RegisterResponse;
import com.yasir.authService.services.EventProducerService;
import com.yasir.authService.services.JwtService;
import com.yasir.authService.services.RefreshTokenService;
import com.yasir.authService.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    Logger log;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final EventProducerService eventProducerService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, EventProducerService eventProducerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.eventProducerService = eventProducerService;
    }

    // Register new user
    @Override
    public RegisterResponse registerUser(SignUpRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        // Save User
        User savedUser = userRepository.save(user);

        if (savedUser == null || savedUser.getId() == null) {
            throw new UserRegistrationFailedException("Failed to register user");
        }

        UserDto userDto = UserDto.builder()
                .id(savedUser.getId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // Publish user details to Kafka (non-blocking)
        try {
            eventProducerService.produceEvent(userDto);
        } catch (Exception e) {
            // Log and continue
            log.error("Failed to send user registration event to Kafka for user {}", savedUser.getEmail(), e);
            // (Optional) persist to a retry table or dead-letter queue
        }

        // Build response (always return if DB save was successful)
        return RegisterResponse.builder()
                .email(savedUser.getEmail())
                .build();
    }

    // Authenticate User
    @Override
    public AuthResponse authenticateUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            // Generate Access Token (JWT)
            String accessToken = jwtService.generateJwtToken(request.getEmail());

            // Generate Refresh Token (can be longer expiry)
            String refreshToken = refreshTokenService.generateRefreshToken(request.getEmail());

            // Build response
            AuthResponse response =
                    AuthResponse.builder()
                            .jwtToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();

            return response;
        } else {
            throw new RuntimeException("Authentication failed!");
        }
    }




}
