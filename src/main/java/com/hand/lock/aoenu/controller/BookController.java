package com.hand.lock.aoenu.controller;

import com.hand.lock.aoenu.annotation.RedisLock;
import com.hand.lock.aoenu.annotation.RedisLockKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BookController
 *
 * @author Levin
 * @since 2018/6/06 0031
 */
@RestController
@RequestMapping("/books")
public class BookController {

    @RedisLock(lockKey = "books")
    @GetMapping
    public String query(@RedisLockKey(order = 0) @RequestParam String token) {
        return "success -- " + token;
    }

}
