package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "course_bundles")
public class CourseBundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title; // Tiêu đề combo
    private String description; // Mô tả ngắn
    private BigDecimal price; // Giá combo (thấp hơn tổng giá các khóa lẻ)
    private String imageUrl; // Hình ảnh đại diện combo
    private boolean status = true; // Trạng thái (active/inactive)
    private boolean isDeleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bundle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseBundleItem> bundleItems;

    @OneToMany(mappedBy = "bundle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Enrolled_Courses> enrolled_courses;

    //Đảm bảo quan hệ Cha con
    public void updateBundleItems(List<CourseBundleItem> newItems) {
        this.bundleItems.clear(); // orphanRemoval sẽ xóa các item cũ khỏi DB
        for (CourseBundleItem item : newItems) {
            item.setBundle(this); // đảm bảo quan hệ 2 chiều đúng
            this.bundleItems.add(item);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
