package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogsall")
    public ResponseEntity<List<BlogDTO>> getAllBlogDTOs() {
        List<BlogDTO> blogDTOs = blogService.getAllBlogDTOs();
        return ResponseEntity.ok(blogDTOs);
    }
}
