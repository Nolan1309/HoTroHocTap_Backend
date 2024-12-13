package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDetailDTO_User {
    private LocalDateTime paymentDate;
    private BigDecimal totalPayment;
    private String paymentMethod;
    private Integer accountId;
    private Integer paymentDetailId;
    private Integer courseId;
    private String courseTitle;
    private BigDecimal coursePrice;
    private String courseAuthor;
    private String courseLanguage;
    private String courseName;
    private String courseDescription;
    private Integer courseDuration;

    public PaymentDetailDTO_User(LocalDateTime paymentDate, BigDecimal totalPayment, String paymentMethod, Integer accountId, Integer paymentDetailId, Integer courseId, String courseTitle, BigDecimal coursePrice, String courseAuthor, String courseLanguage, String courseName, String courseDescription, Integer courseDuration) {
        this.paymentDate = paymentDate;
        this.totalPayment = totalPayment;
        this.paymentMethod = paymentMethod;
        this.accountId = accountId;
        this.paymentDetailId = paymentDetailId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.coursePrice = coursePrice;
        this.courseAuthor = courseAuthor;
        this.courseLanguage = courseLanguage;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseDuration = courseDuration;
    }
}
