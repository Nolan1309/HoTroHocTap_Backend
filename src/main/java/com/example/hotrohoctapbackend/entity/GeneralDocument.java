package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "general_documents")
public class GeneralDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image_url;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "format")
    private String format;

    @Column(name = "size")
    private String size;

    @Column(name = "view")
    private int view;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "generalDocument", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GeneralDocument_Acount> generalDocumentAcounts;

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }
}