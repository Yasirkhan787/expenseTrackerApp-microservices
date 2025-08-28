package com.yasir.authService.controllers;

import com.yasir.authService.models.UserDto;
import com.yasir.authService.requests.AuthRequest;
import com.yasir.authService.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register new User
    @PostMapping("/register")
    public void registerUser(@RequestBody UserDto user){
        userService.registerUser(user);
    }

    // Authenticate User
    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody AuthRequest request){
        try{
            userService.authenticateUser(request);
            return "Authenticated";
        }catch (Exception e){
            throw e;
        }
    }
}
