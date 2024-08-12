package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "paymentdetail")
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail,Integer> {
}
