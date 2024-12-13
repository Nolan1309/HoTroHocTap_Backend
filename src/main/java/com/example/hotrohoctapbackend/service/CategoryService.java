package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<CategoryDTO> getCategoriesByLevel(int level) {
        List<Object[]> list = categoryRepository.findByLevel2(level);
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Object[] row : list) {
            int id = (int) row[0];
            int lv = (int) row[1];
            String name = (String) row[2];
            Integer idParent = (row[3] != null) ? ((Number) row[3]).intValue() : null;
            Long parentId = (idParent != null) ? idParent.longValue() : null;
            CategoryDTO item = new CategoryDTO(id, lv, name, parentId );
           categoryDTOList.add(item);
        }
        return categoryDTOList;
    }

    public List<Category> getCategoriesByLevel2(int level) {
        return categoryRepository.findByLevel(level);
    }

    public List<Category> getSubCategories(int parentId) {
        return categoryRepository.findByCategory_Id(parentId);
    }

    public List<Category> getCategoriesByLevelAndParentId(int level, long parentId) {
        return categoryRepository.findCategoriesByLevelAndParentId(level, parentId);
    }

    public List<Category> getCategoriesByParentId(int id_category) {
        return categoryRepository.findByParentId(id_category);
    }

    public List<Category> findCategoryNameByIdCategory(int id_category) {
        return categoryRepository.findCategoryNameByIdCategory(id_category);
    }

    public List<CategoryDTO> getAllCategory() {
        List<Object[]> list = categoryRepository.getAllCategory();
        List<CategoryDTO> courseSummaries = new ArrayList<>();
        for (Object[] row : list) {
            int id = (int) row[0];
            int level = (int) row[1];

            String name = (String) row[2];
            Integer idParent = (row[3] != null) ? ((Number) row[3]).intValue() : null;
            Long parentId = (idParent != null) ? idParent.longValue() : null;
            CategoryDTO item = new CategoryDTO(id, level, name, parentId );
            courseSummaries.add(item);
        }
        return courseSummaries;
    }

    // Hàm cập nhật Category dựa vào CategoryDTO
    public Category updateCategory(int id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setName(categoryDTO.getName());
            category.setLevel(categoryDTO.getLevel());

            // Tìm Category cha dựa trên parentId từ CategoryDTO
            if (categoryDTO.getParentId() != null) {
                Category parentCategory = categoryRepository.findById(categoryDTO.getParentId().intValue())
                        .orElseThrow(() -> new RuntimeException("Parent Category not found with id " + categoryDTO.getParentId()));
                category.setCategory(parentCategory); // Đặt Category cha
            } else {
                category.setCategory(null); // Nếu parentId là null, đặt Category cha là null
            }

            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id " + id);
        }
    }
    // Hàm thêm mới Category dựa vào CategoryDTO
    public Category insertCategory(CategoryDTO categoryDTO) {
        // Tạo một đối tượng Category mới từ dữ liệu trong CategoryDTO
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setLevel(categoryDTO.getLevel());

        // Tìm Category cha dựa trên parentId từ CategoryDTO, nếu có
        if (categoryDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentId().intValue())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found with id " + categoryDTO.getParentId()));
            category.setCategory(parentCategory); // Đặt Category cha
        } else {
            category.setCategory(null); // Nếu parentId là null, đặt Category cha là null
        }

        // Lưu Category mới vào cơ sở dữ liệu và trả về kết quả
        return categoryRepository.save(category);
    }
}
