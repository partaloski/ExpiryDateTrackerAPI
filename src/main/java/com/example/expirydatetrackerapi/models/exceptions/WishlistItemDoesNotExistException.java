package com.example.expirydatetrackerapi.models.exceptions;

public class WishlistItemDoesNotExistException extends RuntimeException{
    public WishlistItemDoesNotExistException(String username, String productId) {
        super(String.format("A Wishlist Item for the user %s and product %s does not exist.", username, productId));
    }
}
