package com.wjl.englishreadingassistant.redis.service.impl;



import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjl.englishreadingassistant.redis.service.RedisService;
import com.wjl.englishreadingassistant.redis.statistics.CacheMonitor;
import com.wjl.englishreadingassistant.redis.statistics.CacheStatistics;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final CacheStatistics cacheStatistics;
    private final CacheMonitor cacheMonitor;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, CacheStatistics cacheStatistics, CacheMonitor cacheMonitor) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheStatistics = cacheStatistics;
        this.cacheMonitor = cacheMonitor;
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
        //Start timing
        long start = System.currentTimeMillis();

        Object value =
                redisTemplate.opsForValue().get(key);
        //Time consumed by redis query
        long cost =
                System.currentTimeMillis() - start;

        //Cache miss
        if(value == null){
            cacheStatistics.recordMiss();

            cacheMonitor.print(key,
                               false,
                                cost,
                                cacheStatistics
                    );

            return null;
        }

        //Cache hit
        cacheStatistics.recordHit();

        cacheMonitor.print(
                key,
                true,
                cost,
                cacheStatistics
        );

        return objectMapper.convertValue(value,clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeReference) {
        // Start timing
        long start = System.currentTimeMillis();

        Object value = redisTemplate.opsForValue().get(key);

        // Time consumed by Redis query
        long cost = System.currentTimeMillis() - start;

        // Cache miss
        if (value == null) {

            cacheStatistics.recordMiss();

            cacheMonitor.print(
                    key,
                    false,
                    cost,
                    cacheStatistics
            );

            return null;
        }

        // cache hit
        cacheStatistics.recordHit();

        cacheMonitor.print(
                key,
                true,
                cost,
                cacheStatistics
        );

        JavaType javaType =
                objectMapper.getTypeFactory()
                        .constructType(typeReference.getType());

        return objectMapper.convertValue(value, javaType);


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
