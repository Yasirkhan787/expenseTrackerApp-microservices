package com.yasir.authService.exceptions;

public class UserRegistrationFailedException extends RuntimeException {
    public UserRegistrationFailedException(String message) {
        super(message);
    }
}
