package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminCommentGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCommentDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Comment.CommentItemDTO;
import com.example.hotrohoctapbackend.DTO.User.CommentDTO_User;
import com.example.hotrohoctapbackend.DTO.User.CommentUserDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CommentRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<CommentUserDTO> getRootCommentsByVideoAndLessonUserList(int videoId, int lessonId, Pageable pageable) {
        // Lấy danh sách comment gốc từ repository
        Page<Comment> comments = commentRepository.findRootCommentsByVideoAndLesson(videoId, lessonId, pageable);

        // Chuyển đổi từ Page<Comment> sang Page<CommentDTO>
        Page<CommentUserDTO> commentDTOs = comments.map(comment -> convertToDTO(comment));

        return commentDTOs;
    }


    private CommentUserDTO convertToDTO(Comment comment) {
        // Chuyển đổi Comment thành CommentDTO
        List<CommentUserDTO> replies = comment.getReplies().stream()
                .map(this::convertToDTO) // Ánh xạ các comment con
                .collect(Collectors.toList());

        return new CommentUserDTO(
                comment.getId(),
                comment.getContent(),
                comment.getAccount().getId(),
                comment.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                replies
        );
    }

    public List<Comment> getChildrenCommentsByParentAndVideoAndLessonUser(int parentId, int videoId, int lessonId) {
        return commentRepository.findChildrenByParentIdAndVideoAndLesson(parentId, videoId, lessonId);
    }

    public Comment deleteComment(int commentId) {
        // Tìm comment theo ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + commentId + " not found."));

        // Kiểm tra nếu comment đã bị xóa
        if (comment.getIsDeleted()) {
            throw new IllegalStateException("Comment with ID " + commentId + " has already been deleted.");
        }

        // Đánh dấu comment là đã xóa
        comment.setDeletedAt(LocalDateTime.now());
        comment.setIsDeleted(true);

        // Lưu thay đổi
        return commentRepository.save(comment);
    }

    public LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())  // Chuyển đổi Instant sang ZoneId hệ thống mặc định
                .toLocalDateTime();  // Chuyển đổi thành LocalDateTime
    }

    private CommentUserDTO convertToDTO2(Comment comment) {
        List<CommentUserDTO> replies = (comment.getReplies() != null ? comment.getReplies().stream()
                .map(reply -> new CommentUserDTO(reply.getId(), reply.getContent(),
                        reply.getAccount().getId(), convertDateToLocalDateTime(reply.getCreatedAt()),
                        new ArrayList<>()))
                .collect(Collectors.toList()) : new ArrayList<>()); // Nếu null, sử dụng một danh sách rỗng

        return new CommentUserDTO(comment.getId(), comment.getContent(),
                comment.getAccount().getId(), convertDateToLocalDateTime(comment.getCreatedAt()), replies);
    }


    public CommentUserDTO addCommentFromDTOUser(CommentDTO_User commentDTO) {
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

        comment.setDeletedAt(LocalDateTime.now());
        comment.setIsDeleted(false);

        // Nếu có contentId, thiết lập bình luận cha
        if (commentDTO.getContentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getContentId())
                    .orElseThrow(() -> new IllegalArgumentException("Bình luận cha không tồn tại"));
            comment.setParentComment(parentComment);
        }


        // Lưu bình luận vào cơ sở dữ liệu
        Comment savedComment = commentRepository.save(comment);

        // Chuyển đổi Comment thành CommentUserDTO và trả về
        return convertToDTO2(savedComment);

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
            account.setIsDeleted(true);
            account.setDeletedAt(LocalDateTime.now());
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
            account.setIsDeleted(false);
            account.setDeletedAt(LocalDateTime.now());
            // Lưu thay đổi
            return commentRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + commentID);
        }
    }

    @Transactional
    public Comment activeCommentAdmin(int commentID) {
        Optional<Comment> commentOpt = commentRepository.findById(commentID);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();

            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found with id: " + commentID);
        }
    }


    @Transactional
    public Comment unactiveCommentAdmin(int commentID) {
        Optional<Comment> commentOpt = commentRepository.findById(commentID);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();

            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found with id: " + commentID);
        }
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminCommentDTORestoreList> getComments(String content, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = commentRepository.findComments(content, deletedDate, pageable);
        List<AdminCommentDTORestoreList> adminBlogDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminCommentDTORestoreList dto = new AdminCommentDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setContent((String) result[1]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[2]);
            dto.setCreatedAt(createAt);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsApproved((Boolean) result[4]);
            dto.setIsDeleted((Boolean) result[5]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[6]);
            dto.setUpdatedAt(updateAt);

            dto.setAccId((Integer) result[7]);
            dto.setContentId((Integer) result[8]);
            dto.setLessonId((Integer) result[9]);
            dto.setVideoId((Integer) result[10]);

            adminBlogDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminBlogDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Comment updateRestoreComment(AdminCommentDTORestoreList adminCourseDTORestoreList) {
        Optional<Comment> accountOptional = commentRepository.findById(adminCourseDTORestoreList.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Chapter not found with id: " + adminCourseDTORestoreList.getId());
        } else {
            Comment comment = accountOptional.get();
            comment.setIsDeleted(false);
            return commentRepository.save(comment);
        }
    }

    public void deleteRestoreComment(AdminCommentDTORestoreList adminCommentDTORestoreList) {
        Optional<Comment> commentOptional = commentRepository.findById(adminCommentDTORestoreList.getId());
        if (commentOptional.isEmpty()) {
            throw new RuntimeException("Chapter not found with id: " + adminCommentDTORestoreList.getId());
        } else {
            commentRepository.delete(commentOptional.get());
        }
    }

    public CommentItemDTO convertToCommentItemDTO(Comment comment) {
        CommentItemDTO commentItemDTO = new CommentItemDTO();

        // Ánh xạ dữ liệu từ entity Comment sang DTO
        commentItemDTO.setId(String.valueOf(comment.getId()));
        commentItemDTO.setContent(comment.getContent());
        commentItemDTO.setAuthorName(comment.getAccount().getFullname());
        commentItemDTO.setAuthorId(String.valueOf(comment.getAccount().getId()));
        commentItemDTO.setAuthorRole(comment.getAccount().getRole().getRoleName()); // student, teacher, admin
        commentItemDTO.setTargetType(comment.getTargetType().name()); // 'course', 'lesson', 'material', 'article'

        commentItemDTO.setStatus(comment.getStatus().name()); // 'published', 'pending', 'rejected'
        commentItemDTO.setCreatedAt(comment.getCreatedAt().toString());

        commentItemDTO.setReplies(comment.getReplies().size());

        // Optionally add replyTo if this comment is a reply
        if (comment.getParentComment() != null) {
            commentItemDTO.setReplyTo(String.valueOf(comment.getParentComment().getId()));
        }

        // You can also include author avatar if required
        commentItemDTO.setAuthorAvatar(comment.getAccount().getImage());

        return commentItemDTO;
    }

    // Phương thức lấy danh sách bình luận với các bộ lọc và phân trang
    public Page<CommentItemDTO> getCommentsWithFilters(String content, Comment.Status status, Comment.TargetType targetType, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllWithFilters(content, status, targetType, pageable);
        return comments.map(this::convertToCommentItemDTO);
    }

    public Comment replyToComment(int commentId, String content, int adminId) {
        // Lấy bình luận gốc mà Admin sẽ trả lời
        Optional<Comment> parentCommentOptional = commentRepository.findById(commentId);
        if (!parentCommentOptional.isPresent()) {
            throw new IllegalArgumentException("Comment not found.");
        }

        Comment parentComment = parentCommentOptional.get();

        // Kiểm tra xem người trả lời có phải là Admin không
        Optional<Account> accountOptional = accountRepository.findById(adminId);
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("Admin not found.");
        }

        Account admin = accountOptional.get();

        // Tạo bình luận trả lời
        Comment replyComment = new Comment();
        replyComment.setContent(content);
        replyComment.setParentComment(parentComment); // Gán bình luận gốc
        replyComment.setAccount(admin); // Gán Admin làm người trả lời
        replyComment.setStatus(Comment.Status.PUBLISHED); // Trạng thái bình luận
        replyComment.setCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now())); // Ghi lại thời gian
        replyComment.setUpdatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now())); // Cập nhật thời gian
        replyComment.setVideo(parentComment.getVideo());
        replyComment.setTargetType(parentComment.getTargetType());
        replyComment.setLesson(parentComment.getLesson());
        replyComment.setIsDeleted(false); // Mặc định không bị xóa
        replyComment.setDeletedAt(null); // Không có thời gian xóa


        // Lưu bình luận trả lời
        return commentRepository.save(replyComment);
    }

    public Comment updateStatusComment(Integer id, String status) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // Cập nhật trạng thái
        comment.setStatus(Comment.Status.valueOf(status));
        comment.setUpdatedAt(new java.util.Date()); // Cập nhật thời gian

        // Lưu bình luận sau khi thay đổi trạng thái
        return commentRepository.save(comment);
    }

    // Phương thức để cập nhật trạng thái cho nhiều bình luận
    public List<Comment> bulkUpdateStatus(List<Integer> ids, String status) {
        List<Comment> comments = commentRepository.findAllById(ids);

        // Cập nhật trạng thái cho các bình luận
        comments.forEach(comment -> {
            comment.setStatus(Comment.Status.valueOf(status));
            comment.setUpdatedAt(new java.util.Date());  // Cập nhật thời gian sửa đổi
        });

        // Lưu các bình luận đã cập nhật
        return commentRepository.saveAll(comments);
    }

    // Phương thức để xóa nhiều bình luận
    public boolean bulkDeleteComments(List<Integer> ids) {
        List<Comment> commentsToDelete = commentRepository.findAllById(ids);

        if (commentsToDelete.isEmpty()) {
            return false;  // Không có bình luận nào để xóa
        }

        // Đánh dấu bình luận là đã bị xóa
        commentsToDelete.forEach(comment -> {
            comment.setIsDeleted(true);
            comment.setDeletedAt(java.time.LocalDateTime.now());
        });

        // Lưu các bình luận đã xóa
        commentRepository.saveAll(commentsToDelete);
        return true;
    }
}
