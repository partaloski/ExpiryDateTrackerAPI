package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;

import java.util.List;

public interface UserProductWishlistService {
    UserProductsWishlistDTO addToWishlist(String username, Integer productId, Integer quantity, String auth_code);
    void removeFromWishlist(String username, Integer productId, String auth_code);
    void clearWishlist(String username, String auth_code);
}
