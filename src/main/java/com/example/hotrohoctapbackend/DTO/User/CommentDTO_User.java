package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class CommentDTO_User {
    private String content;       // Nội dung bình luận
    private int accId;            // ID tài khoản người bình luận
    private int lessonId;         // ID bài học
    private int videoId;          // ID video
    private Integer contentId;    // ID bình luận cha (nullable)
}