package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> findAll();
    Manufacturer save(String name);
    void delete(Integer id);
}
