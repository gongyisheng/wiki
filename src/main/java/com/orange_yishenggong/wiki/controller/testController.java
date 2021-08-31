package com.orange_yishenggong.wiki.controller;

import com.orange_yishenggong.wiki.domain.test;
import com.orange_yishenggong.wiki.service.testService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class testController {

    @Resource
    private testService testService;

    @Resource
    private RedisTemplate redisTemplate;

    private static final Logger Log = LoggerFactory.getLogger(testController.class);
    //http://127.0.0.1:8080/hello
    //@PostMapping("/hello")  405 error,has the path but method not allowed
    //http://127.0.0.1:8080/hello2  404 error,do not have the path
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World";
    }
    @PostMapping("/hello/post")
    public String hello(String name){
        return "Hello World"+name;
    }
    @GetMapping("/list")
    public List<test> list(){
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        Log.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        Log.info("key: {}, value: {}", key, object);
        return object;
    }
}
