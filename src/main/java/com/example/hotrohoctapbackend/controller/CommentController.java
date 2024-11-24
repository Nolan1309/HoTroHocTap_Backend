package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.CommentDTO_User;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // API để lấy các bình luận gốc với phân trang, lọc theo video và lesson
    @GetMapping("/video/{videoId}/lesson/{lessonId}")
    public Page<Comment> getRootCommentsByVideoAndLessonUser(
            @PathVariable int videoId,
            @PathVariable int lessonId,
            Pageable pageable) {
        return commentService.getRootCommentsByVideoAndLessonUser(videoId, lessonId, pageable);
    }

    // API để lấy các bình luận con theo parentId, videoId và lessonId
    @GetMapping("/{parentId}/children/video/{videoId}/lesson/{lessonId}")
    public List<Comment> getChildrenCommentsByParentAndVideoAndLessonUser(
            @PathVariable int parentId,
            @PathVariable int videoId,
            @PathVariable int lessonId) {
        return commentService.getChildrenCommentsByParentAndVideoAndLessonUser(parentId, videoId, lessonId);
    }

    @PostMapping("/submit")
    public ResponseEntity<Comment> addCommentUser(@RequestBody CommentDTO_User commentDTO) {
        try {
            Comment createdComment = commentService.addCommentFromDTOUser(commentDTO);
            return ResponseEntity.ok(createdComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        try {
            Comment deletedComment = commentService.deleteComment(commentId);
            return ResponseEntity.ok(deletedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


}
