package com.example.expirydatetrackerapi.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @Column(name = "id")
    private String productId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    public Product(String productId, String name, Manufacturer manufacturer) {
        this.productId = productId;
        this.name = name;
        this.manufacturer = manufacturer;
    }
}
