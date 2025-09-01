package com.yasir.userService.services;

import com.yasir.userService.entities.User;
import com.yasir.userService.requests.UserRequest;

public interface UserService {

    User createOrUpdateUser(UserRequest request);
}
