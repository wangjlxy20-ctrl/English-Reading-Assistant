package com.wjl.englishreadingassistant.service.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class RedisServiceImpl implements RedisService{

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void set(String key, T value) {

    }

    @Override
    public <T> void set(String key, T value, Duration duration) {

    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }
}
