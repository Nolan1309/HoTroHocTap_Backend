package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.CategoryCourseDTO;
import com.example.hotrohoctapbackend.dao.CourseCategoryRepository;
import com.example.hotrohoctapbackend.entity.CourseCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseCategoryService {

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    public List<CategoryCourseDTO> getAllCategories() {
        return courseCategoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryCourseDTO> getCategoryById(Integer id) {
        return courseCategoryRepository.findById(id).map(this::convertToDTO);
    }

    public CategoryCourseDTO saveCategory(CategoryCourseDTO categoryDTO) {
        CourseCategory category = convertToEntity(categoryDTO);
        CourseCategory savedCategory = courseCategoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    private CategoryCourseDTO convertToDTO(CourseCategory category) {
        CategoryCourseDTO dto = new CategoryCourseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreateAt(category.getCreatedAt());
        dto.setUpdateAt(category.getUpdatedAt());
        return dto;
    }

    private CourseCategory convertToEntity(CategoryCourseDTO dto) {
        CourseCategory category = new CourseCategory();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setCreatedAt(dto.getCreateAt());
        category.setUpdatedAt(dto.getCreateAt());
        return category;
    }
}
