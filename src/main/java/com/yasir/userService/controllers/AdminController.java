package com.yasir.userService.controllers;

import com.yasir.userService.entities.User;
import com.yasir.userService.requests.UserRequest;
import com.yasir.userService.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")  // Only admins can access
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // create or update user
    @PostMapping("/create-or-update")
    public ResponseEntity<User> createOrUpdateUser(@RequestBody UserRequest request) {
        User user = userService.createOrUpdateUser(request);
        return ResponseEntity.ok(user);
    }
}
