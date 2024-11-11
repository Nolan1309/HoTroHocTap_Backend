package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "blogcategorys")
public interface BlogCategoryRepository extends JpaRepository<BlogCategory,Integer> {

    @Query(value = "SELECT " +
            "bc.id, " +
            "bc.name, " +
            "bc.description, " +
            "bc.created_at, " +
            "bc.updated_at, " +
            "COUNT(b.id) AS blog_count " +
            "FROM blog_categories bc " +
            "LEFT JOIN blogs b ON bc.id = b.cat_blog_id " +
            "GROUP BY bc.id, bc.name, bc.description, bc.created_at, bc.updated_at " +
            "ORDER BY blog_count DESC",
            nativeQuery = true)
    List<Object[]> findBlogCategoriesWithBlogCount();

}
