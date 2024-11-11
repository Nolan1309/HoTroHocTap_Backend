package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.BlogCategoryDTO;
import com.example.hotrohoctapbackend.entity.BlogCategory;
import com.example.hotrohoctapbackend.service.BlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/blog-category")
public class BlogCategoryController {
    @Autowired
    private BlogCategoryService blogCategoryService;

//    @GetMapping()
//    public List<BlogCategory> getAllCategoryBlog(){
//        return blogCategoryService.getAll();
//    }

    @GetMapping()
    public ResponseEntity<List<BlogCategoryDTO>> getBlogCategoriesWithBlogCount() {
        List<BlogCategoryDTO> blogCategories = blogCategoryService.getBlogCategoriesWithBlogCount();
        return ResponseEntity.ok(blogCategories);
    }



}
