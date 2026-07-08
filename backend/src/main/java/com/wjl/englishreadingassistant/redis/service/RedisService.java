package com.wjl.englishreadingassistant.redis.service;

import com.alibaba.fastjson2.TypeReference;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface RedisService {
    /*
    * Save data to cache without expiration time
    * */
    <T> void set(String key,T value);

    /*
    * Save data with specified expiration time
    * */
    <T> void set(String key, T value, Duration duration);


    /*
    * Retrieve cached data
    * */
    <T> T get(String key,Class<T> clazz);

    <T> T get(String key, TypeReference<T> typeReference);

    /*
    * Delete cache entry
    * */
    void delete(String key);

    /*
    * Check whether the specified key exists
    * */
    boolean exists(String key);
}
