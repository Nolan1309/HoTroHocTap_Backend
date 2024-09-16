package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "methodPay")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pay_title")
    private String pay_title;

    @Column(name = "pay_description")
    private String pay_description;

//    @OneToMany(mappedBy = "paymentMethod",
//            fetch = FetchType.LAZY
//            , cascade = {
//            CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH
//    })
//    private List<Payment> paymentList;
}
