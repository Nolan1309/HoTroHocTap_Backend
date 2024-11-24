package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.DTO.User.PaymentDetailDTO_User;
import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    @Query(value = "SELECT p.id AS payment_id, a.fullname AS buyer_name, p.total_payment, p.payment_date, p.payment_method AS payment_method " +
            "FROM payments p " +
            "JOIN account a ON p.account_id = a.id ", nativeQuery = true)
    List<Object[]> findPayment();


    @Query(value = """
        SELECT 
            p.id AS paymentId, 
            p.payment_date AS paymentDate, 
            p.total_payment AS totalPayment, 
            COUNT(pd.course_id) AS courseCount, 
            p.payment_method AS paymentMethod
        FROM 
            payments p
        INNER JOIN 
            payments_detail pd ON p.id = pd.payment_id
        WHERE 
            p.account_id = :accountId
        GROUP BY 
            p.id, p.payment_date, p.total_payment, p.payment_method
        ORDER BY 
            p.payment_date DESC
        LIMIT :offset, :pageSize
    """, nativeQuery = true)
    List<Object[]> findPaymentSummariesByAccountIdUser(
            @Param("accountId") Long accountId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    @Query(value = """
        SELECT COUNT(DISTINCT p.id)
        FROM payments p
        WHERE p.account_id = :accountId
    """, nativeQuery = true)
    long countPaymentsByAccountIdUser(@Param("accountId") Long accountId);


    @Query(value = """
        SELECT 
            p.payment_date AS paymentDate,
            p.total_payment AS totalPayment,
            p.paymentMethod AS paymentMethod,
            p.account_id AS accountId,
            pd.id AS paymentDetailId,
            pd.course_id AS courseId,
            pd.course_title AS courseTitle,
            pd.price AS coursePrice,
            c.author AS courseAuthor,
            c.language AS courseLanguage,
            c.courses_title AS courseName,
            c.description AS courseDescription,
            c.duration AS courseDuration
        FROM payments p
        LEFT JOIN payments_detail pd ON p.id = pd.payment_id
        LEFT JOIN courses c ON pd.course_id = c.id
        WHERE p.id = :paymentId
    """, nativeQuery = true)
    List<Object[]> findPaymentDetailsByPaymentIdUser(@Param("paymentId") Integer paymentId);
}
