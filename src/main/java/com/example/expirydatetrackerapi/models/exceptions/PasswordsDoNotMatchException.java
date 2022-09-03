package com.example.expirydatetrackerapi.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordsDoNotMatchException extends ResponseStatusException {
    public PasswordsDoNotMatchException() {
        super(HttpStatus.NOT_ACCEPTABLE);
    }
}
