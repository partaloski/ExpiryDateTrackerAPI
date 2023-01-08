package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.NotValidException;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import com.example.expirydatetrackerapi.utils.RedisUtility;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.example.expirydatetrackerapi.common.LoggerStringsContainer.*;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {
    private final String REDIS_KEY = "manufacturers";
    private final ManufacturerRepository repository;
    private final RedisUtility redisUtility;
    private final Gson gson;
    private final Logger logger = LoggerFactory.getLogger(ManufacturerServiceImpl.class);


    @Override
    public List<Manufacturer> findAll() {
        String cacheResult = null;

        try{
            cacheResult = redisUtility.getValue(REDIS_KEY);
        }
        catch (Exception e){
            logger.error(CACHE_LOOKUP_FAILED_MESSAGE);
        }

        List<Manufacturer> manufacturers;

        if(isNull(cacheResult)){
            manufacturers = repository.findAll();

            try{
                redisUtility.setValue(REDIS_KEY, manufacturers);
            }
            catch (Exception e){
                logger.error(CACHE_UPDATE_FAILED_MESSAGE);
            }

            return manufacturers;
        }

        return gson.fromJson(cacheResult, List.class);
    }

    @Override
    public Manufacturer save(String name) {
        if(isNull(name) || name.isEmpty()){
            throw new NotValidException("Manufacturer's name cannot be empty.");
        }

        Manufacturer manufacturer = new Manufacturer(name);

        manufacturer = repository.save(manufacturer);

        invalidateCache();

        return manufacturer;
    }

    @Override
    public void delete(Integer id) {
        if(isNull(id) || id < 0){
            throw new NotValidException("ID cannot be null or smaller than 0.");
        }

        Manufacturer manufacturer = repository.findById(id).orElseThrow(() -> new ManufacturerDoesNotExistException(id));

        repository.delete(manufacturer);
        invalidateCache();
    }

    private void invalidateCache(){
        try{
            redisUtility.clearValue(REDIS_KEY);
        }
        catch (Exception e){
            logger.error(CACHE_INVALIDATION_FAILED_MESSAGE);
        }
    }
}
