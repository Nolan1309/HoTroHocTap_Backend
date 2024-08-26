package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories-all")
    public List<CategoryDTO> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getCategoriesByLevel(@RequestParam int level) {
        return categoryService.getCategoriesByLevel(level);
    }

    // Optional: Endpoints for other levels
    @GetMapping("/categories/level1")
    public List<CategoryDTO> getLevel1Categories() {
        return categoryService.getCategoriesByLevel(1);
    }

    @GetMapping("/categories/level2")
    public List<CategoryDTO> getLevel2Categories() {
        return categoryService.getCategoriesByLevel(2);
    }

    @GetMapping("/categories/level3")
    public List<CategoryDTO> getLevel3Categories() {
        return categoryService.getCategoriesByLevel(3);
    }
    @GetMapping("/categories/level")
    public List<Category> getCategories(@RequestParam int level, @RequestParam long parentId) {
        return categoryService.getCategoriesByLevelAndParentId(level, parentId);
    }


    // API để lấy tất cả các danh mục theo cấp bậc
    @GetMapping("/categories/level/{level}")
    public List<CategoryDTO> getCategories(@PathVariable int level) {
        return categoryService.getCategoriesByLevel(level);
    }

    // API để lấy tất cả các danh mục con của một danh mục cha
    @GetMapping("/categories/parent/{parentId}")
    public List<Category> getSubCategories(@PathVariable int parentId) {
        return categoryService.getSubCategories(parentId);
    }

    @GetMapping("/categories_parent_id")
    public List<Category> getCategoriesByParentId(@RequestParam int id_category) {
        return categoryService.getCategoriesByParentId(id_category);
    }
    @GetMapping("/name-by-id")
    public List<Category> getCategoryById(@RequestParam int id_category) {
        return categoryService.findCategoryNameByIdCategory(id_category);
    }

}
