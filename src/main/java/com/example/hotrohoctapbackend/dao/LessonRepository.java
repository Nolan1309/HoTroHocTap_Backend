package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "lessons")
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
}
