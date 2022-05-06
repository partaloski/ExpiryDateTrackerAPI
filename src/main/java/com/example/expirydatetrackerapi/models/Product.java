package com.example.expirydatetrackerapi.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @Column(name = "id")
    private Integer productId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    public Product(Integer productId, String name, Manufacturer manufacturer) {
        this.productId = productId;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public Product() {
    }
}
