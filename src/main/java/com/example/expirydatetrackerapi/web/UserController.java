package com.example.expirydatetrackerapi.web;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.*;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationDTO> login(@RequestParam String username, @RequestParam String password){
        User user = usersService.login(username, password);
        return ResponseEntity.ok(UserAuthenticationDTO.createAuthOf(user));

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String name,
            @RequestParam(required = false) String surname,
            @RequestParam String email){
        usersService.register(username, password, confirmPassword, name, surname, email);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/{username}/wishlist")
    public ResponseEntity<UserWishlistsDTO> wishlistProductsForUser(@PathVariable String username, @RequestParam String auth_code){
        UserWishlistsDTO wishlist = UserWishlistsDTO.createOf(usersService.getWishlistForUser(username,auth_code));
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/{username}/expiryList")
    public ResponseEntity<UserExpiriesDTO> expiryListForUser(@PathVariable String username, @RequestParam String auth_code){
        UserExpiriesDTO expiries = UserExpiriesDTO.createOf(usersService.getExpiryListForUser(username,auth_code));
        return ResponseEntity.ok(expiries);
    }
}
