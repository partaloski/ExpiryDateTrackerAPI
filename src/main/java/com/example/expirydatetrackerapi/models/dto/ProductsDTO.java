package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductsDTO {
    private List<Product> Products;

    public ProductsDTO(List<Product> products) {
        Products = products;
    }

    public static ProductsDTO createOf(List<Product> products){
        return new ProductsDTO(products);
    }

}
