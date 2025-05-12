package com.example.hotrohoctapbackend.entity;


import com.example.hotrohoctapbackend.enums.BannerPosition;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "banner_vouchers")
public class BannerVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")// Tự động tăng
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link")
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private BannerPosition position; // Position của banner

    @Column(name = "platform")
    @Enumerated(EnumType.STRING) // Enum để đảm bảo tính chính xác cho platform
    private Platform platform;

    @Column(name = "type")
    @Enumerated(EnumType.STRING) // Enum để đảm bảo tính chính xác cho type
    private BannerType type;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Platform {
        WEB, MOBILE
    }

    // Enum cho Banner Type (Regular, Voucher)
    public enum BannerType {
        REGULAR, VOUCHER
    }

    @ManyToOne
    @JoinColumn(name = "account_id")  // Tạo liên kết với Account (người tạo Banner)
    private Account account;
}
