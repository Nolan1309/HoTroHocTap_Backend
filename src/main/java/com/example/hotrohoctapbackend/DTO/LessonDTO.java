package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class LessonDTO {
    private int id;
    private String title;
    private Boolean deleted;

    private Boolean status;

    private String topic;

    private String isTestExcluded;

    // Constructors
    public LessonDTO() {
    }

    public LessonDTO(int id, String title, Boolean deleted, Boolean status, String topic , String isTestExcluded) {
        this.id = id;
        this.title = title;
        this.deleted = deleted;
        this.status = status;
        this.topic = topic;
        this.isTestExcluded = isTestExcluded;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIsTestExcluded() {
        return isTestExcluded;
    }

    public void setIsTestExcluded(String isTestExcluded) {
        this.isTestExcluded = isTestExcluded;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
