package com.example.expirydatetrackerapi.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserWithUsernameDoesNotExistException extends ResponseStatusException {
    public UserWithUsernameDoesNotExistException(String username) {
        super(HttpStatus.BAD_REQUEST, "The user with username \"" + username + "\" cannot be found.");
    }
}
