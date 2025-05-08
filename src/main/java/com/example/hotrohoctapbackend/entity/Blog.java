package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cat_blog_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Account author;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    // Tóm tắt bài viết
    @Column(name = "summary", columnDefinition = "LONGTEXT")
    private String summary;

    // Đánh dấu bài viết nổi bật
    @Column(name = "featured")
    private Boolean featured = false;

    // Số lượt xem
    @Column(name = "views")
    private Integer views = 0;

    // Số lượng bình luận
    @Column(name = "comment_count")
    private Integer commentCount = 0;
    

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }

}