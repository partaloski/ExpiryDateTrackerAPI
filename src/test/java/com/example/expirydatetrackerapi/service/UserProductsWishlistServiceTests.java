package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.dto.UserWishlistsDTO;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductWishlistRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.impl.UserProductWishlistServiceImpl;
import com.example.expirydatetrackerapi.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserProductsWishlistServiceTests {
    @Mock
    private UserProductWishlistRepository repository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ProductRepository productRepository;
    private UsersService userService;
    private UserProductWishlistService service;

    @BeforeEach
    void setUp(){
        userService = new UsersServiceImpl(usersRepository);
        service = new UserProductWishlistServiceImpl(repository, usersRepository, productRepository, userService);
    }

    @Test
    void shouldAddToWishList(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        List<UserProductsWishlist> wishlist = new LinkedList<>();
        User user = new User(username, name, surname, email, password, auth_code);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productId, productName, manufacturer);
        Integer quantity = 1;
        UserProductsWishlist item = new UserProductsWishlist(user, product, quantity);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(repository.save(item))
                .willReturn(item);
        //then
        UserProductsWishlistDTO dto = service.addToWishlist(username, productId, quantity, auth_code);
        assertThat(dto.getProduct().getProductId()).isEqualTo(productId);
        assertThat(dto.getProduct().getName()).isEqualTo(productName);
        assertThat(dto.getProduct().getManufacturer().getId()).isEqualTo(manufacturerId);
        assertThat(dto.getProduct().getManufacturer().getName()).isEqualTo(manufacturerName);
        assertThat(dto.getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void shouldNotAddToWishListUserNotFound(){
        //given
        String username = "username";
        String productId = "Product";
        Integer quantity = 1;
        String auth_code = "secret";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.addToWishlist(username, productId, quantity, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotAddToWishListUserFailedAuth(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_fake = "fake";
        User user = new User(username, name, surname, email, password, auth_code);
        String productId = "1234";
        Integer quantity = 1;
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.addToWishlist(username, productId, quantity, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }

    @Test
    void shouldNotAddToWishListProductNotFound(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        String productId = "1234";
        Integer quantity = 1;
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.addToWishlist(username, productId, quantity, auth_code))
                .isInstanceOf(ProductDoesNotExistException.class);
    }

    @Test
    void shouldRemoveFromWishlist(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        List<UserProductsWishlist> wishlist = new LinkedList<>();
        User user = new User(username, name, surname, email, password, auth_code);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productId, productName, manufacturer);
        Integer quantity = 1;
        UserProductsWishlist item = new UserProductsWishlist(user, product, quantity);
        UserProductsWishlistPK pk = new UserProductsWishlistPK(username, productId);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(repository.findById(pk))
                .willReturn(Optional.of(item));
        //then
        assertDoesNotThrow(() -> service.removeFromWishlist(username, productId, auth_code));
    }

    @Test
    void shouldNotDeleteFromWishListUserNotFound(){
        //given
        String username = "username";
        String productId = "Product";
        String auth_code = "secret";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.removeFromWishlist(username, productId, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotDeleteFromWishListUserFailedAuth(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_fake = "fake";
        User user = new User(username, name, surname, email, password, auth_code);
        String productId = "1234";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.removeFromWishlist(username, productId, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }

    @Test
    void shouldNotRemoveFromWishListProductNotFound(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        String productId = "1234";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.removeFromWishlist(username, productId, auth_code))
                .isInstanceOf(ProductDoesNotExistException.class);
    }
    @Test
    void shouldNotRemoveFromWishListWishlistNotFound(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productId = "1234";
        String productName = "Product";
        Product product = new Product(productId, productName, manufacturer);
        Integer quantity = 1;
        UserProductsWishlistPK pk = new UserProductsWishlistPK(username, productId);
        String productIdPK = pk.getProductId();
        String userIdPK = pk.getUsername();
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(repository.findById(pk))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.removeFromWishlist(username, productId, auth_code))
                .isInstanceOf(WishlistItemDoesNotExistException.class);
    }

    @Test
    void shouldClearWishlist(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(repository.findAllByUser(user))
                .willReturn(Collections.EMPTY_LIST);
        //then
        assertDoesNotThrow(() -> service.clearWishlist(username, auth_code));
    }

    @Test
    void shouldNotClearWishlistUserNotFound(){
        //given
        String username = "username";
        String auth_code = "secret";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.clearWishlist(username, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotClearWishlistUserFailedToAuthenticate(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_fake = "fake";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.clearWishlist(username, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }
}
