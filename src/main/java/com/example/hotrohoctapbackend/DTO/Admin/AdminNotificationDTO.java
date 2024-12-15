package com.example.hotrohoctapbackend.DTO.Admin;

import java.time.LocalDateTime;

public class AdminNotificationDTO {
    private Integer id;
    private Boolean isDeleted;
    private String message;
    private String title;
    private String topic;
    private LocalDateTime createAt;
    private LocalDateTime deletedDate;
    private LocalDateTime updateAt;
    // Constructor
    public AdminNotificationDTO(Integer id, Boolean isDeleted, String message, String title, String topic) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.message = message;
        this.title = title;
        this.topic = topic;
    }

    public AdminNotificationDTO(Integer id, Boolean isDeleted, String message, String title, String topic, LocalDateTime createAt, LocalDateTime deletedDate, LocalDateTime updateAt) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.message = message;
        this.title = title;
        this.topic = topic;
        this.createAt = createAt;
        this.deletedDate = deletedDate;
        this.updateAt = updateAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
