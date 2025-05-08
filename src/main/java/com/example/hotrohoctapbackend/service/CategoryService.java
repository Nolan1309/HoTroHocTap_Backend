package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Category.CategoryDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Category.CategoryDTOPublic;
import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            String type = (String) row[6];
            CategoryDTO item = new CategoryDTO(id, lv, name, type, parentId);
            categoryDTOList.add(item);
        }
        return categoryDTOList;
    }

    public List<Category> getSubCategories(int parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
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
            String type = (String) row[6];
            CategoryDTO item = new CategoryDTO(id, level, name, type, parentId);
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
            category.setType(categoryDTO.getType());
            // Tìm Category cha dựa trên parentId từ CategoryDTO
            if (categoryDTO.getParentId() != null) {
                Category parentCategory = categoryRepository.findById(categoryDTO.getParentId().intValue())
                        .orElseThrow(() -> new RuntimeException("Parent Category not found with id " + categoryDTO.getParentId()));
                category.setParentCategory(parentCategory); // Đặt Category cha
            } else {
                category.setParentCategory(null); // Nếu parentId là null, đặt Category cha là null
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
        category.setType(categoryDTO.getType());
        // Tìm Category cha dựa trên parentId từ CategoryDTO, nếu có
        if (categoryDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentId().intValue())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found with id " + categoryDTO.getParentId()));
            category.setParentCategory(parentCategory); // Đặt Category cha
        } else {
            category.setParentCategory(null); // Nếu parentId là null, đặt Category cha là null
        }
        // Lưu Category mới vào cơ sở dữ liệu và trả về kết quả
        return categoryRepository.save(category);
    }

    // Phương thức phân trang và lọc theo name, type, status, isDeleted
    public Page<CategoryDTOAdmin> getCategories(String name, String type, String status, boolean isDeleted, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findCategoriesByFilters(
                name, type, status, isDeleted, pageable
        );
        return categoryPage.map(this::convertToDTO); // Chuyển đổi entity sang DTO
    }

    public List<CategoryDTOPublic> getCategoriesByLevelAndType(int level, String type) {
        List<Category> categories = categoryRepository.findByLevelAndType(level, type);
        return categories.stream()
                .map(this::convertToDTOPublic)
                .collect(Collectors.toList());
    }

    public List<CategoryDTOPublic> getCategoriesByLevelAndTypeBlog(int level, String type) {
        List<Category> categories = categoryRepository.findByLevelAndType(level, type);
        return categories.stream()
                .map(this::convertToDTOPublicBlog)
                .collect(Collectors.toList());
    }

    private CategoryDTOPublic convertToDTOPublic(Category category) {
        CategoryDTOPublic dto = new CategoryDTOPublic();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setLevel(category.getLevel());
        dto.setType(category.getType());
        dto.setDescription(category.getDescription());
        dto.setItemCount(category.getCourseList().size());
        dto.setStatus(category.getStatus());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    private CategoryDTOPublic convertToDTOPublicBlog(Category category) {
        CategoryDTOPublic dto = new CategoryDTOPublic();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setLevel(category.getLevel());
        dto.setType(category.getType());
        dto.setDescription(category.getDescription());
        dto.setItemCount(category.getBlogList().size());
        dto.setStatus(category.getStatus());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    // Chuyển đổi từ entity sang DTO
    private CategoryDTOAdmin convertToDTO(Category category) {
        List<CategoryDTOAdmin> children = category.getChildren().stream()
                .map(this::convertToDTO) // Đệ quy để lấy danh mục con
                .collect(Collectors.toList());
        return new CategoryDTOAdmin(category.getId(), category.getName(), category.getType(), category.getDescription(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                category.getLevel(), category.getOrderIndex(), category.getStatus(), category.getItemCount(),
                category.isDeleted(), category.getCreatedAt(), category.getUpdatedAt(), children);
    }


    public Category createCategory(CategoryDTOAdmin categoryDTO) {
        // Chuyển đổi CategoryDTOAdmin thành Category entity
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setDescription(categoryDTO.getDescription());
        Category parent = null;
        if (categoryDTO.getParentId() != null) {
            parent = categoryRepository.findById(categoryDTO.getParentId()).get();
            parent.setItemCount(parent.getItemCount() + 1);
            categoryRepository.save(parent);
        }
        category.setParentCategory(parent);
        category.setParentCategory(parent);
        category.setLevel(categoryDTO.getLevel());
        category.setOrderIndex(categoryDTO.getOrderIndex());
        category.setStatus(categoryDTO.getStatus());
        category.setItemCount(categoryDTO.getItemCount());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public Category updateCategory(Integer id, CategoryDTOAdmin categoryDTO) {
        // Tìm kiếm danh mục theo ID
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Cập nhật thông tin
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setDescription(categoryDTO.getDescription());
        Category parent = null;
        if (categoryDTO.getParentId() != null) {
            parent = categoryRepository.findById(categoryDTO.getParentId()).get();
        }
        category.setParentCategory(parent);
        category.setLevel(categoryDTO.getLevel());
        category.setOrderIndex(categoryDTO.getOrderIndex());
        category.setStatus(categoryDTO.getStatus());
        category.setItemCount(categoryDTO.getItemCount());
        category.setUpdatedAt(LocalDateTime.now());
        category.setDeleted(categoryDTO.isDeleted());
        category.setDeletedDate(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        categoryRepository.delete(category);
    }
}
