package com.example.expirydatetrackerapi.models.exceptions;

public class UserWithEmailAlreadyExists extends RuntimeException{
    public UserWithEmailAlreadyExists(String email) {
        super("You already have an user with the email \"" + email + "\" registered. Try logging in.");
    }
}
