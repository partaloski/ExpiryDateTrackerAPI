package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Manufacturer;
import lombok.Data;

import java.util.List;

@Data
public class ManufacturersDTO {
    private List<Manufacturer> Manufacturers;

    public ManufacturersDTO(List<Manufacturer> products) {
        Manufacturers = products;
    }

    public static ManufacturersDTO createOf(List<Manufacturer> manufacturers){
        return new ManufacturersDTO(manufacturers);
    }

    public ManufacturersDTO() {
    }
}
