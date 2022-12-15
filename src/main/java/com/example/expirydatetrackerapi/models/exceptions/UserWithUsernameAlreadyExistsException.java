package com.example.expirydatetrackerapi.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserWithUsernameAlreadyExistsException extends ResponseStatusException {
    public UserWithUsernameAlreadyExistsException(String email) {
        super(HttpStatus.FORBIDDEN, "You already have an user with the email \"" + email + "\" registered. Try logging in.");
    }
}
