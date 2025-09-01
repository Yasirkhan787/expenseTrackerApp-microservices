package com.yasir.userService.services;

import com.yasir.userService.models.UserDto;

public interface EventConsumerService {

    void consumeEvent(UserDto userDto);
}

