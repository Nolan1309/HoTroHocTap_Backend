package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.BlogCategoryDTO;
import com.example.hotrohoctapbackend.dao.BlogCategoryRepository;
import com.example.hotrohoctapbackend.entity.BlogCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogCategoryService {
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    public List<BlogCategory> getAll(){
        return blogCategoryRepository.findAll();
    }

    public List<BlogCategoryDTO> getBlogCategoriesWithBlogCount() {
        List<Object[]> results = blogCategoryRepository.findBlogCategoriesWithBlogCount();
        return results.stream()
                .map(row -> new BlogCategoryDTO(
                        (int) row[0],
                        (String) row[1],
                        (String) row[2],
                        (Timestamp) row[3],
                        (Timestamp) row[4],
                        ((Long) row[5]).intValue())) // Convert Long to int for blog count
                .collect(Collectors.toList());
    }
    public BlogCategory addBlogCategory(String name, String description) {
        if (blogCategoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại!");
        }
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setName(name);
        blogCategory.setDescription(description);
        blogCategory.setCreatedAt(LocalDateTime.now());
        blogCategory.setUpdatedAt(LocalDateTime.now());

        return blogCategoryRepository.save(blogCategory);
    }
}
