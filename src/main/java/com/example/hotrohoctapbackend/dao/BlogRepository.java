package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "blogs")
public interface BlogRepository extends JpaRepository<Blog,Integer> {

    @Query(value = "SELECT \n" +
            "    c.id, \n" +
            "    c.content, \n" +
            "    c.created_at, \n" +
            "    c.title, \n" +
            "    c.updated_at, \n" +
            "    c.author_id, \n" +
            "    c.cat_blog_id, \n" +
            "    c.status, \n" +
            "    c.image, \n" +
            "    ac.fullname AS author_name, \n" +
            "    bc.name AS category_name\n" +
            "FROM \n" +
            "    blogs c \n" +
            "INNER JOIN \n" +
            "    account ac ON c.author_id = ac.id \n" +
            "INNER JOIN \n" +
            "    blog_categories bc ON c.cat_blog_id = bc.id;\n", nativeQuery = true)
    Page<Object[]> findAllBlogs(Pageable pageable);


    @Query(value = "SELECT c.id, c.content, c.created_at, c.title, c.updated_at, c.author_id, " +
            "c.cat_blog_id, c.status, c.image, ac.fullname AS author_name, " +
            "bc.name AS category_name " +
            "FROM blogs c " +
            "INNER JOIN account ac ON c.author_id = ac.id " +
            "INNER JOIN blog_categories bc ON c.cat_blog_id = bc.id " +
            "WHERE c.id = :id", nativeQuery = true)
    List<Object[]> getBlog(@Param("id") Integer id);


    @Query(value = "SELECT c.id, c.content, c.created_at, c.title, c.updated_at, c.author_id, c.cat_blog_id, " +
            "c.status, c.image, ac.fullname AS author_name, bc.name AS category_name " +
            "FROM blogs c " +
            "INNER JOIN account ac ON c.author_id = ac.id " +
            "INNER JOIN blog_categories bc ON c.cat_blog_id = bc.id " +
            "WHERE c.cat_blog_id = :categoryId",
            countQuery = "SELECT COUNT(c.id) FROM blogs c WHERE c.cat_blog_id = :categoryId", // Để đếm tổng số lượng blog
            nativeQuery = true)
    Page<Object[]> findByCategoryIdWithPagination(@Param("categoryId") Integer categoryId, Pageable pageable);


    List<Blog> findAllByOrderByCreatedAtDesc(Pageable pageable);

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
    @Query(value = "SELECT b.id AS id, b.title AS title, a.fullname AS fullname, " +
            "c.name AS category_name, b.status AS status, b.is_deleted AS is_deleted " +
            "FROM blogs b " +
            "JOIN account a ON b.author_id = a.id " +
            "JOIN blog_categories c ON b.cat_blog_id = c.id",
            countQuery = "SELECT COUNT(b.id) FROM blogs b",
            nativeQuery = true)
    Page<Object[]> findBlogAdmin(Pageable pageable);
    @Query(value = "SELECT b.title, b.content, b.image, b.status, b.cat_blog_id FROM blogs b WHERE b.id = :id", nativeQuery = true)
    List<Object[]> findBlogByIdAdmin(@Param("id") Integer id);
}
