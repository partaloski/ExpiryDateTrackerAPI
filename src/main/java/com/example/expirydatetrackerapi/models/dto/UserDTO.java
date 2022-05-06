package com.example.expirydatetrackerapi.models.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String name;
    private String surname;
    private String email;

    public UserDTO(String username, String name, String surname, String email) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
