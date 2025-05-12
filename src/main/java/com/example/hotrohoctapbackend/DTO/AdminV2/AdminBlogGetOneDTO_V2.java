package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class AdminBlogGetOneDTO_V2 {
    private String title;
    private String content;
    private String image;
    private Boolean status;
    private Integer level_3_id;
    private Integer level_2_id;
    private Integer level_1_id;

    public AdminBlogGetOneDTO_V2(String title, String content, String image, Boolean status, Integer level_3_id, Integer level_2_id, Integer level_1_id) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.status = status;
        this.level_3_id = level_3_id;
        this.level_2_id = level_2_id;
        this.level_1_id = level_1_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getLevel_3_id() {
        return level_3_id;
    }

    public void setLevel_3_id(Integer level_3_id) {
        this.level_3_id = level_3_id;
    }

    public Integer getLevel_2_id() {
        return level_2_id;
    }

    public void setLevel_2_id(Integer level_2_id) {
        this.level_2_id = level_2_id;
    }

    public Integer getLevel_1_id() {
        return level_1_id;
    }

    public void setLevel_1_id(Integer level_1_id) {
        this.level_1_id = level_1_id;
    }
}
