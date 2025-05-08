package com.example.hotrohoctapbackend.DTO.AdminV3.Comment;


public class ReplyCommentAdmin {
    private String content; // Nội dung trả lời bình luận
    private Integer adminId; // ID của admin trả lời bình luận

    public ReplyCommentAdmin() {
    }

    public ReplyCommentAdmin(String content, Integer adminId) {
        this.content = content;
        this.adminId = adminId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}