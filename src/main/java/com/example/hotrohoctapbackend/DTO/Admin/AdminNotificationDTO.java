package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminNotificationDTO {
    private Integer id;
    private Boolean isDeleted;
    private String message;
    private String title;
    private String topic;

    // Constructor
    public AdminNotificationDTO(Integer id, Boolean isDeleted, String message, String title, String topic) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.message = message;
        this.title = title;
        this.topic = topic;
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
