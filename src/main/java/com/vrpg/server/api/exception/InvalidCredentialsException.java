package com.vrpg.server.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        this("Invalid username and password combination!");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
