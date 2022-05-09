package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProductsExpiryDTO {
    private Integer id;
    private UserDTO user;
    private Product product;
    private LocalDate expiryDate;

    public UserProductsExpiryDTO(Integer id, User user, Product product, LocalDate expiryDate) {
        this.id = id;
        this.user = UserDTO.createOfUser(user);
        this.product = product;
        this.expiryDate = expiryDate;
    }

    public UserProductsExpiryDTO() {
    }

    public static UserProductsExpiryDTO createExpiryOf(UserProductsExpiry expiry){
        return new UserProductsExpiryDTO(expiry.getID(), expiry.getUser(), expiry.getProduct(), expiry.getExpirydate());
    }

}
