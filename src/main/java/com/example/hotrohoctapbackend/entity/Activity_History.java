package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "activity_history")
public class Activity_History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;  // Người dùng thực hiện hành động

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;  // Loại hành động

    @Column(name = "description")
    private String description;  // Mô tả chi tiết hành động

    @Column(name = "timestamp")
    private LocalDateTime timestamp;  // Thời gian thực hiện hành động

    @Column(name = "additional_data")
    private String additionalData;  // Dữ liệu bổ sung (ví dụ: khóa học, bài thi, video)

    @PrePersist
    protected void onCreate() {
        // Đặt thời gian thực hiện hành động mặc định là thời gian hiện tại
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }


}
