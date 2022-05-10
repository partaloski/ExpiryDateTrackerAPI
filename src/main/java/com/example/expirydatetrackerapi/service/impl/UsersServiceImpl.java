package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String username, String password) {
        return usersRepository.findUserByUsernameAndPassword(username, password).orElseThrow(() -> new UserLoginFailedException());
    }

    @Override
    public User register(String username, String password, String confirmPassword, String name, String surname, String email) {
        if(usersRepository.findById(username).isPresent())
            throw new UserWithUsernameAlreadyExistsException(email);
        if(password.equals(confirmPassword)){
            User user = new User(username, name, surname, email, password);
            return usersRepository.save(user);
        }
        else{
            throw new PasswordsDoNotMatchException();
        }
    }

    @Override
    public List<UserProductsWishlistDTO> getWishlistForUser(String username, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!this.authenticate(username, auth_code))
            throw new UserFailedToAuthenticateException(username);
        if(user.getProductsWishlist().size() == 0)
            return new ArrayList<>();
        else{
            Collection<UserProductsWishlist> wishlist = user.getProductsWishlist();
            List<UserProductsWishlistDTO> wishlistDTOS = new ArrayList<>();
            for(UserProductsWishlist wl: wishlist){
                wishlistDTOS.add(UserProductsWishlistDTO.createWishlistOf(wl));
            }
            return wishlistDTOS;
        }
    }

    @Override
    public List<UserProductsExpiryDTO> getExpiryListForUser(String username, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!this.authenticate(username, auth_code))
            throw new UserFailedToAuthenticateException(username);
        if(user.getProductsExpiries().size() == 0)
            return new ArrayList<>();
        else{
            Collection<UserProductsExpiry> expiries = user.getProductsExpiries();
            List<UserProductsExpiryDTO> expiryDTOS = new ArrayList<>();
            for(UserProductsExpiry e: expiries){
                expiryDTOS.add(UserProductsExpiryDTO.createExpiryOf(e));
            }
            expiryDTOS.sort(UserProductsExpiryDTO.comparator);
            return expiryDTOS;
        }
    }

    @Override
    public boolean authenticate(String username, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!user.getAuth_code().contentEquals(auth_code))
            return false;
        return true;
    }
}
