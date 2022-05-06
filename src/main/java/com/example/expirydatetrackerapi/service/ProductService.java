package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Product;

import java.util.List;

public interface ProductService {
    public List<Product> findAll();
    public Product addProduct(Integer id, String name, Integer manufacturer_id);
    public Product getProduct(Integer id);
    public void deleteProduct(Integer id);
}
