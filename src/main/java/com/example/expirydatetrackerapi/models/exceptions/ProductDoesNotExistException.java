package com.example.expirydatetrackerapi.models.exceptions;

public class ProductDoesNotExistException extends RuntimeException{
    public ProductDoesNotExistException(String id) {
        super("The product with barcode (id) [" + id + "] does not exist.");
    }
}
