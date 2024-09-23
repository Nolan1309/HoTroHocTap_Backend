package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "course_categories")
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Integer> {
}
