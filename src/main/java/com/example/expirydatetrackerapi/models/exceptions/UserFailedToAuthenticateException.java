package com.example.expirydatetrackerapi.models.exceptions;

public class UserFailedToAuthenticateException extends RuntimeException{
    public UserFailedToAuthenticateException(String username) {
        super("The user with the username \""+username+"\" failed to authenticate.");
    }
}
