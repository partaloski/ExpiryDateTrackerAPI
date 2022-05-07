package com.example.expirydatetrackerapi.models.exceptions;

public class ProductWithIdAlreadyExistsException extends RuntimeException{
    public ProductWithIdAlreadyExistsException(Integer id) {
        super("The product with id #"+id+" already exists.");
    }
}
