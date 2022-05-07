package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.exceptions.ProductDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.UserExpiryDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.UserFailedToAuthenticateException;
import com.example.expirydatetrackerapi.models.exceptions.UserWithUsernameDoesNotExistException;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.repository.UserProductExpiryRepository;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UserProductExpiryService;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserProductExpiryServiceImpl implements UserProductExpiryService {
    private final UserProductExpiryRepository repository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final UsersService userService;

    public UserProductExpiryServiceImpl(UserProductExpiryRepository repository, UsersRepository usersRepository, ProductRepository productRepository, UsersService userService) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public UserProductsExpiryDTO addExpiry(String username, Integer productId, LocalDate expiryDate, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductDoesNotExistException((productId)));
        UserProductsExpiry userProductsExpiry = new UserProductsExpiry(user, product, expiryDate);
        repository.save(userProductsExpiry);
        return UserProductsExpiryDTO.createExpiryOf(userProductsExpiry);
    }

    @Override
    public void clearExpiryList(String username, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        List<UserProductsExpiry> list = repository.findAllByUser(user);
        repository.deleteAll(list);
    }

    @Override
    public void deleteExpiry(Integer id, String auth_code) {
        UserProductsExpiry userProductsExpiry = repository.findById(id).orElseThrow(() -> new UserExpiryDoesNotExistException(id));
        String username = userProductsExpiry.getUser().getUsername();
        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }
        repository.delete(userProductsExpiry);
    }


}
