package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query(value = "SELECT * FROM comments c WHERE c.content_id IS NULL AND c.video_id = :videoId AND c.lesson_id = :lessonId AND c.is_deleted = false AND c.is_approved = true",
            countQuery = "SELECT count(*) FROM comments c WHERE c.content_id IS NULL AND c.video_id = :videoId AND c.lesson_id = :lessonId AND c.is_deleted = false AND c.is_approved = true",
            nativeQuery = true)
    Page<Comment> findRootCommentsByVideoAndLesson(@Param("videoId") int videoId, @Param("lessonId") int lessonId, Pageable pageable);

    // Tìm bình luận con theo parentId, videoId và lessonId
    @Query(value = "SELECT * FROM comments c WHERE c.content_id = :parentId AND c.video_id = :videoId AND c.lesson_id = :lessonId AND c.is_deleted = false AND c.is_approved = true",
            nativeQuery = true)
    List<Comment> findChildrenByParentIdAndVideoAndLesson(@Param("parentId") int parentId, @Param("videoId") int videoId, @Param("lessonId") int lessonId);
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
