package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminTestUpdateDTO;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.service.RedisTestService;
import com.example.hotrohoctapbackend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private RedisTestService redisTestService;
    @Autowired
    private TestService testService;
    // Endpoint để lấy dữ liệu từ cache
    @GetMapping("/cache")
    public String getCache(@RequestParam String key) {
        return redisTestService.getFromCache(key);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable int id, @RequestBody AdminTestUpdateDTO updateDTO) {
        Test updatedTest = testService.updateTest(id, updateDTO);
        return ResponseEntity.ok(updatedTest);
    }
    @GetMapping("/chitiet/{id}")
    public ResponseEntity<AdminTestUpdateDTO> getTestById(@PathVariable int id) {
        AdminTestUpdateDTO responseDTO = testService.getTestById(id);
        return ResponseEntity.ok(responseDTO);
    }
    @PostMapping("/add")
    public ResponseEntity<Test> addTest(@RequestBody AdminTestUpdateDTO newTestDTO) {
        try {
            Test newTest = testService.addTest(newTestDTO);
            return new ResponseEntity<>(newTest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
