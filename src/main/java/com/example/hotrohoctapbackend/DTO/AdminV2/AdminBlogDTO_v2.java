package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class AdminBlogDTO_v2 {
    private Long blogId;                  // ID của blog
    private String title;                 // Tiêu đề của blog
    private String content;               // Nội dung của blog
    private String authorName;            // Tên tác giả
    private Long level3Id;                // ID danh mục cấp 3
    private String categoryNameLevel3;    // Tên danh mục cấp 3
    private Long level2Id;                // ID danh mục cấp 2
    private String categoryNameLevel2;    // Tên danh mục cấp 2
    private Long level1Id;                // ID danh mục cấp 1
    private String categoryNameLevel1;    // Tên danh mục cấp 1
    private Boolean status;                // Trạng thái của blog
    private Boolean isDeleted;            // Trạng thái xóa của blog

    public AdminBlogDTO_v2(Long blogId, String title, String content, String authorName, Long level3Id, String categoryNameLevel3, Long level2Id, String categoryNameLevel2, Long level1Id, String categoryNameLevel1, Boolean status, Boolean isDeleted) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.level3Id = level3Id;
        this.categoryNameLevel3 = categoryNameLevel3;
        this.level2Id = level2Id;
        this.categoryNameLevel2 = categoryNameLevel2;
        this.level1Id = level1Id;
        this.categoryNameLevel1 = categoryNameLevel1;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getLevel3Id() {
        return level3Id;
    }

    public void setLevel3Id(Long level3Id) {
        this.level3Id = level3Id;
    }

    public String getCategoryNameLevel3() {
        return categoryNameLevel3;
    }

    public void setCategoryNameLevel3(String categoryNameLevel3) {
        this.categoryNameLevel3 = categoryNameLevel3;
    }

    public Long getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(Long level2Id) {
        this.level2Id = level2Id;
    }

    public String getCategoryNameLevel2() {
        return categoryNameLevel2;
    }

    public void setCategoryNameLevel2(String categoryNameLevel2) {
        this.categoryNameLevel2 = categoryNameLevel2;
    }

    public Long getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(Long level1Id) {
        this.level1Id = level1Id;
    }

    public String getCategoryNameLevel1() {
        return categoryNameLevel1;
    }

    public void setCategoryNameLevel1(String categoryNameLevel1) {
        this.categoryNameLevel1 = categoryNameLevel1;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
