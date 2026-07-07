package com.wjl.englishreadingassistant.service.redis;

import java.time.Duration;

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


    /*
    * Delete cache entry
    * */
    void delete(String key);

    /*
    * Check whether the specified key exists
    * */
    boolean exists(String key);
}
