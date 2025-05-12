package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "categorys")
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted, c.type FROM categories c WHERE c.level = :level", nativeQuery = true)
    List<Object[]> findByLevel2(@Param("level") int level);

    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted, c.type FROM categories c WHERE c.level = :level AND c.parent_id = :parentId", nativeQuery = true)
    List<Category> findCategoriesByLevelAndParentId(@Param("level") int level, @Param("parentId") long parentId);

    List<Category> findByLevel(int level);

    List<Category> findByParentCategoryId(int parentId);

    List<Category> findByStatus(String status);

    List<Category> findByNameContaining(String name);

    @Query("SELECT c FROM Category c WHERE "
            + "(LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name = '') "
            + "AND (c.type = :type OR :type = '') "
            + "AND (c.status = :status OR :status = '') "
            + "AND c.isDeleted = :isDeleted "
            + "AND c.level = 1")
        // Chỉ lấy các danh mục có level = 1
    Page<Category> findCategoriesByFilters(
            String name,
            String type,
            String status,
            boolean isDeleted,
            Pageable pageable
    );

    List<Category> findByLevelAndType(int level, String type);

    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted, c.type FROM categories c", nativeQuery = true)
    List<Object[]> getAllCategory();

    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted, c.type FROM categories c WHERE c.parent_id = :id_category", nativeQuery = true)
    List<Category> findByParentId(@Param("id_category") int id_category);

    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted, c.type FROM categories c WHERE c.id_category = :id_category", nativeQuery = true)
    List<Category> findCategoryNameByIdCategory(@Param("id_category") int id_category);

}
