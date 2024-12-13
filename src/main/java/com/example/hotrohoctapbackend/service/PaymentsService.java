package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminPaymentDTO;
import com.example.hotrohoctapbackend.DTO.Admin.DashboardReportDto;
import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.DTO.User.PaymentDetailDTO_User;
import com.example.hotrohoctapbackend.DTO.User.PaymentSummaryDTO_User;
import com.example.hotrohoctapbackend.dao.PaymentRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public PaymentsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<AdminPaymentDTO> getPaymentAdmin(Pageable pageable) {
        Page<Object[]> rawResults = paymentRepository.findPaymentAdmin(pageable);

        return rawResults.map(result ->
                new AdminPaymentDTO(
                        (Integer) result[0],                   // payment_id
                        (String) result[1],                 // buyer_name
                        (BigDecimal) result[2],             // total_payment
                        (Date) result[3],                   // payment_date
                        (String) result[4]                  // payment_method
                )
        );
    }

    public Page<PaymentSummaryDTO_User> getPaymentSummariesByAccountId(Long accountId, int page, int size) {
        int offset = page * size;
        List<Object[]> results = paymentRepository.findPaymentSummariesByAccountIdUser(accountId, offset, size);

        List<PaymentSummaryDTO_User> paymentSummaries = results.stream().map(record -> {
            PaymentSummaryDTO_User dto = new PaymentSummaryDTO_User();
            dto.setPaymentId(((Integer) record[0]));
            dto.setPaymentDate(((Timestamp) record[1]).toLocalDateTime());
            dto.setTotalPayment((BigDecimal) record[2]);
            dto.setCourseCount(((Long) record[3]));
            dto.setPaymentMethod((String) record[4]);
            return dto;
        }).collect(Collectors.toList());

        long totalElements = paymentRepository.countPaymentsByAccountIdUser(accountId);

        return new PageImpl<>(paymentSummaries, PageRequest.of(page, size), totalElements);
    }

    public List<PaymentDetailDTO_User> getPaymentDetailsById_User(Integer paymentId) {
        return paymentRepository.findPaymentDetailsByPaymentIdUser(paymentId).stream()
                .map(result -> new PaymentDetailDTO_User(
                        (LocalDateTime) result[0],  // paymentDate
                        (BigDecimal) result[1],    // totalPayment
                        (String) result[2],        // paymentMethod
                        (Integer) result[3],       // accountId
                        (Integer) result[4],       // paymentDetailId
                        (Integer) result[5],       // courseId
                        (String) result[6],        // courseTitle
                        (BigDecimal) result[7],    // coursePrice
                        (String) result[8],        // courseAuthor
                        (String) result[9],
                        // courseLanguage
                        (String) result[10],       // courseName
                        (String) result[11],       // courseDescription
                        (Integer) result[12]       // courseDuration
                ))
                .collect(Collectors.toList());
    }

    public DashboardReportDto getDashboardReport() {
        String currentDate = LocalDate.now().toString(); // Lấy ngày hiện tại
        List<Object[]> result = paymentRepository.getDashboardReport(currentDate);

        if (!result.isEmpty()) {
            Object[] row = result.get(0);

            return new DashboardReportDto(
                    ((Number) row[0]).doubleValue(),
                    ((Number) row[1]).intValue(),
                    ((Number) row[2]).intValue(),
                    ((Number) row[3]).intValue(),
                    ((Number) row[4]).intValue(),
                    ((Number) row[5]).intValue()
            );
        }

        return new DashboardReportDto(0, 0, 0, 0, 0, 0);
    }

    public List<Object[]> getMonthlySalesData(int year) {
        return paymentRepository.getMonthlySalesData(year);
    }
}
