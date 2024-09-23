package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.dao.PaymentRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentsService {


    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(PaymentResponseDTO payment) {

        Payment entity = new Payment();
        entity.setId(payment.getId());
        entity.setPayment_date(payment.getPayment_date());
        entity.setPaymentMethod(payment.getPaymentMethod());
        entity.setTotal_payment(payment.getTotal_payment());
        Account account = new Account();
        account.setId(payment.getAccount_id());
        entity.setAccount(account);
        return paymentRepository.save(entity);
    }

    public PaymentResponseDTO getPayment(Integer id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.get().getId());
        dto.setPayment_date(payment.get().getPayment_date());
        dto.setPaymentMethod(payment.get().getPaymentMethod());
        dto.setTotal_payment(payment.get().getTotal_payment());
        dto.setAccount_id(payment.get().getAccount().getId());
        return dto;
    }
}
