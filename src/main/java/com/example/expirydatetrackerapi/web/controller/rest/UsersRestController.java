package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.PasswordsDoNotMatchException;
import com.example.expirydatetrackerapi.models.exceptions.UserWithEmailAlreadyExists;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    private final UsersService usersService;

    public UsersRestController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    private ResponseEntity<String> login(@RequestParam String username, @RequestParam String password){
        User user = usersService.login(username, password);
        if (user != null)
            return new ResponseEntity<>(user.getAuth_code(), HttpStatus.OK);
        else
            return new ResponseEntity<>("Login failed, user with username and password not found", HttpStatus.FORBIDDEN);

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
            User user = usersService.register(username, password, confirmPassword, name, surname, email);
            return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
        }
        catch (PasswordsDoNotMatchException pe){
            return new ResponseEntity<>("The passwords do not match.", HttpStatus.BAD_REQUEST);
        }
        catch (UserWithEmailAlreadyExists pe){
            return new ResponseEntity<>("User with username already exists.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{username}/wishlist")
    private ResponseEntity<List<UserProductsWishlistDTO>> wishlistProductsForUser(@PathVariable String username){
        List<UserProductsWishlistDTO> wishlist = usersService.getWishlistForUser(username);

        if(wishlist==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        }
    }

    @GetMapping("/{username}/expiryList")
    private ResponseEntity<List<UserProductsExpiryDTO>> expiryListForUser(@PathVariable String username){
        List<UserProductsExpiryDTO> expiries = usersService.getExpiryListForUser(username);
        if(expiries==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(expiries, HttpStatus.OK);
        }
    }
}
