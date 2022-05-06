package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.models.User;
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
    public ResponseEntity<UserProductsExpiry> addExpiry (
            @RequestParam String username,
            @RequestParam Integer productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
        UserProductsExpiry userProductsExpiry = serviceExpiry.addExpiry(username, productId, expiryDate);
        if(userProductsExpiry == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return ResponseEntity.ok().body(userProductsExpiry);
    }

    @DeleteMapping("/delete/e")
    public ResponseEntity<String> removeExpiry (@RequestParam Integer id){
        try{
            serviceExpiry.deleteExpiry(id);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("Expiry item with ID " + id + " not found.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The Expiry item was removed.");
    }

    @DeleteMapping("/clear/e")
    public ResponseEntity<String> clearExpiries (@RequestParam String username){
        try{
            serviceExpiry.clearExpiryList(username);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The Expiry list for the user with username \"" + username + "\" was emptied out successfully.");
    }



    @PostMapping("/add/w")
    public ResponseEntity<UserProductsWishlist> addWishlist (@RequestParam String username, @RequestParam Integer productId, @RequestParam Integer quantity){
        UserProductsWishlist userProductsWishlist = serviceWishlist.addToWishlist(username, productId, quantity);
        if(userProductsWishlist == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return ResponseEntity.ok().body(userProductsWishlist);
    }

    @DeleteMapping("/delete/w")
    public ResponseEntity<String> removeWishlistItem (@RequestParam String username, @RequestParam Integer productId){
        try{
            serviceWishlist.removeFromWishlist(username, productId);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("Wishlist item not found.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("The wishlist item was removed.");
    }

    @DeleteMapping("/clear/w")
    public ResponseEntity<String> clearWishlist (@RequestParam String username){
        try{
            serviceWishlist.clearWishlist(username);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        String response = "The wishlist has been cleaned";
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/e/{username}")
    public ResponseEntity<List<UserProductsExpiry>> getExpiryListForUser(@PathVariable String username){
        try{
            List<UserProductsExpiry> listStart = serviceExpiry.getExpiryListForUser(username);
            List<UserProductsExpiry> list = new LinkedList<>();
            for(UserProductsExpiry ex: listStart){
                User user = ex.getUser();
                user.clearQuery();
                ex.setUser(user);
                list.add(ex);
            }
            return ResponseEntity.ok().body(list);
        }
        catch (RuntimeException ex){
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/w/{username}")
    public ResponseEntity<List<UserProductsWishlist>> getWishlistForUser(@PathVariable String username){
        try{
            List<UserProductsWishlist> listStart = serviceWishlist.getWishlistForUser(username);
            List<UserProductsWishlist> list = new LinkedList<>();
            for(UserProductsWishlist wi: listStart){
                User user = wi.getUser();
                user.clearQuery();
                wi.setUser(user);
                list.add(wi);
            }
            return ResponseEntity.ok().body(list);
        }
        catch (RuntimeException ex){
            return ResponseEntity.notFound().build();
        }
    }
}
