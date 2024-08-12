package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "blogs")
public interface BlogRepository extends JpaRepository<Blog,Integer> {
}
