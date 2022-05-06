package com.example.expirydatetrackerapi.models.exceptions;

public class ManufacturerDoesNotExistException extends RuntimeException{
    public ManufacturerDoesNotExistException(Integer id) {
        super("Manufacturer with ID #"+id+ " does not exist.");
    }
}
