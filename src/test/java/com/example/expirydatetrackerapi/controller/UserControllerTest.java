package com.example.expirydatetrackerapi.controller;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserAuthenticationDTO;
import com.example.expirydatetrackerapi.models.dto.UserExpiriesDTO;
import com.example.expirydatetrackerapi.models.dto.UserWishlistsDTO;
import com.example.expirydatetrackerapi.service.UsersService;
import com.example.expirydatetrackerapi.web.UserController;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UsersService service;
    private UserController controller;

    @BeforeEach
    void setUp(){
        controller = new UserController(service);
    }

    @Test
    void shouldLogin(){
        //given
        String username = "Username";
        String password = "Password";
        String authCode = "Secret";
        User user = new User(username, password, authCode);
        given(service.login(username, password)).willReturn(user);
        //when
        ResponseEntity<UserAuthenticationDTO> userResponse = controller.login(username, password);
        //then
        assertThat(userResponse.getBody().getAuth_code()).isEqualTo(authCode);
    }

    @Test
    void shouldRegister(){
        //given
        String username = "Username";
        String password = "Password";
        String confirmPassword = "Password";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String authCode = "Secret";
        User user = new User(username, name, surname, email, password, authCode);
        given(service.register(username, password, confirmPassword, name, surname, email)).willReturn(user);
        //when
        ResponseEntity<String> userResponse = controller.register(username, password, confirmPassword, name, surname, email);
        //then
        assertThat(userResponse.getBody()).isEqualTo("User registered successfully!");
    }

    @Test
    void shouldGetExpiries(){
        //given
        String username = "Username";
        String authCode = "Secret";
        given(service.getExpiryListForUser(username, authCode)).willReturn(Collections.emptyList());
        //when
        ResponseEntity<UserExpiriesDTO> userResponse = controller.expiryListForUser(username, authCode);
        //then
        assertThat(userResponse.getBody().getExpiries().size()).isEqualTo(0);
    }

    @Test
    void shouldGetWishlist(){
        //given
        String username = "Username";
        String authCode = "Secret";
        given(service.getWishlistForUser(username, authCode)).willReturn(Collections.emptyList());
        //when
        ResponseEntity<UserWishlistsDTO> userResponse = controller.wishlistProductsForUser(username, authCode);
        //then
        assertThat(userResponse.getBody().getWishlist().size()).isEqualTo(0);
    }


}
