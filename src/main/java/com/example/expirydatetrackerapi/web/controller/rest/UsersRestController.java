package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.*;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    private final UsersService usersService;

    public UsersRestController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    private ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password){
        try{
            User user = usersService.login(username, password);
            return new ResponseEntity<>(UserAuthenticationDTO.createAuthOf(user), HttpStatus.OK);
        }
        catch (UserLoginFailedException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/register")
    private ResponseEntity<String> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String name,
            @RequestParam(required = false) String surname,
            @RequestParam String email){
        try{
            usersService.register(username, password, confirmPassword, name, surname, email);
            return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
        }
        catch (PasswordsDoNotMatchException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UserWithUsernameAlreadyExistsException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{username}/wishlist")
    private ResponseEntity<Object> wishlistProductsForUser(@PathVariable String username, @RequestParam String auth_code){
        try{
            UserWishlistsDTO wishlist = UserWishlistsDTO.createOf(usersService.getWishlistForUser(username,auth_code));
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        }
        catch(UserFailedToAuthenticateException | UserWithUsernameDoesNotExistException fa){
            return new ResponseEntity<>(fa.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/expiryList")
    private ResponseEntity<Object> expiryListForUser(@PathVariable String username, @RequestParam String auth_code){
        try{
            UserExpiriesDTO expiries = UserExpiriesDTO.createOf(usersService.getExpiryListForUser(username,auth_code));
            return new ResponseEntity<>(expiries, HttpStatus.OK);
        }
        catch(UserFailedToAuthenticateException | UserWithUsernameDoesNotExistException fa){
            return new ResponseEntity<>(fa.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
