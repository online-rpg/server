package com.vrpg.server.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AlreadyExistingUserException extends RuntimeException {

    public AlreadyExistingUserException() {
        super("User already exists!");
    }
}
