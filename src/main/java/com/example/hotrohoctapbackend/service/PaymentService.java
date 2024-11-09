package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminPaymentDTO;
import com.example.hotrohoctapbackend.dao.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<AdminPaymentDTO> getPayment() {
        List<Object[]> rawResults = paymentRepository.findPayment();

        return rawResults.stream().map(result ->
                new AdminPaymentDTO(
                        (Integer) result[0],                   // payment_id
                        (String) result[1],                 // buyer_name
                        (BigDecimal) result[2],             // total_payment
                        (Date) result[3],                   // payment_date
                        (String) result[4]                  // payment_method
                )
        ).collect(Collectors.toList());
    }
}
