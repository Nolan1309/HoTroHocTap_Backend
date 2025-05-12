package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminCommentGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCommentDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Comment.BulkDeleteRequest;
import com.example.hotrohoctapbackend.DTO.AdminV3.Comment.BulkStatusUpdateRequest;
import com.example.hotrohoctapbackend.DTO.AdminV3.Comment.CommentItemDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Comment.ReplyCommentAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.StatusUpdateRequest;
import com.example.hotrohoctapbackend.DTO.User.CommentDTO_User;
import com.example.hotrohoctapbackend.DTO.User.CommentUserDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // API để lấy các bình luận gốc với phân trang, lọc theo video và lesson
    @GetMapping("/video/{videoId}/lesson/{lessonId}")
    public Page<CommentUserDTO> getRootCommentsByVideoAndLessonUser(
            @PathVariable int videoId,
            @PathVariable int lessonId,
            Pageable pageable) {
        return commentService.getRootCommentsByVideoAndLessonUserList(videoId, lessonId, pageable);
    }

    // API để lấy các bình luận con theo parentId, videoId và lessonId
    @GetMapping("/{parentId}/children/video/{videoId}/lesson/{lessonId}")
    public List<Comment> getChildrenCommentsByParentAndVideoAndLessonUser(
            @PathVariable int parentId,
            @PathVariable int videoId,
            @PathVariable int lessonId) {
        return commentService.getChildrenCommentsByParentAndVideoAndLessonUser(parentId, videoId, lessonId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentItemDTO>>> getComments(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Comment.Status status,
            @RequestParam(required = false) Comment.TargetType targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentItemDTO> comments = commentService.getCommentsWithFilters(content, status, targetType, pageable);

        ApiResponse<Page<CommentItemDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Comments retrieved successfully", comments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reply/{commentId}")
    public ResponseEntity<ApiResponse<?>> replyToComment(
            @PathVariable Integer commentId,
            @RequestBody ReplyCommentAdmin replyCommentAdmin) {

        try {
            // Gọi service để trả lời bình luận
            Comment replyComment = commentService.replyToComment(commentId, replyCommentAdmin.getContent(), replyCommentAdmin.getAdminId());

            // Trả về kết quả thành công
            ApiResponse<Comment> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Reply created successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            // Trả về lỗi nếu có vấn đề
            ApiResponse<Comment> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateCommentStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest statusUpdateRequest) {

        try {
            // Gọi service để cập nhật trạng thái của bình luận
            Comment updatedComment = commentService.updateStatusComment(id, statusUpdateRequest.getStatus());

            // Trả về kết quả thành công
            ApiResponse<Comment> response = new ApiResponse<>(HttpStatus.OK.value(), "Comment status updated successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ex) {
            // Trả về lỗi nếu có vấn đề
            ApiResponse<Comment> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<CommentUserDTO> addCommentUser(@RequestBody CommentDTO_User commentDTO) {
        try {
            CommentUserDTO createdComment = commentService.addCommentFromDTOUser(commentDTO);
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

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteCommentAdmin(@PathVariable int commentId) {
        try {
            Comment deletedComment = commentService.deleteComment(commentId);
            ApiResponse<?> response = new ApiResponse<>(HttpStatus.OK.value(), "Successful", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            ApiResponse<?> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<?> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/bulk/status")
    public ResponseEntity<ApiResponse<String>> bulkUpdateStatus(
            @RequestBody BulkStatusUpdateRequest request) {

        try {
            // Cập nhật trạng thái cho các bình luận
            List<Comment> updatedComments = commentService.bulkUpdateStatus(request.getIds(), request.getStatus());

            // Trả về kết quả thành công
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Comments updated successfully", "Updated " + updatedComments.size() + " comments");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            // Trả về lỗi nếu có vấn đề
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // API xóa nhiều bình luận
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<String>> bulkDeleteComments(@RequestBody BulkDeleteRequest request) {
        try {
            // Xóa các bình luận
            boolean isDeleted = commentService.bulkDeleteComments(request.getIds());

            // Trả về kết quả thành công hoặc thất bại
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), isDeleted ? "Comments deleted successfully" : "No comments found to delete", null);
            return new ResponseEntity<>(response, isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Trả về lỗi nếu có vấn đề
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/restore/list-all-comments")
    public Page<AdminCommentDTORestoreList> getBlogs(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (content.equals("")) {
            content = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }

        return commentService.getComments(content, deletedDate, page, size);
    }

    @PutMapping("/restore/{commentId}")
    public ResponseEntity<Comment> restoreChapter(@PathVariable Integer commentId) {
        AdminCommentDTORestoreList adminCommentDTORestoreList = new AdminCommentDTORestoreList();
        adminCommentDTORestoreList.setId(commentId);
        Comment restoreChapter = commentService.updateRestoreComment(adminCommentDTORestoreList);
        return ResponseEntity.ok(restoreChapter);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteChapter(@PathVariable Integer commentId) {
        AdminCommentDTORestoreList adminCommentDTORestoreList = new AdminCommentDTORestoreList();
        adminCommentDTORestoreList.setId(commentId);
        commentService.deleteRestoreComment(adminCommentDTORestoreList);
        return ResponseEntity.ok("Comment permanently deleted.");
    }


}
