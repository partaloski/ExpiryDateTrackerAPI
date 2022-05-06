package com.example.expirydatetrackerapi.models.exceptions;

public class ProductWithIdAlreadyExists extends RuntimeException{
    public ProductWithIdAlreadyExists(Integer id) {
        super("The product with id #"+id+" already exists.");
    }
}
