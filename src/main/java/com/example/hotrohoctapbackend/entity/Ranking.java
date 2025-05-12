package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.PeriodType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ranking")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  // ID của bản ghi xếp hạng

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;  // Người dùng

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;  // Loại kỳ (WEEKLY, MONTHLY)

//    @Column(name = "period_value")
//    private String periodValue;  // Kỳ xếp hạng (VD: '2025-05')

    @Column(name = "total_points")
    private Integer totalPoints;  // Tổng điểm của người dùng

    @Column(name = "ranking")
    private Integer ranking;  // Thứ hạng của người dùng

    @Column(name = "status")
    private Boolean status;  // Trạng thái tính toán xếp hạng (true nếu đã tính, false nếu chưa)


    @Column(name = "created_at")
    private LocalDateTime createdAt;  // Thời gian tạo xếp hạng

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
