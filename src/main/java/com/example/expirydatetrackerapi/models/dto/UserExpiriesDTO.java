package com.example.expirydatetrackerapi.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserExpiriesDTO {
    private List<UserProductsExpiryDTO> expiries;

    public UserExpiriesDTO(List<UserProductsExpiryDTO> expiries) {
        this.expiries = expiries;
    }


    public static UserExpiriesDTO createOf(List<UserProductsExpiryDTO> list){
        return new UserExpiriesDTO(list);
    }
}