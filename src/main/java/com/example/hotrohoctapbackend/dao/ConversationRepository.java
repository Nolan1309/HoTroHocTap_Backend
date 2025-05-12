package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "conversation")
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}
