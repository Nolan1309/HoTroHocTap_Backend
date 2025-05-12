package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Category.CategoryDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Category.CategoryDTOPublic;
import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/categories")
    public Page<CategoryDTOAdmin> getCategories(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getCategories(name, type, status, isDeleted, pageable);
    }

    @GetMapping("/api/categories/level3/course")
    public ApiResponse<List<CategoryDTOPublic>> getCategoriesByLevel3AndTypeCourse() {
        try {
            List<CategoryDTOPublic> categoryDTOs = categoryService.getCategoriesByLevelAndType(3, "COURSE");
            return new ApiResponse<>(200, "Success", categoryDTOs);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }
    }

    @GetMapping("/api/categories/level3/document")
    public ApiResponse<List<CategoryDTOPublic>> getCategoriesByLevel3AndTypeDocument() {
        try {
            List<CategoryDTOPublic> categoryDTOs = categoryService.getCategoriesByLevelAndType(3, "DOCUMENT");
            return new ApiResponse<>(200, "Success", categoryDTOs);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }
    }


    @GetMapping("/api/categories/level3/blog")
    public ApiResponse<List<CategoryDTOPublic>> getCategoriesByLevel3AndTypeBlog() {
        try {
            List<CategoryDTOPublic> categoryDTOs = categoryService.getCategoriesByLevelAndTypeBlog(3, "BLOG");
            return new ApiResponse<>(200, "Success", categoryDTOs);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }
    }

    @PostMapping("/api/categories")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CategoryDTOAdmin categoryDTO) {
        Category createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Category created successfully", null), HttpStatus.CREATED);
    }

    @PutMapping("/api/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryDTOAdmin categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Category updated successfully", null), HttpStatus.OK);
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(
            @PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Category Delete successfully", null), HttpStatus.OK);
    }


    @GetMapping("/categories-all")
    public List<CategoryDTO> getAllCategory() {
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

    @PutMapping("/update-branch/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody CategoryDTO categoryDTO) {
        try {
            // Gọi Service để cập nhật Category
            Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add-branch")
    public ResponseEntity<Category> insertCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category newCategory = categoryService.insertCategory(categoryDTO); // Call the service method to add a new category
            return ResponseEntity.ok(newCategory); // Return the newly added category with status 200
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request if an error occurs
        }
    }
}
