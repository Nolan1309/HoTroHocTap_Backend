package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.CategoryCourseDTO;
import com.example.hotrohoctapbackend.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-categories")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;

    @GetMapping
    public List<CategoryCourseDTO> getAllCategories() {
        return courseCategoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryCourseDTO> getCategoryById(@PathVariable Integer id) {
        Optional<CategoryCourseDTO> category = courseCategoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryCourseDTO> createCategory(@RequestBody CategoryCourseDTO categoryDTO) {
        CategoryCourseDTO createdCategory = courseCategoryService.saveCategory(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }
}
