package com.yasir.authService.services.Implementations;

import com.yasir.authService.entities.Role;
import com.yasir.authService.entities.User;
import com.yasir.authService.models.UserDto;
import com.yasir.authService.repositories.UserRepository;
import com.yasir.authService.requests.AuthRequest;
import com.yasir.authService.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Register new user
    @Override
    public void registerUser(UserDto userDto) {

        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.valueOf("USER"));

        // Save User to AuthDatabase
        userRepository.save(user);

        // TODO: PUBLISH USER DETAILS TO KAFKA
        // TODO: ADD EXCEPTION HANDLING

    }

    // Authenticate User
    @Override
    public void authenticateUser(AuthRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));


    }



}
