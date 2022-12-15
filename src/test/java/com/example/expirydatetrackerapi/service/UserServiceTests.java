package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.impl.UsersServiceImpl;
import com.example.expirydatetrackerapi.utils.RedisUtility;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    private UsersService service;
    @Mock
    private UsersRepository repository;
    @Mock
    private RedisUtility redisUtility;
    private Gson gson;
    @BeforeEach
    void setUp(){
        service = new UsersServiceImpl(repository, gson, redisUtility);
    }

    @Test
    void shouldLogin(){
        //given
        String username = "username";
        String password = "password";
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //when
        given(repository.findUserByUsernameAndPassword(username, password))
                .willReturn(Optional.of(user));
        //then
        User loggedIn = service.login(username, password);
        assertThat(loggedIn.getUsername()).isEqualTo(username);
        assertThat(loggedIn.getPassword()).isEqualTo(password);
        assertThat(loggedIn.getAuth_code()).isEqualTo(authCode);
    }

    @Test
    void shouldNotLogin(){
        //given
        String username = "username";
        String password = "password";
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //when
        given(repository.findUserByUsernameAndPassword(username, password))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.login(username, password))
                .isInstanceOf(UserLoginFailedException.class);
    }

    @Test
    void shouldNotLoginUsernameEmpty(){
        //given
        String username = "";
        String password = "password";
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //then
        assertThatThrownBy(() -> service.login(username, password))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Username field cannot be empty.\"");
    }

    @Test
    void shouldNotLoginUsernameNull(){
        //given
        String username = null;
        String password = "password";
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //then
        assertThatThrownBy(() -> service.login(username, password))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Username field cannot be empty.\"");
    }

    @Test
    void shouldNotLoginPasswordEmpty(){
        //given
        String username = "username";
        String password = "";
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //then
        assertThatThrownBy(() -> service.login(username, password))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Password field cannot be empty.\"");
    }

    @Test
    void shouldNotLoginPasswordNull(){
        //given
        String username = "username";
        String password = null;
        String authCode = "secret";
        User user = new User(username, password, authCode);
        //then
        assertThatThrownBy(() -> service.login(username, password))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Password field cannot be empty.\"");
    }

    @Test
    void shouldRegister(){
        //given
        String username = "username";
        String password = "password";
        String confirmPassword = "password";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        User user = new User(username, name, surname, email, password);
        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());
        given(repository.save(Mockito.any())).willReturn(user);
        service.register(username, password, confirmPassword, name, surname, email);
        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getPassword()).isEqualTo(password);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getSurname()).isEqualTo(surname);
    }

    @Test
    void shouldNotRegisterPasswordsNotMatching(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        User user = new User(username, name, surname, email, password);
        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(PasswordsDoNotMatchException.class);
    }

    @Test
    void shouldNotRegisterUsernameAlreadyRegistered(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        User user = new User(username, name, surname, email, password);

        //when
        given(repository.findById(username))
                .willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(UserWithUsernameAlreadyExistsException.class);
    }

    @Test
    void shouldNotRegisterEmailNotValid(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test";
        User user = new User(username, name, surname, email, password);

        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"E-Mail is not valid.\"");
    }

    @Test
    void shouldNotRegisterEmailNotValidOther(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = "mailtest.com";
        User user = new User(username, name, surname, email, password);

        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"E-Mail is not valid.\"");
    }

    @Test
    void shouldNotRegisterEmailEmpty(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = "";
        User user = new User(username, name, surname, email, password);

        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"E-Mail is not valid.\"");
    }

    @Test
    void shouldNotRegisterEmailNull(){
        //given
        String username = "username";
        String password = "passwordOne";
        String confirmPassword = "passwordTwo";
        String name = "Name";
        String surname = "Surname";
        String email = null;
        User user = new User(username, name, surname, email, password);

        //when
        given(repository.findById(username))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.register(username, password, confirmPassword, name, surname, email))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"E-Mail is not valid.\"");
    }

    @Test
    void shouldAuthenticate(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertTrue(service.authenticate(username, auth_code));
    }

    @Test
    void shouldNotAuthenticate(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_false = "secretFalse";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertFalse(service.authenticate(username, auth_code_false));
    }

    @Test
    void shouldNotAuthenticateUserNotFound(){
        //given
        String username = "username";
        String auth_code = "secret";
        //when
        given(repository.findById(username)).willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.authenticate(username, auth_code))
                .isInstanceOf(UserWithUsernameDoesNotExistException.class);
    }

    @Test
    void shouldReturnExpiryList(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        List<UserProductsExpiry> expiries = new LinkedList<>();
        User user = new User(username, name, surname, email, password, auth_code);
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productName = "Product";
        String productId = "1234";
        Product product = new Product(productName, productId, manufacturer);
        UserProductsExpiry expiry = new UserProductsExpiry(user, product, LocalDate.now());
        expiries.add(expiry);
        user.setProductsExpiries(expiries);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertThat(service.getExpiryListForUser(username, auth_code).size()).isEqualTo(1);
    }

    @Test
    void shouldNotReturnExpiryListFailedAuth(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_false = "secretFalse";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.getExpiryListForUser(username, auth_code_false))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }

    @Test
    void shouldReturnWishList(){
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
        Product product = new Product(productName, productId, manufacturer);
        UserProductsWishlist item = new UserProductsWishlist(user, product, 1);
        wishlist.add(item);
        user.setProductsWishlist(wishlist);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertThat(service.getWishlistForUser(username, auth_code).size()).isEqualTo(1);
    }

    @Test
    void shouldNotReturnWishListFailedAuth(){
        //given
        String username = "username";
        String password = "passwordOne";
        String name = "Name";
        String surname = "Surname";
        String email = "mail@test.com";
        String auth_code = "secret";
        String auth_code_false = "secretFalse";
        User user = new User(username, name, surname, email, password, auth_code);
        //when
        given(repository.findById(username)).willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> service.getWishlistForUser(username, auth_code_false))
                .isInstanceOf(UserFailedToAuthenticateException.class);
    }
}
