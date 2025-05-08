package com.example.hotrohoctapbackend.DTO.AdminV3.Category;

import lombok.Data;

@Data
public class CategoryDTOCreateRequest {
    private Integer id;
    private String name;
    private String type;
    private String description;
    private Integer parentId;
    private Integer level;
    private Integer orderIndex;
    private String status;
    private Integer itemCount;
    private String createdAt;
    private String updatedAt;

    public CategoryDTOCreateRequest(Integer id, String name, String type, String description, Integer parentId, Integer level, Integer orderIndex, String status, Integer itemCount, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.parentId = parentId;
        this.level = level;
        this.orderIndex = orderIndex;
        this.status = status;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CategoryDTOCreateRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
