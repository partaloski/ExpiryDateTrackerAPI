package com.example.expirydatetrackerapi.models.exceptions;

public class UserWithUsernameAlreadyExistsException extends RuntimeException{
    public UserWithUsernameAlreadyExistsException(String email) {
        super("You already have an user with the email \"" + email + "\" registered. Try logging in.");
    }
}
