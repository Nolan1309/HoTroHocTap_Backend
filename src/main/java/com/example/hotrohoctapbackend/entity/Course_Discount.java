package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "course_discounts")
public class Course_Discount {
    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;
    @Column(name = "isDeleted")
    private boolean isDeleted;
}
