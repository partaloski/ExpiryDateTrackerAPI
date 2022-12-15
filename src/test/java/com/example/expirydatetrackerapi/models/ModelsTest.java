package com.example.expirydatetrackerapi.models;

import com.example.expirydatetrackerapi.ExpiryDateTrackerApiApplication;
import com.example.expirydatetrackerapi.models.dto.*;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ModelsTest {
    @Test
    void testManufacturersDTO(){
        ManufacturersDTO dto = new ManufacturersDTO();

        dto.setManufacturers(Collections.emptyList());
        dto.getManufacturers();
    }

    @Test
    void testManufacturer(){
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setId(null);
        manufacturer.getId();

        manufacturer.setName(null);
        manufacturer.getName();
    }

    @Test
    void testProductsDTO(){
        ProductsDTO dto = new ProductsDTO();

        dto.setProducts(Collections.emptyList());
        dto.getProducts();
    }

    @Test
    void testProduct(){
        Product product = new Product();

        product.getProductId();
        product.setProductId(null);

        product.getName();
        product.setName(null);

        product.getManufacturer();
        product.setManufacturer(null);
    }

    @Test
    void testAuthDTO(){
        UserAuthenticationDTO dto = new UserAuthenticationDTO();

        dto.setAuth_code("");
        dto.getAuth_code();
    }

    @Test
    void testUserDTO(){
        UserDTO dto = new UserDTO();

        dto.setName("");
        dto.setSurname("");
        dto.setEmail("");
        dto.getName();
        dto.getSurname();
        dto.getEmail();
    }

    @Test
    void testUserExpiriesDTO(){
        UserExpiriesDTO dto = new UserExpiriesDTO();

        dto.setExpiries(Collections.emptyList());
        dto.getExpiries();
    }

    @Test
    void testUserWishlistDTO(){
        UserWishlistsDTO dto = new UserWishlistsDTO();

        dto.setWishlist(Collections.emptyList());
        dto.getWishlist();
    }

    @Test
    void testUserProductsExpiryDTO(){
        UserProductsExpiryDTO dto = new UserProductsExpiryDTO();

        dto.setProduct(null);
        dto.getProduct();

        dto.setId(null);
        dto.getId();

        dto.setUser(null);
        dto.getUser();

        dto.setExpiryDate(null);
        dto.getExpiryDate();
    }

    @Test
    void testUserProductsWishlistDTO(){
        UserProductsWishlistDTO dto = new UserProductsWishlistDTO();

        dto.setUser(null);
        dto.getUser();

        dto.setProduct(null);
        dto.getProduct();

        dto.setQuantity(null);
        dto.getQuantity();
    }

    @Test
    void testUserPK(){
        UserProductsWishlistPK pk = new UserProductsWishlistPK();

        pk.setProductId(null);
        pk.getProductId();

        pk.setUsername(null);
        pk.getUsername();
    }

    @Test
    void testRelationsExpiry(){
        UserProductsExpiry expiry = new UserProductsExpiry();
        expiry.setID(null);
        expiry.getID();

        expiry.setUser(null);
        expiry.getUser();

        expiry.setProduct(null);
        expiry.getProduct();

        expiry.setExpirydate(null);
        expiry.getExpirydate();
    }

    @Test
    void testRelationsWishlist(){
        UserProductsWishlist wishlist = new UserProductsWishlist();

        wishlist.setId(null);
        wishlist.getId();

        wishlist.setUser(null);
        wishlist.getUser();

        wishlist.setProduct(null);
        wishlist.getProduct();

        wishlist.setQuantity(null);
        wishlist.getQuantity();
    }

    @Test
    void testUser(){
        User user = new User();
    }
}
