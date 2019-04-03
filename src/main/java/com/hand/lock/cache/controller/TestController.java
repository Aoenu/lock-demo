package com.hand.lock.cache.controller;

import com.hand.lock.cache.annotation.LocalLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BookController
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @LocalLock(key = "test:arg[0]")
    @GetMapping
    public String query(@RequestParam String token) {
        return "success -- " + token;
    }

}
