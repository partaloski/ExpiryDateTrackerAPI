package com.example.expirydatetrackerapi.models.exceptions;

public class UserExpiryDoesNotExistException extends RuntimeException{
    public UserExpiryDoesNotExistException(Integer id) {
        super("The expiry with ID {" + id + "} does not exist.");
    }
}
