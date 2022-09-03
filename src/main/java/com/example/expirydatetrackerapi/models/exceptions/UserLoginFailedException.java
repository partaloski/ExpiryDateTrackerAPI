package com.example.expirydatetrackerapi.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserLoginFailedException extends ResponseStatusException {
    public UserLoginFailedException() {
        super(HttpStatus.BAD_REQUEST,"The username and password combination doesn't match any of our database entries.");
    }
}
