package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation; // Mối quan hệ với cuộc trò chuyện

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender; // Người gửi tin nhắn

    @Column(name = "content")
    private String content; // Nội dung tin nhắn

    @Column(name = "messageType")
    private String messageType; // Loại tin nhắn: text, image, video, file

    @Column(name = "status")
    private String status; // Trạng thái tin nhắn: sent, delivered, read

    @Column(name = "timestamp")
    private String timestamp;  // Thời gian gửi tin nhắn
}
