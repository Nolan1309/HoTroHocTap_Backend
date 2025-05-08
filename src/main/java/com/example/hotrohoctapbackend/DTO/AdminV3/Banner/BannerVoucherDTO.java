package com.example.hotrohoctapbackend.DTO.AdminV3.Banner;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerVoucherDTO {
    private Integer id;
    private String title;
    private String imageUrl;
    private String link;
    private String position;
    private String platform;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean status;
    private Integer priority;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer accountId;
}
