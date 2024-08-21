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

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByLevel(int level) {
        return categoryRepository.findByLevel2(level);
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
}
