package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "type")
    private String type; // private, group

    @Column(name = "name")
    private String name; // Tên cuộc trò chuyện (nếu là nhóm)

//    @OneToMany(mappedBy = "conversation")
//    private List<Message> messages; // Các tin nhắn trong cuộc trò chuyện
//
}
