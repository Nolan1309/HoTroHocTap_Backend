package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PaymentDetail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "paymentdetail")
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail,Integer> {
    List<PaymentDetail> findPaymentByPaymentId(Integer id);
    @Query(value = "SELECT c.courses_title, pd.price " +
            "FROM payments_detail pd " +
            "JOIN courses c ON pd.course_id = c.id " +
            "WHERE pd.payment_id = :paymentId", nativeQuery = true)
    List<Object[]> findCoursePaymentDetailsByPaymentId(@Param("paymentId") Integer paymentId);

    @Query(value = """
        SELECT 
            c.id AS courseId,
            dt.course_title AS courseTitle,
            c.image_url AS imageUrl,
            dt.price AS price
        FROM payments_detail dt
        INNER JOIN payments p ON p.id = dt.payment_id
        INNER JOIN courses c ON c.id = dt.course_id
        WHERE p.id = :paymentId
    """, nativeQuery = true)
    List<Object[]> findCourseDetailsByPaymentId(@Param("paymentId") Integer paymentId);
}
