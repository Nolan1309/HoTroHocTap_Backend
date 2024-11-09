package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PaymentDetail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "paymentdetail")
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {

    @Query(value = "SELECT c.courses_title, pd.price " +
            "FROM payments_detail pd " +
            "JOIN courses c ON pd.course_id = c.id " +
            "WHERE pd.payment_id = :paymentId", nativeQuery = true)
    List<Object[]> findCoursePaymentDetailsByPaymentId(@Param("paymentId") Integer paymentId);
}
