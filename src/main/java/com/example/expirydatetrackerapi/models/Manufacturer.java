package com.example.expirydatetrackerapi.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "manufacturers")
@Data
public class Manufacturer {
    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    public Manufacturer(String name) {
        this.name = name;
    }

    public Manufacturer() {
    }
}