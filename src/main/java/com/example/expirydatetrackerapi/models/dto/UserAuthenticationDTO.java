package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthenticationDTO {
    private String auth_code;

    public UserAuthenticationDTO(String auth_code) {
        this.auth_code = auth_code;
    }


    public static UserAuthenticationDTO createAuthOf(User user){
        return new UserAuthenticationDTO(user.getAuth_code());
    }
}
