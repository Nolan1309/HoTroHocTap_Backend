package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.DiscountFormat;
import com.example.hotrohoctapbackend.enums.DiscountStatus;
import com.example.hotrohoctapbackend.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    // Định dạng cho Discount hay Voucher
    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private DiscountFormat format;

    //Chỉ xài cho Discount , giảm cho Course hay Test
    @Enumerated(EnumType.STRING)
    @Column(name = "discountType")
    private DiscountType discountType;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "start_date")
    private LocalDateTime startedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DiscountStatus status;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    // Có giá trị khi giảm giá là Voucher
    @Column(name = "max_used")
    private Integer maxUsed;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "minOrderValue")
    private BigDecimal minOrderValue;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Course_Discount> courseDiscounts;

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }
}
