package com.naah69.demo;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author naah
 * @date 2019-07-27 1:13 PM
 * @desc
 */
@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("/set/{key}/{value}")
    public void testSetRedisson(@PathVariable("key") String key, @PathVariable("value") String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @GetMapping("/get/{key}")
    public String testGetRedisson(@PathVariable("key") String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
