package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cat_blog_id")
    private BlogCategory category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Account author;
    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;
    @Column(name = "isDeleted")
    private boolean isDeleted;
}