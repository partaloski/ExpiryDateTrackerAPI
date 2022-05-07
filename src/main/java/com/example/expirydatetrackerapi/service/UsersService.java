package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;

import java.util.List;

public interface UsersService {
    User login(String username, String password);
    User register(String username, String password, String confirmPassword, String name, String surname, String email);
    List<UserProductsWishlistDTO> getWishlistForUser(String username);
    List<UserProductsExpiryDTO> getExpiryListForUser(String username);
}
