package com.example.expirydatetrackerapi.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserWishlistsDTO {
    private List<UserProductsWishlistDTO> wishlist;

    public UserWishlistsDTO(List<UserProductsWishlistDTO> wishlist) {
        this.wishlist = wishlist;
    }
    public static UserWishlistsDTO createOf(List<UserProductsWishlistDTO> wishlist){
        return new UserWishlistsDTO(wishlist);
    }
}
