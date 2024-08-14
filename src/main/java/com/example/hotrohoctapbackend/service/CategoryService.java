package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByLevel(int level) {
        return categoryRepository.findByLevel(level);
    }

    public List<Category> getCategoriesByLevelAndParentId(int level, long parentId) {
        return categoryRepository.findCategoriesByLevelAndParentId(level, parentId);
    }
}
