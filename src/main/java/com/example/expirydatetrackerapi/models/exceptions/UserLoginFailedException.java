package com.example.expirydatetrackerapi.models.exceptions;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException() {
        super("The username and password combination doesn't match any of our database entries.");
    }
}
