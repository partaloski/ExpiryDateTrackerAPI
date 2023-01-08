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
import com.example.expirydatetrackerapi.utils.RedisUtility;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.expirydatetrackerapi.common.LoggerStringsContainer.CACHE_INVALIDATION_FAILED_MESSAGE;

@Service
@AllArgsConstructor
public class UserProductExpiryServiceImpl implements UserProductExpiryService {
    private final UserProductExpiryRepository repository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final UsersService userService;
    private final RedisUtility redisUtility;
    private final String REDIS_KEY = "EXPIRIES_";
    private final Logger logger = LoggerFactory.getLogger(UserProductExpiryServiceImpl.class);

    @Override
    public UserProductsExpiryDTO addExpiry(String username, String productId, LocalDate expiryDate, String auth_code) {
        User user = usersRepository.findById(username).orElseThrow(() -> new UserWithUsernameDoesNotExistException(username));

        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductDoesNotExistException((productId)));
        UserProductsExpiry userProductsExpiry = new UserProductsExpiry(user, product, expiryDate);

        userProductsExpiry = repository.save(userProductsExpiry);

        invalidateCache(username);

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

        invalidateCache(username);
    }

    @Override
    public void deleteExpiry(Integer id, String auth_code) {
        UserProductsExpiry userProductsExpiry = repository.findById(id).orElseThrow(() -> new UserExpiryDoesNotExistException(id));

        String username = userProductsExpiry.getUser().getUsername();

        if(!userService.authenticate(username,auth_code)){
            throw new UserFailedToAuthenticateException(username);
        }

        repository.delete(userProductsExpiry);

        invalidateCache(username);
    }

    private String generateCacheKey(String username){
        return String.format("%s%s", REDIS_KEY, username);
    }

    private void invalidateCache(String username){
        try{
            redisUtility.clearValue(generateCacheKey(username));
        }
        catch (Exception e){
            logger.error(CACHE_INVALIDATION_FAILED_MESSAGE);
        }
    }
}
