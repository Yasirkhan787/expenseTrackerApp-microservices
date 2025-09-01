package com.yasir.authService.services.Implementations;

import com.yasir.authService.entities.RefreshToken;
import com.yasir.authService.entities.User;
import com.yasir.authService.repositories.RefreshTokenRepository;
import com.yasir.authService.repositories.UserRepository;
import com.yasir.authService.services.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7L; // 7 days
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    //
    @Override
    public String generateRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + username));

        // check if a refresh token already exists for this user
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            // check expiration
            if (refreshToken.getExpirationDate().before(new Date())) {
                // expired → create new one
                refreshToken.setToken(UUID.randomUUID().toString());
                refreshToken.setExpirationDate(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION));
            }
        } else {
            // new refresh token
            refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expirationDate(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                    .user(user)
                    .build();
        }

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    //
    @Override
    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getExpirationDate().after(new Date()))
                .orElse(false);
    }

    //
    @Override
    public User getUserFromRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

}
