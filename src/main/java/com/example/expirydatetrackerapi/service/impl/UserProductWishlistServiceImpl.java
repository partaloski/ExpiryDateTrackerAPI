package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductWishlistRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UserProductWishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductWishlistServiceImpl implements UserProductWishlistService {
    private final UserProductWishlistRepository repository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;


    public UserProductWishlistServiceImpl(UserProductWishlistRepository repository, UsersRepository usersRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<UserProductsWishlist> getWishlistForUser(String username) {
        User user = usersRepository.findById(username).orElseThrow(() -> new RuntimeException());
        return repository.findAllByUser(user);
    }

    @Override
    public UserProductsWishlist addToWishlist(String username, Integer productId, Integer quantity) {
        User user = usersRepository.findById(username).orElse(null);
        user.setProductsExpiries(null);
        user.setProductsWishlist(null);
        Product product = productRepository.findById(productId).orElse(null);
        if(user == null || product == null)
            return null;
        UserProductsWishlist userProductsWishlist = new UserProductsWishlist(user, product, quantity);
        repository.save(userProductsWishlist);
        User user2 = userProductsWishlist.getUser();
        user2.setPassword(null);
        user2.setProductsExpiries(null);
        user2.setProductsWishlist(null);
        userProductsWishlist.setUser(user2);
        return userProductsWishlist;
    }

    @Override
    public void removeFromWishlist(String username, Integer productId) {
        User user = usersRepository.findById(username).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if(user == null || product == null)
            throw new RuntimeException();
        UserProductsWishlist userProductsWishlist = repository.findById(new UserProductsWishlistPK(username, productId)).orElseThrow(() -> new RuntimeException());
        repository.delete(userProductsWishlist);
    }

    @Override
    public void clearWishlist(String username) {
        User user = usersRepository.getById(username);
        List<UserProductsWishlist> list = repository.findAllByUser(user);
        repository.deleteAll(list);
    }
}
