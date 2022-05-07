package com.example.expirydatetrackerapi.models;

import com.example.expirydatetrackerapi.helpers.AuthCodeGenerator;
import com.example.expirydatetrackerapi.models.dto.UserDTO;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="users")
@Data
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

    public User(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.auth_code = AuthCodeGenerator.generate();
    }

    public UserDTO generateDTO(){
        return new UserDTO(this.username, this.name, this.surname, this.email);
    }

    public User() {
    }

    public void clearQuery(){
        this.productsExpiries = null;
        this.productsWishlist = null;
    }
}
