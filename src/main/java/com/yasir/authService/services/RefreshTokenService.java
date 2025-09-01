package com.yasir.authService.services;

import com.yasir.authService.entities.User;

public interface RefreshTokenService {

    String generateRefreshToken(String email);

    boolean validateRefreshToken(String token);

    User getUserFromRefreshToken(String token);

}
