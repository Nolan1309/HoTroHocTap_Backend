package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "blogcategorys")
public interface BlogCategoryRepository extends JpaRepository<BlogCategory,Integer> {
}
