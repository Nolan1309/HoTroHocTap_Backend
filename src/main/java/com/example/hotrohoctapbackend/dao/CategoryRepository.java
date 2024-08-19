package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "categorys")
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query(value = "SELECT * FROM categories WHERE level = :level", nativeQuery = true)
    List<Category> findByLevel(@Param("level") int level);

    @Query(value = "SELECT * FROM categories WHERE level = :level AND parent_id = :parentId", nativeQuery = true)
    List<Category> findCategoriesByLevelAndParentId(@Param("level") int level, @Param("parentId") long parentId);

    @Query(value = "SELECT * FROM categories WHERE parent_id = :id_category", nativeQuery = true)
    List<Category> findByParentId(@Param("id_category") int id_category);

}
