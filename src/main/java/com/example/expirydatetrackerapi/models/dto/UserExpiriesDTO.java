package com.example.expirydatetrackerapi.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserExpiriesDTO {
    private List<UserProductsExpiryDTO> expiries;

    public UserExpiriesDTO(List<UserProductsExpiryDTO> expiries) {
        this.expiries = expiries;
    }

    public UserExpiriesDTO() {
    }

    public static UserExpiriesDTO createOf(List<UserProductsExpiryDTO> list){
        return new UserExpiriesDTO(list);
    }
}