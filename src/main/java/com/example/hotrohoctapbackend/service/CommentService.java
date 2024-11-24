package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.CommentDTO_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CommentRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Video;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public Page<Comment> getRootCommentsByVideoAndLessonUser(int videoId, int lessonId, Pageable pageable) {
        return commentRepository.findRootCommentsByVideoAndLesson(videoId, lessonId, pageable);
    }


    public List<Comment> getChildrenCommentsByParentAndVideoAndLessonUser(int parentId, int videoId, int lessonId) {
        return commentRepository.findChildrenByParentIdAndVideoAndLesson(parentId, videoId, lessonId);
    }

    public Comment deleteComment(int commentId) {
        // Tìm comment theo ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + commentId + " not found."));

        // Kiểm tra nếu comment đã bị xóa
        if (comment.isDeleted()) {
            throw new IllegalStateException("Comment with ID " + commentId + " has already been deleted.");
        }

        // Đánh dấu comment là đã xóa
        comment.setDeletedDate(LocalDateTime.now());
        comment.setDeleted(true);

        // Lưu thay đổi
        return commentRepository.save(comment);
    }


    public Comment addCommentFromDTOUser(CommentDTO_User commentDTO) {
        // Lấy thông tin tài khoản
        Account account = accountRepository.findById(commentDTO.getAccId())
                .orElseThrow(() -> new IllegalArgumentException("Account không tồn tại"));

        // Lấy thông tin video
        Video video = videoRepository.findById(commentDTO.getVideoId())
                .orElseThrow(() -> new IllegalArgumentException("Video không tồn tại"));

        Lesson lesson = lessonRepository.findById(commentDTO.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson không tồn tại"));
        // Tạo bình luận mới
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAccount(account);
        comment.setVideo(video);
        comment.setLesson(lesson);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        comment.setApproved(false); // Mặc định là đã duyệt
        comment.setDeletedDate(LocalDateTime.now());
        comment.setDeleted(false);

        // Nếu có contentId, thiết lập bình luận cha
        if (commentDTO.getContentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getContentId())
                    .orElseThrow(() -> new IllegalArgumentException("Bình luận cha không tồn tại"));
            comment.setComment(parentComment);
        }

        // Lưu bình luận
        return commentRepository.save(comment);
    }
}
