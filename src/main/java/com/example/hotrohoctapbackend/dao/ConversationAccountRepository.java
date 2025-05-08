package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.ConversationAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "conversation_account")
public interface ConversationAccountRepository extends JpaRepository<ConversationAccount, Integer> {
}
