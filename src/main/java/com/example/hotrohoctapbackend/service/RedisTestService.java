package com.example.hotrohoctapbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "testCache", key = "#key")
    public String getFromCache(String key) {
        return "Cached value for key: " + key;
    }
}