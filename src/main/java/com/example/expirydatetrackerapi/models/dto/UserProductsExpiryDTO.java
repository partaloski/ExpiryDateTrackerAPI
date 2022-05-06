package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProductsExpiryDTO {
    private UserDTO user;
    private Product product;
    private LocalDate expiryDate;

    public UserProductsExpiryDTO(User user, Product product, LocalDate expiryDate) {
        UserDTO userDto = UserDTO.createOfUser(user);
        this.user = userDto;
        this.product = product;
        this.expiryDate = expiryDate;
    }

    public UserProductsExpiryDTO() {
    }

    public static UserProductsExpiryDTO createExpiryOf(UserProductsExpiry expiry){
        return new UserProductsExpiryDTO(expiry.getUser(), expiry.getProduct(), expiry.getExpirydate());
    }

}
