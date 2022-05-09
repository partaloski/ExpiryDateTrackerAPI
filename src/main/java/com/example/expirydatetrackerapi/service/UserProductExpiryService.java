package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;

import java.time.LocalDate;
import java.util.List;

public interface UserProductExpiryService {
    UserProductsExpiryDTO addExpiry(String username, String productId, LocalDate expiryDate, String auth_code);
    void deleteExpiry(Integer id, String auth_code);
    void clearExpiryList(String username, String auth_code);
}
