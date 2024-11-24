package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

@Data
public class AdminDiscountGetDTO {
    private Integer id;
    private String description;
    private Double discountValue;
    private String title;
    private Boolean isDeleted;

    // Constructor
    public AdminDiscountGetDTO(Integer id, String description,Double discountValue, String title, Boolean isDeleted) {
        this.id = id;
        this.description = description;
        this.discountValue = discountValue;
        this.title = title;
        this.isDeleted = isDeleted;
    }
}
