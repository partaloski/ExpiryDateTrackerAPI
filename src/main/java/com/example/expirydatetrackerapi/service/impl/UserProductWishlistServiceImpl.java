package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.ProductDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.UserFailedToAuthenticateException;
import com.example.expirydatetrackerapi.models.exceptions.UserWithUsernameDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.WishlistItemDoesNotExistException;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductWishlistRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UserProductWishlistService;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductWishlistServiceImpl implements UserProductWishlistService {
    private final UserProductWishlistRepository repository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final UsersService userService;


    public UserProductWishlistServiceImpl(UserProductWishlistRepository repository, UsersRepository usersRepository, ProductRepository productRepository, UsersService userService) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }
    @Override
    public UserProductsWishlistDTO addToWishlist(String username, String productId, Integer quantity, String auth_code) {
        User user = usersRepository.findById(username)
                .orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductDoesNotExistException(productId));
        UserProductsWishlist userProductsWishlist = new UserProductsWishlist(user, product, quantity);
        repository.save(userProductsWishlist);
        return UserProductsWishlistDTO.createWishlistOf(userProductsWishlist);
    }

    @Override
    public void removeFromWishlist(String username, String productId, String auth_code) {
        usersRepository.findById(username)
                .orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductDoesNotExistException(productId));
        UserProductsWishlist userProductsWishlist = repository.findById(new UserProductsWishlistPK(username, productId))
                .orElseThrow(() -> new WishlistItemDoesNotExistException(username, productId));
        repository.delete(userProductsWishlist);
    }

    @Override
    public void clearWishlist(String username, String auth_code) {
        User user = usersRepository.findById(username)
                .orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        List<UserProductsWishlist> list = repository.findAllByUser(user);
        repository.deleteAll(list);
    }
}
