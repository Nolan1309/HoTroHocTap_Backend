package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "conversation_account")
public class ConversationAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")// Tự động tăng
    private int id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation; // Mối quan hệ với cuộc trò chuyện

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Mối quan hệ với người dùng (account)
}
