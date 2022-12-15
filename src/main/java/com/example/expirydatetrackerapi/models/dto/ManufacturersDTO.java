package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Manufacturer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ManufacturersDTO implements Serializable {
    private List<Manufacturer> Manufacturers;

    public ManufacturersDTO(List<Manufacturer> products) {
        Manufacturers = products;
    }

    public static ManufacturersDTO createOf(List<Manufacturer> manufacturers){
        return new ManufacturersDTO(manufacturers);
    }

}
