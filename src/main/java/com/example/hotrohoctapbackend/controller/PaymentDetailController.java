package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminPaymentDetailDTO;
import com.example.hotrohoctapbackend.service.PaymentDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PaymentDetailController {

    private final PaymentDetailService paymentDetailService;

    public PaymentDetailController(PaymentDetailService paymentDetailService) {
        this.paymentDetailService = paymentDetailService;
    }
    @GetMapping("/payment-details/{paymentId}")
    public List<AdminPaymentDetailDTO> getCoursePaymentDetailsByPaymentId(@PathVariable Integer paymentId) {
        return paymentDetailService.getCoursePaymentDetailsByPaymentId(paymentId);
    }
}
