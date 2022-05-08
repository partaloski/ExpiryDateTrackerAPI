package com.example.expirydatetrackerapi.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserWishlistsDTO {
    private List<UserProductsWishlistDTO> wishlist;

    public UserWishlistsDTO(List<UserProductsWishlistDTO> wishlist) {
        this.wishlist = wishlist;
    }

    public UserWishlistsDTO() {
    }

    public static UserWishlistsDTO createOf(List<UserProductsWishlistDTO> wishlist){
        return new UserWishlistsDTO(wishlist);
    }
}
