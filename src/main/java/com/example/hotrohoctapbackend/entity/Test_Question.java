package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "test_answers")
@Data
public class Test_Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;



    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}