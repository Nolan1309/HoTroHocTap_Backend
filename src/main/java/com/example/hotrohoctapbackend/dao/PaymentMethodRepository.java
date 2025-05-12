package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PaymentMethod;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "payment_method")
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, CriteriaBuilder.In> {
}
