package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
