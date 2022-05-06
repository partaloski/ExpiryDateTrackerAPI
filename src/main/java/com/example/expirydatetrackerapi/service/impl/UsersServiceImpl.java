package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.PasswordsDoNotMatchException;
import com.example.expirydatetrackerapi.models.exceptions.UserWithEmailAlreadyExists;
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
    public boolean login(String username, String password) {
        Optional<User> user = usersRepository.findUserByUsernameAndPassword(username, password);
        return user.isPresent();
    }

    @Override
    public User register(String username, String password, String confirmPassword, String name, String surname, String email) {
        if(usersRepository.findById(username).isPresent())
            throw new UserWithEmailAlreadyExists(email);
        if(password.equals(confirmPassword)){
            User user = new User(username, name, surname, email, password);
            return usersRepository.save(user);
        }
        else{
            throw new PasswordsDoNotMatchException();
        }
    }

    @Override
    public List<UserProductsWishlistDTO> getWishlistForUser(String username) {
        User user = usersRepository.findById(username).orElse(null);
        if(user == null){
            return null;
        }
        else{
            if(user.getProductsWishlist().size() == 0)
                return new ArrayList<>();
            else{
                Collection<UserProductsWishlist> wishlist = user.getProductsWishlist();
                List<UserProductsWishlistDTO> wishlistDTOS = new ArrayList<>();
                for(UserProductsWishlist wl: wishlist){
                    wishlistDTOS.add(UserProductsWishlistDTO.createExpiryOf(wl));
                }
                return wishlistDTOS;
            }
        }
    }

    @Override
    public List<UserProductsExpiryDTO> getExpiryListForUser(String username) {
        User user = usersRepository.findById(username).orElse(null);
        if(user == null){
            return null;
        }
        else{
            if(user.getProductsExpiries().size() == 0)
                return new ArrayList<>();
            else{
                Collection<UserProductsExpiry> expiries = user.getProductsExpiries();
                List<UserProductsExpiryDTO> expiryDTOS = new ArrayList<>();
                for(UserProductsExpiry e: expiries){
                    expiryDTOS.add(UserProductsExpiryDTO.createExpiryOf(e));
                }
                return expiryDTOS;
            }
        }
    }
}
