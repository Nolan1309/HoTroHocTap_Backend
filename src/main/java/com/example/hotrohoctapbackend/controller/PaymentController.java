package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminPaymentDTO;
import com.example.hotrohoctapbackend.service.PaymentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    public List<AdminPaymentDTO> getPayment() {
        return paymentService.getPayment();
    }
}
