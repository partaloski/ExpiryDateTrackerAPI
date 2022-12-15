package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.helpers.ValidationMethods;
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

import static java.util.Objects.isNull;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;


    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User login(String username, String password) {
        if(isNull(username) || username.isEmpty()){
            throw new NotValidException("Username field cannot be empty.");
        }

        if(isNull(password) || password.isEmpty()){
            throw new NotValidException("Password field cannot be empty.");
        }

        return usersRepository.findUserByUsernameAndPassword(username, password).orElseThrow(UserLoginFailedException::new);
    }

    @Override
    public User register(String username, String password, String confirmPassword, String name, String surname, String email) {
        if(usersRepository.findById(username).isPresent())
            throw new UserWithUsernameAlreadyExistsException(username);
        if(ValidationMethods.isEmailInvalid(email))
            throw new NotValidException("E-Mail is not valid.");
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
        Collection<UserProductsWishlist> wishlist = user.getProductsWishlist();
        List<UserProductsWishlistDTO> wishlistDTOS = new ArrayList<>();
        for(UserProductsWishlist wl: wishlist){
            wishlistDTOS.add(UserProductsWishlistDTO.createWishlistOf(wl));
        }
        return wishlistDTOS;
    }

    @Override
    public List<UserProductsExpiryDTO> getExpiryListForUser(String username, String auth_code) {
        User user = usersRepository.findById(username)
                .orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!this.authenticate(username, auth_code))
            throw new UserFailedToAuthenticateException(username);
        Collection<UserProductsExpiry> expiries = user.getProductsExpiries();
        List<UserProductsExpiryDTO> expiryDTOS = new ArrayList<>();
        for(UserProductsExpiry e: expiries){
            expiryDTOS.add(UserProductsExpiryDTO.createExpiryOf(e));
        }
        expiryDTOS.sort(UserProductsExpiryDTO.comparator);
        return expiryDTOS;
    }

    @Override
    public boolean authenticate(String username, String auth_code) {
        User user = usersRepository.findById(username)
                .orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));
        if(!user.getAuth_code().contentEquals(auth_code))
            return false;
        return true;
    }
}
