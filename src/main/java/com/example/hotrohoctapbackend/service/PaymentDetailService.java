package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminPaymentDetailDTO;
import com.example.hotrohoctapbackend.dao.PaymentDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentDetailService {

    private final PaymentDetailRepository paymentDetailRepository;

    public PaymentDetailService(PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailRepository = paymentDetailRepository;
    }

    public List<AdminPaymentDetailDTO> getCoursePaymentDetailsByPaymentId(Integer paymentId) {
        List<Object[]> rawResults = paymentDetailRepository.findCoursePaymentDetailsByPaymentId(paymentId);

        return rawResults.stream().map(result ->
                new AdminPaymentDetailDTO(
                        (String) result[0],      // course_name
                        ((Number) result[1]).doubleValue()  // price
                )
        ).collect(Collectors.toList());
    }
}