package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class TestController {
    @Autowired
    private RedisTestService redisTestService;

    // Endpoint để lấy dữ liệu từ cache
    @GetMapping("/cache")
    public String getCache(@RequestParam String key) {
        return redisTestService.getFromCache(key);
    }
}
