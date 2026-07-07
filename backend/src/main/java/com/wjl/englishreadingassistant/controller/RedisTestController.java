package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Book;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test")
    public String test() {

        redisTemplate.opsForValue().set("hello", "world");

        Object value = redisTemplate.opsForValue().get("hello");

        return value.toString();
    }

}
