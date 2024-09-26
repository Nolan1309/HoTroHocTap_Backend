package com.example.hotrohoctapbackend.controller;


import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.entity.Payment;
import com.example.hotrohoctapbackend.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentsService paymentService;

    @PostMapping("/add")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentResponseDTO payment) {
        Payment createdPayment = paymentService.createPayment(payment);

        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(createdPayment.getId());
        dto.setPaymentMethod(createdPayment.getPaymentMethod());
        dto.setPayment_date(createdPayment.getPayment_date());
        dto.setTotal_payment(createdPayment.getTotal_payment());
        dto.setAccount_id(createdPayment.getAccount().getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable("id") Integer id) {
        PaymentResponseDTO payment = paymentService.getPayment(id);

        // Kiểm tra nếu payment là null thì trả về 404
        if (payment != null) {
            return new ResponseEntity<>(payment, HttpStatus.OK); // Trả về Payment và mã 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Trả về mã 404 Not Found
        }
    }

}