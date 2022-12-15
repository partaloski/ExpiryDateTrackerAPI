package com.example.expirydatetrackerapi.models;

import com.example.expirydatetrackerapi.helpers.AuthCodeGenerator;
import com.example.expirydatetrackerapi.models.dto.UserDTO;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "auth_code", length = 32, columnDefinition="bpchar")
    private String auth_code;

    @OneToMany(mappedBy="user")
    private Collection<UserProductsExpiry> productsExpiries;

    @OneToMany(mappedBy="user")
    private Collection<UserProductsWishlist> productsWishlist;

    public User(String username, String password, String auth_code) {
        this.username = username;
        this.password = password;
        this.auth_code = auth_code;
        this.name = "";
        this.surname = "";
        this.email = "";
        this.productsExpiries = Collections.EMPTY_LIST;
        this.productsWishlist = Collections.EMPTY_LIST;
    }

    public User(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.auth_code = AuthCodeGenerator.generate();
    }

    public User(String username, String name, String surname, String email, String password, String auth_code) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.auth_code = auth_code;
    }
}
