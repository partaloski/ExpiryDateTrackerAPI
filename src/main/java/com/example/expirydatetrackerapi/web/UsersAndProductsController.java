package com.example.expirydatetrackerapi.web;

import com.example.expirydatetrackerapi.helpers.DateParser;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.UserFailedToAuthenticateException;
import com.example.expirydatetrackerapi.service.UserProductExpiryService;
import com.example.expirydatetrackerapi.service.UserProductWishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/products-users")
public class UsersAndProductsController {
    private final UserProductExpiryService serviceExpiry;
    private final UserProductWishlistService serviceWishlist;

    public UsersAndProductsController(UserProductExpiryService serviceExpiry, UserProductWishlistService serviceWishlist) {
        this.serviceExpiry = serviceExpiry;
        this.serviceWishlist = serviceWishlist;
    }

    @PostMapping("/add/e")
    public ResponseEntity<UserProductsExpiryDTO> addExpiry (
            @RequestParam String username,
            @RequestParam String productId,
            @RequestParam String expiryDate,
            @RequestParam String auth_code){
        LocalDate date = DateParser.parseDate(expiryDate);
        UserProductsExpiryDTO userProductsExpiryDTO = serviceExpiry.addExpiry(username, productId, date, auth_code);
        return ResponseEntity.ok(userProductsExpiryDTO);
    }

    @DeleteMapping("/delete/e")
    public ResponseEntity<String> removeExpiry (@RequestParam Integer id, @RequestParam String auth_code){
        serviceExpiry.deleteExpiry(id, auth_code);
        return ResponseEntity.ok().body("The Expiry item was removed.");
    }

    @DeleteMapping("{username}/clear/e")
    public ResponseEntity<String> clearExpiries (@PathVariable String username, @RequestParam String auth_code){
        serviceExpiry.clearExpiryList(username, auth_code);
        return ResponseEntity.ok("The Expiry list for the user with username \"" + username + "\" was emptied out successfully.");
    }

    @PostMapping("/add/w")
    public ResponseEntity<UserProductsWishlistDTO> addWishlist (@RequestParam String username, @RequestParam String productId, @RequestParam Integer quantity, @RequestParam String auth_code){
        UserProductsWishlistDTO userProductsWishlistDTO = serviceWishlist.addToWishlist(username, productId, quantity, auth_code);
        return ResponseEntity.ok().body(userProductsWishlistDTO);
    }

    @DeleteMapping("/delete/w")
    public ResponseEntity<String> removeWishlistItem (@RequestParam String username, @RequestParam String productId, @RequestParam String auth_code){
        serviceWishlist.removeFromWishlist(username, productId, auth_code);
        return ResponseEntity.ok().body("The wishlist item was removed.");
    }

    @DeleteMapping("{username}/clear/w")
    public ResponseEntity<String> clearWishlist (@PathVariable String username, @RequestParam String auth_code){
        serviceWishlist.clearWishlist(username, auth_code);
        return ResponseEntity.ok("The wishlist has been cleaned");
    }
}
