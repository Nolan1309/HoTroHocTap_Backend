package com.example.hotrohoctapbackend.service;
import com.example.hotrohoctapbackend.DTO.Admin.AdminCommentGetDTO;
import com.example.hotrohoctapbackend.dao.CommentRepository;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public Page<AdminCommentGetDTO> getAllCommentAdmin(int page, int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để lấy dữ liệu dạng Page<Object[]>
        Page<Object[]> dataPage = commentRepository.findAllComments(pageable);

        // Map dữ liệu từ Object[] sang AdminCommentGetDTO
        Page<AdminCommentGetDTO> resultPage = dataPage.map(row -> {
            AdminCommentGetDTO dto = new AdminCommentGetDTO();
            dto.setId((Integer) row[0]);          // id
            dto.setContent((String) row[1]);     // content
            dto.setCreatedAt((Timestamp) row[2]);
            dto.setFullname((String) row[3]);
            dto.setIsApproved((Boolean) row[4]);
            dto.setIsDeleted((Boolean) row[5]);    // isDeleted
            return dto;
        });

        // Trả về Page<AdminCommentGetDTO>
        return resultPage;
    }
    public Comment hideCommentAdmin(int commentID) {
        // Tìm tài khoản theo ID
        Optional<Comment> accountOpt = commentRepository.findById(commentID);

        if (accountOpt.isPresent()) {
            Comment account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return commentRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + commentID);
        }
    }

    public Comment showCommentAdmin(int commentID) {
        // Tìm tài khoản theo ID
        Optional<Comment> accountOpt = commentRepository.findById(commentID);

        if (accountOpt.isPresent()) {
            Comment account = accountOpt.get();
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return commentRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + commentID);
        }
    }
    public Comment activeCommentAdmin(int commentID) {
        // Tìm tài khoản theo ID
        Optional<Comment> accountOpt = commentRepository.findById(commentID);

        if (accountOpt.isPresent()) {
            Comment account = accountOpt.get();
            account.setApproved(false);
            // Lưu thay đổi
            return commentRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + commentID);
        }
    }

    public Comment unactiveCommentAdmin(int commentID) {
        // Tìm tài khoản theo ID
        Optional<Comment> accountOpt = commentRepository.findById(commentID);

        if (accountOpt.isPresent()) {
            Comment account = accountOpt.get();
            account.setApproved(true);
            // Lưu thay đổi
            return commentRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + commentID);
        }
    }
}
