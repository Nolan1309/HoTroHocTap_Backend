package com.example.hotrohoctapbackend.controller;
import com.example.hotrohoctapbackend.DTO.Admin.AdminCommentGetDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/getall")
    public Page<AdminCommentGetDTO> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getAllCommentAdmin(page, size);
    }
    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideCommnetAdmin(@PathVariable int id) {
        try {
            Comment hidedComment = commentService.hideCommentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
    @PutMapping("/show/{id}")
    public ResponseEntity<?> showCommnetAdmin(@PathVariable int id) {
        try {
            Comment showComment = commentService.showCommentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeCommnetAdmin(@PathVariable int id) {
        try {
            Comment activeComment = commentService.activeCommentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
    @PutMapping("/unactive/{id}")
    public ResponseEntity<?> unactiveCommentAdmin(@PathVariable int id) {
        try {
            Comment unactivedComment = commentService.unactiveCommentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
}
