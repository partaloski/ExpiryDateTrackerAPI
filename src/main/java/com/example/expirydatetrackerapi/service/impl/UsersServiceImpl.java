package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.common.LoggerStringsContainer;
import com.example.expirydatetrackerapi.helpers.ValidationMethods;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.dto.UserProductsExpiryDTO;
import com.example.expirydatetrackerapi.models.dto.UserProductsWishlistDTO;
import com.example.expirydatetrackerapi.models.exceptions.*;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import com.example.expirydatetrackerapi.repository.UsersRepository;
import com.example.expirydatetrackerapi.service.UsersService;
import com.example.expirydatetrackerapi.utils.RedisUtility;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.expirydatetrackerapi.common.LoggerStringsContainer.CACHE_LOOKUP_FAILED_MESSAGE;
import static com.example.expirydatetrackerapi.common.LoggerStringsContainer.CACHE_UPDATE_FAILED_MESSAGE;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final Gson gson;
    private final RedisUtility redisUtility;
    private final String REDIS_KEY_EXPIRIES = "EXPIRIES_";
    private final String REDIS_KEY_WISHLIST = "WISHLIST_";
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

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

        if(!this.authenticate(username, auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }

        String cacheKey = generateWishlistCacheKey(username);
        String wishlistJSON = null;

        try{
            wishlistJSON = redisUtility.getValue(cacheKey);
        }
        catch (Exception e){
            logger.error(CACHE_LOOKUP_FAILED_MESSAGE);
        }

        Collection<UserProductsWishlist> wishlist;

        if(isNull(wishlistJSON)){
            wishlist = user.getProductsWishlist();

            try{
                redisUtility.setValue(cacheKey, wishlist);
            }
            catch (Exception e){
                logger.error(CACHE_UPDATE_FAILED_MESSAGE);
            }
        }
        else{
            wishlist = gson.fromJson(wishlistJSON, Collection.class);
        }

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

        if(!this.authenticate(username, auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }

        String cacheKey = generateExpiryCacheKey(username);
        String expiriesJSON = null;

        try{
            expiriesJSON = redisUtility.getValue(cacheKey);
        }
        catch (Exception e){
            logger.error(CACHE_LOOKUP_FAILED_MESSAGE);
        }


        Collection<UserProductsExpiry> expiries;

        if(isNull(expiriesJSON)){
            expiries = user.getProductsExpiries();
            try{
                redisUtility.setValue(cacheKey, expiries);
            }
            catch (Exception e){
                logger.error(CACHE_UPDATE_FAILED_MESSAGE);
            }
        }
        else{
            expiries = gson.fromJson(expiriesJSON, Collection.class);
        }

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

    private String generateExpiryCacheKey(String username){
        return String.format("%s%s", REDIS_KEY_EXPIRIES, username);
    }

    private String generateWishlistCacheKey(String username){
        return String.format("%s%s", REDIS_KEY_WISHLIST, username);
    }
}
