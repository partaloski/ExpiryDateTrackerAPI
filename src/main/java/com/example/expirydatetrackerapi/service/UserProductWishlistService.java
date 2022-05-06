package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;

import java.util.List;

public interface UserProductWishlistService {
    UserProductsWishlist addToWishlist(String username, Integer productId, Integer quantity);
    void removeFromWishlist(String username, Integer productId);
    void clearWishlist(String username);
    List<UserProductsWishlist> getWishlistForUser(String username);
}
