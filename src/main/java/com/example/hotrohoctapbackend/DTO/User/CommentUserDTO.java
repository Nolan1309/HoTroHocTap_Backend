package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentUserDTO {
    @Getter
    private int id;
    @Getter
    private String content;

    @Getter
    private Integer accountId;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private List<CommentUserDTO> replies;

//    public CommentUserDTO(int id, String content, boolean isApproved, LocalDateTime createdAt, List<CommentUserDTO> replies) {
//        this.id = id;
//        this.content = content;
//        this.isApproved = isApproved;
//        this.createdAt = createdAt;
//        this.replies = replies;
//    }

    public CommentUserDTO(int id, String content, Integer accountId, LocalDateTime createdAt, List<CommentUserDTO> replies) {
        this.id = id;
        this.content = content;

        this.accountId = accountId;
        this.createdAt = createdAt;
        this.replies = replies;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<CommentUserDTO> getReplies() {
        return replies;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setReplies(List<CommentUserDTO> replies) {
        this.replies = replies;
    }

    public CommentUserDTO() {
    }
}
