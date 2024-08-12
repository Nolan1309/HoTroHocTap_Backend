package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "chapters")
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
}
