package com.example.expirydatetrackerapi.utils;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisUtility {
    private RedisTemplate<String, String> redisTemplate;
    Gson gson;

    public void setValue(final String key, Object object){
        redisTemplate.opsForValue().set(key, gson.toJson(object));
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
    }

    public String getValue(final String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void clearValue(final String key){
        redisTemplate.delete(key);
    }
}
