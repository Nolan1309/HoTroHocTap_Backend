package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query(value = """
            SELECT 
                c.id AS id, 
                c.content AS content, 
                c.created_at AS createdAt, 
                a.fullname AS fullname,
                c.is_approved AS isApproved,
                c.is_deleted AS isDeleted
            FROM 
                comments c
            LEFT JOIN 
                account a ON c.acc_id = a.id
            """,
            countQuery = "SELECT COUNT(*) FROM comments c",
            nativeQuery = true)
    Page<Object[]> findAllComments(Pageable pageable);
}
