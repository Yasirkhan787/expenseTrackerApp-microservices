package com.yasir.authService.services;

import com.yasir.authService.models.UserDto;

public interface EventProducerService {

    void produceEvent(UserDto userDto);
}
