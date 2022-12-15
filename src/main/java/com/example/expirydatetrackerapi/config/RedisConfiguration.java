package com.example.expirydatetrackerapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableCaching
@Profile("!test")
public class RedisConfiguration {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        final RedisTemplate<String, String>  redisTemplate = new RedisTemplate<String, String>();

        redisTemplate
                .setKeySerializer(new StringRedisSerializer());

        redisTemplate
                .setHashKeySerializer(
                        new GenericToStringSerializer<String>(String.class)
                );

        redisTemplate.setHashValueSerializer(
                new JdkSerializationRedisSerializer()
        );

        redisTemplate.setValueSerializer(
                new JdkSerializationRedisSerializer()
        );

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

        JedisClientConfiguration jedisClientConfiguration =
                JedisClientConfiguration.builder().build();

        JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jedisClientConfiguration);

        factory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }
}
