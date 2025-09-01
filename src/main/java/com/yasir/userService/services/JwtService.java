package com.yasir.userService.services;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
    Boolean validateToken(String token);

    String getSubject(String token);

    Collection<? extends GrantedAuthority> getAuthorities(String token);
}
