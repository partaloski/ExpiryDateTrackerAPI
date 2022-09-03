package com.example.expirydatetrackerapi.controller;

import com.example.expirydatetrackerapi.helpers.DateParser;
import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.service.UserProductExpiryService;
import com.example.expirydatetrackerapi.service.UserProductWishlistService;
import com.example.expirydatetrackerapi.web.UsersAndProductsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsersAndProductsControllerTest {
    @Mock
    private UserProductExpiryService serviceExpiry;
    @Mock
    private UserProductWishlistService serviceWishlist;
    private UsersAndProductsController controller;

    @BeforeEach
    void setUp(){
        controller = new UsersAndProductsController(serviceExpiry, serviceWishlist);
    }

    @Test
    void shouldAddExpiry(){
        //given
        Integer id = 1;
        String username = "Username";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String password = "Password";
        User user = new User(username, name, surname, email, password);
        UserDTO userDTO = new UserDTO(username, name, surname, email);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productId = "1234";
        String productName = "Product";
        Product product = new Product(productId, productName, manufacturer);
        String date ="2022-01-01";
        LocalDate parsedDate = DateParser.parseDate(date);
        String authCode = "Secret";
        UserProductsExpiryDTO dto =  new UserProductsExpiryDTO(id, user, product, parsedDate);
        given(serviceExpiry.addExpiry(username, productId, parsedDate, authCode)).willReturn(dto);
        //when
        ResponseEntity<UserProductsExpiryDTO> response = controller.addExpiry(username, productId, date, authCode);
        //then
        assertThat(response.getBody().getExpiryDate()).isEqualTo(parsedDate);
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(response.getBody().getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldRemoveExpiry(){
        //given
        Integer id = 1;
        String authCode = "Secret";
        //when
        ResponseEntity<String> response = controller.removeExpiry(id, authCode);
        //then
        assertThat(response.getBody()).isEqualTo("The Expiry item was removed.");
    }

    @Test
    void shouldClearExpiries(){
        //given
        String username = "Username";
        String authCode = "Secret";
        //when
        ResponseEntity<String> response = controller.clearExpiries(username, authCode);
        //then
        assertThat(response.getBody()).isEqualTo("The Expiry list for the user with username \"" + username + "\" was emptied out successfully.");
    }

    @Test
    void shouldAddWishlistItem(){
        //given
        String username = "Username";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String password = "Password";
        User user = new User(username, name, surname, email, password);
        UserDTO userDTO = new UserDTO(username, name, surname, email);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productId = "1234";
        String productName = "Product";
        Product product = new Product(productId, productName, manufacturer);
        Integer quantity = 1;
        String authCode = "Secret";
        UserProductsWishlistDTO dto =  new UserProductsWishlistDTO(user, product, quantity);
        given(serviceWishlist.addToWishlist(username, productId, quantity, authCode)).willReturn(dto);
        //when
        ResponseEntity<UserProductsWishlistDTO> response = controller.addWishlist(username, productId, quantity, authCode);
        //then
        assertThat(response.getBody().getQuantity()).isEqualTo(quantity);
        assertThat(response.getBody().getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(response.getBody().getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldRemoveWishlistItem(){
        //given
        String username = "Username";
        String productId = "1234";
        String authCode = "Secret";
        //when
        ResponseEntity<String> response = controller.removeWishlistItem(username, productId, authCode);
        //then
        assertThat(response.getBody()).isEqualTo("The wishlist item was removed.");
    }

    @Test
    void shouldClearWishlist(){
        //given
        String username = "Username";
        String authCode = "Secret";
        //when
        ResponseEntity<String> response = controller.clearWishlist(username, authCode);
        //then
        assertThat(response.getBody()).isEqualTo("The wishlist has been cleaned");
    }

}
