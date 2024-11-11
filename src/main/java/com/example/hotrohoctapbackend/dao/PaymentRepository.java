package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    @Query(value = "SELECT p.id AS payment_id, a.fullname AS buyer_name, p.total_payment, p.payment_date, p.payment_method AS payment_method " +
            "FROM payments p " +
            "JOIN account a ON p.account_id = a.id ", nativeQuery = true)
    List<Object[]> findPayment();
}
