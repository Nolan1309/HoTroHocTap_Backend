package com.example.hotrohoctapbackend.DTO.AdminV3.Ranking;

import com.example.hotrohoctapbackend.enums.PeriodType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RankingDTO {
    private Long id;  // ID của bản ghi xếp hạng
    private String avatar;
    private Integer accountId;
    private String accountName;  // Tên người dùng
    private PeriodType periodType;  // Loại kỳ (WEEKLY, MONTHLY)
    //    private String periodValue;  // Kỳ xếp hạng (VD: '2025-05')
    private int totalPoints;  // Tổng điểm của người dùng
    private int ranking;  // Thứ hạng của người dùng
    private boolean status;  // Trạng thái tính toán xếp hạng (true nếu đã tính, false nếu chưa)
    private LocalDateTime createdAt;  // Thời gian tạo xếp hạng
    private LocalDateTime updatedAt;
}
