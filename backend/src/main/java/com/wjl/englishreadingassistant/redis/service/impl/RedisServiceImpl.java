package com.wjl.englishreadingassistant.redis.service.impl;



import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjl.englishreadingassistant.redis.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;


    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key,value);

    }

    @Override
    public <T> void set(String key, T value, Duration duration) {
        redisTemplate.opsForValue().set(key,value,duration);

    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return null;
        }
        return objectMapper.convertValue(value,clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeReference) {
        Object value =
                redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        JavaType javaType =
                objectMapper.getTypeFactory()
                        .constructType(typeReference.getType());

        return objectMapper.convertValue(
                value,
                javaType
        );


    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {

        Boolean result = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(result);
    }
}
