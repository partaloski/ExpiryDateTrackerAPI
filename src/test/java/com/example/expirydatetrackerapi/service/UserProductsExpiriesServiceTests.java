package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductExpiryRepository;
import com.example.expirydatetrackerapi.repository.UserProductWishlistRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.impl.UserProductExpiryServiceImpl;
import com.example.expirydatetrackerapi.service.impl.UserProductWishlistServiceImpl;
import com.example.expirydatetrackerapi.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserProductsExpiriesServiceTests {
    @Mock
    private UserProductExpiryRepository repository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ProductRepository productRepository;
    private UsersService userService;
    private UserProductExpiryService service;

    @BeforeEach
    void setUp(){
        userService = new UsersServiceImpl(usersRepository);
        service = new UserProductExpiryServiceImpl(repository, usersRepository, productRepository, userService);
    }

    @Test
    void shouldAddToExpiryList(){
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
        LocalDate date = LocalDate.now();
        UserProductsExpiry item = new UserProductsExpiry(user, product, date);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(repository.save(item))
                .willReturn(item);
        //then
        UserProductsExpiryDTO dto = service.addExpiry(username, productId, date, auth_code);
        assertThat(dto.getExpiryDate()).isEqualTo(date);
        assertThat(dto.getProduct().getProductId()).isEqualTo(productId);
        assertThat(dto.getProduct().getName()).isEqualTo(productName);
        assertThat(dto.getProduct().getManufacturer().getId()).isEqualTo(manufacturerId);
        assertThat(dto.getProduct().getManufacturer().getName()).isEqualTo(manufacturerName);
        assertThat(dto.getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void shouldNotAddToExpiryListUserNotFound(){
        //given
        String username = "username";
        String productId = "Product";
        LocalDate date = LocalDate.now();
        String auth_code = "secret";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.addExpiry(username, productId, date, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotAddToExpiryListUserFailedAuth(){
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
        LocalDate date = LocalDate.now();
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.addExpiry(username, productId, date, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }

    @Test
    void shouldNotAddToExpiryListProductNotFound(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        String productId = "1234";
        LocalDate date = LocalDate.now();
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.addExpiry(username, productId, date, auth_code))
                .isInstanceOf(ProductDoesNotExistException.class);
    }

    @Test
    void shouldRemoveFromExpirylist(){
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
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productId, productName, manufacturer);
        Integer id = 101;
        LocalDate date = LocalDate.now();
        UserProductsExpiry item = new UserProductsExpiry(id, user, product, date);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(repository.findById(id))
                .willReturn(Optional.of(item));
        //then
        assertDoesNotThrow(() -> service.deleteExpiry(id, auth_code));
    }

    @Test
    void shouldNotDeleteFromExpiryListUserNotFound(){
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
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productId, productName, manufacturer);
        Integer id = 101;
        LocalDate date = LocalDate.now();
        UserProductsExpiry item = new UserProductsExpiry(id, user, product, date);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        given(repository.findById(id))
                .willReturn(Optional.of(item));
        //then
        assertThatThrownBy(() -> service.deleteExpiry(id, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotDeleteFromExpiryListUserFailedAuth(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_fake = "fake";
        User user = new User(username, name, surname, email, password, auth_code);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productId, productName, manufacturer);
        Integer id = 101;
        LocalDate date = LocalDate.now();
        UserProductsExpiry item = new UserProductsExpiry(id, user, product, date);
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.of(user));
        given(repository.findById(id))
                .willReturn(Optional.of(item));
        //then
        assertThatThrownBy(() -> service.deleteExpiry(id, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }
    @Test
    void shouldNotRemoveFromExpiryListWishlistNotFound(){
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
        Integer id = 101;
        LocalDate date = LocalDate.now();
        UserProductsExpiry item = new UserProductsExpiry(id, user, product, date);
        //when
        given(repository.findById(id))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.deleteExpiry(id, auth_code))
                .isInstanceOf(UserExpiryDoesNotExistException.class);
    }

    @Test
    void shouldClearExpiryList(){
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
        assertDoesNotThrow(() -> service.clearExpiryList(username, auth_code));
    }

    @Test
    void shouldNotClearExpirylistUserNotFound(){
        //given
        String username = "username";
        String auth_code = "secret";
        //when
        given(usersRepository.findById(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.clearExpiryList(username, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldNotClearExpiryListUserFailedToAuthenticate(){
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
        assertThatThrownBy(() -> service.clearExpiryList(username, auth_code_fake))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }
}
