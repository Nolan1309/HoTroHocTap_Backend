package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
