package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "enrolled_courses")
public interface Enrolled_CoursesRepository extends JpaRepository<Enrolled_Courses,Integer> {
}
