package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "blogs")
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    @Query(value = "SELECT \n" +
            "                c.id, \n" +
            "               c.content, \n" +
            "               c.created_at, \n" +
            "               c.title, \n" +
            "                c.updated_at, \n" +
            "                c.author_id, \n" +
            "               c.cat_blog_id, \n" +
            "                c.status, \n" +
            "                c.image, \n" +
            "                ac.fullname AS author_name, \n" +
            "                bc.name AS category_name\n" +
            "            FROM \n" +
            "               blogs c \n" +
            "            INNER JOIN \n" +
            "               account ac ON c.author_id = ac.id \n" +
            "            INNER JOIN \n" +
            "               categories bc ON c.cat_blog_id = bc.id_category\n" +
            "               where c.status = 1 and c.is_deleted = 0", nativeQuery = true)
    Page<Object[]> findAllBlogs(Pageable pageable);

    @Query("SELECT b FROM Blog b " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:categoryId IS NULL OR b.category.id = :categoryId) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:fromDate IS NULL OR b.createdAt >= :fromDate) " +
            "AND (:toDate IS NULL OR b.createdAt <= :toDate) " +
            "AND b.isDeleted = false")
    Page<Blog> searchBlogs(@Param("title") String title,
                           @Param("categoryId") Integer categoryId,
                           @Param("status") Boolean status,
                           @Param("fromDate") LocalDateTime fromDate,
                           @Param("toDate") LocalDateTime toDate,
                           Pageable pageable);

    @Query(value = "SELECT c.id, c.content, c.created_at, c.title, c.updated_at, c.author_id, " +
            "c.cat_blog_id, c.status, c.image, ac.fullname AS author_name, " +
            "bc.name AS category_name " +
            "FROM blogs c " +
            "INNER JOIN account ac ON c.author_id = ac.id " +
            "INNER JOIN categories bc ON c.cat_blog_id = bc.id_category " +
            "WHERE c.id = :id and c.is_deleted = 0", nativeQuery = true)
    List<Object[]> getBlog(@Param("id") Integer id);


    @Query(value = "SELECT c.id, c.content, c.created_at, c.title, c.updated_at, c.author_id, c.cat_blog_id,\n" +
            "            c.status, c.image, ac.fullname AS author_name, bc.name AS category_name \n" +
            "            FROM blogs c \n" +
            "            INNER JOIN account ac ON c.author_id = ac.id \n" +
            "            INNER JOIN blog_categories bc ON c.cat_blog_id = bc.id \n" +
            "            WHERE c.cat_blog_id = :categoryId and c.status = 1 and c.is_deleted = 0",
            countQuery = "SELECT COUNT(c.id) FROM blogs c WHERE c.cat_blog_id = :categoryId", // Để đếm tổng số lượng blog
            nativeQuery = true)
    Page<Object[]> findByCategoryIdWithPagination(@Param("categoryId") Integer categoryId, Pageable pageable);


    List<Blog> findAllByStatusAndIsDeletedOrderByCreatedAtDesc(boolean status, boolean isDeleted, Pageable pageable);

    @Query(value = "SELECT b.id AS id, " +
            "b.title AS title," +
            "a.fullname AS authorFullName, " +
            "bc.name AS category, " +
            "b.status AS status " +
            "FROM blogs b " +
            "JOIN account a ON b.author_id = a.id " +
            "JOIN categories bc ON b.cat_blog_id = bc.id_category",
            nativeQuery = true)
    List<Object[]> findAllBlogsAsObjectArray();

    @Query(value = "SELECT \n" +
            "    b.id AS blog_id,\n" +
            "    b.title AS title,\n" +
            "    b.content ,\n" +
            "    a.fullname AS author_name,\n" +
            "    b.cat_blog_id as level_3_id,\n" +
            "    c3.name AS category_name_level_3,\n" +
            "    c2.id_category AS level_2_id,\n" +
            "    c2.name AS category_name_level_2,\n" +
            "    c1.id_category AS level_1_id,\n" +
            "    c1.name AS category_name_level_1,\n" +
            "    b.status AS status,\n" +
            "    b.is_deleted AS is_deleted\n" +
            "FROM blogs b\n" +
            "JOIN account a ON b.author_id = a.id\n" +
            "JOIN categories c3 ON b.cat_blog_id = c3.id_category\n" +
            "LEFT JOIN categories c2 ON c3.parent_id = c2.id_category\n" +
            "LEFT JOIN categories c1 ON c2.parent_id = c1.id_category;\n" +
            "         ",
            countQuery = "SELECT COUNT(b.id) FROM blogs b where is_deleted = 0",
            nativeQuery = true)
    Page<Object[]> findBlogAdmin(Pageable pageable);

    @Query(value = """
            SELECT 
                b.id AS blog_id,
                b.title AS title,
                b.content,
                a.fullname AS author_name,
                b.cat_blog_id AS level_3_id,
                c3.name AS category_name_level_3,
                c2.id_category AS level_2_id,
                c2.name AS category_name_level_2,
                c1.id_category AS level_1_id,
                c1.name AS category_name_level_1,
                b.status AS status,
                b.is_deleted AS is_deleted
            FROM 
                blogs b
            JOIN 
                account a ON b.author_id = a.id
            LEFT JOIN 
                categories c3 ON b.cat_blog_id = c3.id_category
            LEFT JOIN 
                categories c2 ON c3.parent_id = c2.id_category
            LEFT JOIN 
                categories c1 ON c2.parent_id = c1.id_category
            WHERE 
                b.is_deleted = 0
                AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                AND (:searchTerm IS NULL OR b.title LIKE CONCAT('%', :searchTerm, '%'))
            """,
            countQuery = """
                    SELECT 
                        COUNT(b.id)
                    FROM 
                        blogs b
                    LEFT JOIN 
                        categories c3 ON b.cat_blog_id = c3.id_category
                    LEFT JOIN 
                        categories c2 ON c3.parent_id = c2.id_category
                    LEFT JOIN 
                        categories c1 ON c2.parent_id = c1.id_category
                    WHERE 
                        b.is_deleted = 0
                        AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                        AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                        AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                        AND (:searchTerm IS NULL OR b.title LIKE CONCAT('%', :searchTerm, '%'))
                    """,
            nativeQuery = true)
    Page<Object[]> findBlogAdminSearch(
            @Param("categoryId1") Integer categoryId1,
            @Param("categoryId2") Integer categoryId2,
            @Param("categoryId3") Integer categoryId3,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    @Query(value = "SELECT \n" +
            "    b.title, \n" +
            "    b.content, \n" +
            "    b.image, \n" +
            "    b.status, \n" +
            "    b.cat_blog_id AS level_3_id, \n" +
            "    c2.id_category AS level_2_id, \n" +
            "    c1.id_category AS level_1_id\n" +
            "FROM blogs b\n" +
            "LEFT JOIN categories c3 ON b.cat_blog_id = c3.id_category\n" +
            "LEFT JOIN categories c2 ON c3.parent_id = c2.id_category\n" +
            "LEFT JOIN categories c1 ON c2.parent_id = c1.id_category\n" +
            "WHERE b.id = :id;\n", nativeQuery = true)
    List<Object[]> findBlogByIdAdmin(@Param("id") Integer id);

    @Query(value = """
            SELECT bg.id, bg.content, bg.created_at, bg.deleted_date, bg.image, bg.is_deleted, bg.status, bg.title, bg.updated_at, bg.author_id, bg.cat_blog_id
            FROM blogs bg
            LEFT JOIN categories c3 ON bg.cat_blog_id = c3.id_category
            LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
            LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
            WHERE bg.is_deleted = 1
            AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
            AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
            AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
            AND (:title IS NULL OR LOWER(bg.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:deletedDate IS NULL OR DATE(bg.deleted_date) = :deletedDate)
            """,
            countQuery = """
                    SELECT COUNT(*)  FROM blogs bg
                    LEFT JOIN categories c3 ON bg.cat_blog_id = c3.id_category
                    LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
                    LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
                    WHERE bg.is_deleted = 1
                    AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                    AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                    AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                    AND (:title IS NULL OR LOWER(bg.title) LIKE LOWER(CONCAT('%', :title, '%')))
                    AND (:deletedDate IS NULL OR DATE(bg.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true)
    Page<Object[]> findBlogsBy(
            @Param("categoryId1") Integer categoryId1,
            @Param("categoryId2") Integer categoryId2,
            @Param("categoryId3") Integer categoryId3,
            @Param("title") String title,
            @Param("deletedDate") String deletedDate,
            Pageable pageable);


    @Query("SELECT b FROM Blog b WHERE " +
            "(:title IS NULL OR b.title LIKE %:title%) AND " +
            "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
            "b.status = true AND " +
            "b.isDeleted = false " +
            "ORDER BY b.views DESC")
    Page<Blog> findBlogsByTitleAndCategory(String title, Integer categoryId, Pageable pageable);

}
