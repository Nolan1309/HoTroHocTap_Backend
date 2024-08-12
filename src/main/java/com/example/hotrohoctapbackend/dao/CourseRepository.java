package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course,Integer> {
}
