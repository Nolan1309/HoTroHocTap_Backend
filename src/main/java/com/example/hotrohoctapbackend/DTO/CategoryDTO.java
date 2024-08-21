package com.example.hotrohoctapbackend.DTO;

public class CategoryDTO {
    private int id;
    private int level;
    private String name;
    private Long parentId;

    public CategoryDTO(int id, int level, String name, Long parentId) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
