package com.example.expirydatetrackerapi.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "manufacturers")
@Data
public class Manufacturer {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    public Manufacturer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Manufacturer(String name) {
        this.name = name;
    }

    public Manufacturer() {
    }
}
