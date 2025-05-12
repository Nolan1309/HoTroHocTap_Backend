package com.example.hotrohoctapbackend.DTO.AdminV3.Bundle;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComboPackageDTOListSimple {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
