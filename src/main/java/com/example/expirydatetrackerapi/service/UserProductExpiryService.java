package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;

import java.time.LocalDate;
import java.util.List;

public interface UserProductExpiryService {
    UserProductsExpiry addExpiry(String username, Integer productId, LocalDate expiryDate);
    void deleteExpiry(Integer id);
    void clearExpiryList(String username);
    List<UserProductsExpiry> getExpiryListForUser(String username);
}
