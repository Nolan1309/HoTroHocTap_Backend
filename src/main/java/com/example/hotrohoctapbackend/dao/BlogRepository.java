package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.DTO.BlogDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import java.util.List;
@RepositoryRestResource(path = "blogs")
public interface BlogRepository extends JpaRepository<Blog,Integer> {
    @Query(value = "SELECT b.id AS id, " +
            "b.title AS title,"+
            "a.fullname AS authorFullName, " +
            "bc.name AS category, " +
            "b.status AS status " +
            "FROM blogs b " +
            "JOIN account a ON b.author_id = a.id " +
            "JOIN blog_categories bc ON b.cat_blog_id = bc.id",
            nativeQuery = true)
    List<Object[]> findAllBlogsAsObjectArray();

}
