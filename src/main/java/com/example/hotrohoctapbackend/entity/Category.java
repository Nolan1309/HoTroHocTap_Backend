package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(name = "level")
    private int level;

    @Column(name = "order_index")
    private int orderIndex; // Thêm chỉ số sắp xếp

    @Column(name = "description")
    private String description; // Thêm mô tả cho category

    @Column(name = "status")
    private String status; // 'active' | 'inactive'

    @Column(name = "item_count")
    private int itemCount;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<Category> children;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Course> courseList;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Blog> blogList;

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
        if (this.type == null) {
            this.type = "document"; // Giá trị mặc định
        }
        if (this.status == null) {
            this.status = "active"; // Giá trị mặc định cho status
        }
    }

}
