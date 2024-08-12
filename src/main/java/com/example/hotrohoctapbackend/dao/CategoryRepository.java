package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "categorys")
public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
