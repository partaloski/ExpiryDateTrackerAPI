package com.example.expirydatetrackerapi.models.dto;

import com.example.expirydatetrackerapi.models.User;
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

    public static UserDTO createOfUser(User user){
        return new UserDTO(user.getUsername(), user.getName(), user.getSurname(), user.getEmail());
    }
}
