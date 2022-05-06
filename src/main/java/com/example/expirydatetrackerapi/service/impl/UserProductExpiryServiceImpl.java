package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.helpers.DateParser;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductExpiryRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UserProductExpiryService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserProductExpiryServiceImpl implements UserProductExpiryService {
    private final UserProductExpiryRepository repository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;


    public UserProductExpiryServiceImpl(UserProductExpiryRepository repository, UsersRepository usersRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<UserProductsExpiry> getExpiryListForUser(String username) {
        User user = usersRepository.findById(username).orElseThrow(() -> new RuntimeException());
        return repository.findAllByUser(user);
    }

    @Override
    public UserProductsExpiry addExpiry(String username, Integer productId, LocalDate expiryDate) {
        User user = usersRepository.findById(username).orElse(null);
        user.setProductsExpiries(null);
        user.setProductsWishlist(null);
        Product product = productRepository.findById(productId).orElse(null);
        if(user == null || product == null)
            return null;
        UserProductsExpiry userProductsExpiry = new UserProductsExpiry(user, product, expiryDate);
        repository.save(userProductsExpiry);
        User user2 = userProductsExpiry.getUser();
        user2.setPassword(null);
        user2.setProductsExpiries(null);
        user2.setProductsWishlist(null);
        userProductsExpiry.setUser(user2);
        return userProductsExpiry;
    }

    @Override
    public void clearExpiryList(String username) {
        User user = usersRepository.getById(username);
        List<UserProductsExpiry> list = repository.findAllByUser(user);
        repository.deleteAll(list);
    }

    @Override
    public void deleteExpiry(Integer id) {
        UserProductsExpiry userProductsExpiry = repository.findById(id).orElseThrow(() -> new RuntimeException());
        repository.delete(userProductsExpiry);
    }


}
