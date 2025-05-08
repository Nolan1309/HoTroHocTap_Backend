package com.example.hotrohoctapbackend.DTO.AdminV3.Category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTOPublic {
    private int id;
    private String name;
    private int level;
    private String type;
    private String description;
    private int itemCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTOPublic() {
    }

    public CategoryDTOPublic(int id, String name, int level, String type, String description, int itemCount, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.type = type;
        this.description = description;
        this.itemCount = itemCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
