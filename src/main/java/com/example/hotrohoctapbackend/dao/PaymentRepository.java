package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.AdminPaymentDTO;
import com.example.hotrohoctapbackend.DTO.AdminPaymentDTO;
import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(value = "SELECT p.id AS payment_id, a.fullname AS buyer_name, p.total_payment, p.payment_date, mp.pay_title AS payment_method " +
            "FROM payments p " +
            "JOIN account a ON p.account_id = a.id " +
            "JOIN method_pay mp ON p.method_id = mp.id", nativeQuery = true)
    List<Object[]> findPayment();

}
