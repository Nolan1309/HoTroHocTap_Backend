package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Course_Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "course_discounts")
public interface Course_DiscountRepository extends JpaRepository<Course_Discount,Integer> {
}
