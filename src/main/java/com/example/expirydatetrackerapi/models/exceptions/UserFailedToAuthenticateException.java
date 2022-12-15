package com.example.expirydatetrackerapi.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserFailedToAuthenticateException extends ResponseStatusException {
    public UserFailedToAuthenticateException(String username) {
        super(HttpStatus.FORBIDDEN,"The user with the username \""+username+"\" failed to authenticate.");
    }
}
