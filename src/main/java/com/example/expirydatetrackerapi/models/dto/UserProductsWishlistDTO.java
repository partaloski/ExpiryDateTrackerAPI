package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import lombok.Data;

@Data
public class UserProductsWishlistDTO {
    private UserDTO user;
    private Product product;
    private Integer quantity;

    public UserProductsWishlistDTO(User user, Product product, Integer quantity) {
        UserDTO userDTO = UserDTO.createOfUser(user);
        this.user = userDTO;
        this.product = product;
        this.quantity = quantity;
    }

    public UserProductsWishlistDTO() {
    }
    public static UserProductsWishlistDTO createExpiryOf(UserProductsWishlist expiry){
        return new UserProductsWishlistDTO(expiry.getUser(), expiry.getProduct(), expiry.getQuantity());
    }
}
