package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.helpers.DateParser;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.UserFailedToAuthenticateException;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.service.UserProductExpiryService;
import com.example.expirydatetrackerapi.service.UserProductWishlistService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/productsUsers")
public class UsersAndProductsRestController {
    private final UserProductExpiryService serviceExpiry;
    private final UserProductWishlistService serviceWishlist;

    public UsersAndProductsRestController(UserProductExpiryService serviceExpiry, UserProductWishlistService serviceWishlist) {
        this.serviceExpiry = serviceExpiry;
        this.serviceWishlist = serviceWishlist;
    }

    @PostMapping("/add/e")
    public ResponseEntity<Object> addExpiry (
            @RequestParam String username,
            @RequestParam String productId,
            @RequestParam String expiryDate,
            @RequestParam String auth_code){
        try{
            LocalDate date = DateParser.parseDate(expiryDate);
            UserProductsExpiryDTO userProductsExpiryDTO = serviceExpiry.addExpiry(username, productId, date, auth_code);
            if(userProductsExpiryDTO == null){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            else
                return ResponseEntity.ok().body(userProductsExpiryDTO);
        }
        catch (UserFailedToAuthenticateException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/e")
    public ResponseEntity<String> removeExpiry (@RequestParam Integer id, @RequestParam String auth_code){
        try{
            serviceExpiry.deleteExpiry(id, auth_code);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The Expiry item was removed.");
    }

    @DeleteMapping("{username}/clear/e")
    public ResponseEntity<String> clearExpiries (@PathVariable String username, @RequestParam String auth_code){
        try{
            serviceExpiry.clearExpiryList(username, auth_code);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The Expiry list for the user with username \"" + username + "\" was emptied out successfully.");
    }



    @PostMapping("/add/w")
    public ResponseEntity<UserProductsWishlistDTO> addWishlist (@RequestParam String username, @RequestParam String productId, @RequestParam Integer quantity, @RequestParam String auth_code){
        UserProductsWishlistDTO userProductsWishlistDTO = serviceWishlist.addToWishlist(username, productId, quantity, auth_code);
        if(userProductsWishlistDTO == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return ResponseEntity.ok().body(userProductsWishlistDTO);
    }

    @DeleteMapping("/delete/w")
    public ResponseEntity<String> removeWishlistItem (@RequestParam String username, @RequestParam String productId, @RequestParam String auth_code){
        try{
            serviceWishlist.removeFromWishlist(username, productId, auth_code);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("Wishlist item not found.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The wishlist item was removed.");
    }

    @DeleteMapping("{username}/clear/w")
    public ResponseEntity<String> clearWishlist (@PathVariable String username, @RequestParam String auth_code){
        try{
            serviceWishlist.clearWishlist(username, auth_code);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        String response = "The wishlist has been cleaned";
        return ResponseEntity.ok().body(response);
    }
}
