package com.example.hotrohoctapbackend.DTO.AdminV3.Category;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryDTOAdmin {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String type;
    @Getter
    private String description;
    @Getter
    private Integer parentId;
    @Getter
    private int level;
    @Getter
    private int orderIndex;
    @Getter
    private String status;
    @Getter
    private int itemCount;
    private boolean isDeleted;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime updatedAt;
    @Getter
    private List<CategoryDTOAdmin> children;

    public CategoryDTOAdmin() {
    }

    public CategoryDTOAdmin(int id, String name, String type, String description, Integer parentId, int level,
                            int orderIndex, String status, int itemCount, boolean isDeleted, LocalDateTime createdAt,
                            LocalDateTime updatedAt, List<CategoryDTOAdmin> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.parentId = parentId;
        this.level = level;
        this.orderIndex = orderIndex;
        this.status = status;
        this.itemCount = itemCount;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.children = children;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setChildren(List<CategoryDTOAdmin> children) {
        this.children = children;
    }
}
