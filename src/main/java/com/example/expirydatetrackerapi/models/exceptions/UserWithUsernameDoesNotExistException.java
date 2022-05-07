package com.example.expirydatetrackerapi.models.exceptions;

public class UserWithUsernameDoesNotExistException extends RuntimeException{
    public UserWithUsernameDoesNotExistException(String username) {
        super("The user with username \"" + username + "\" cannot be found.");
    }
}
