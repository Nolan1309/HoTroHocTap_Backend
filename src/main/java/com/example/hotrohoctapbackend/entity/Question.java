package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

//    @ManyToOne
//    @JoinColumn(name = "test_id", nullable = false)
//    private Test test;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "option_a", columnDefinition = "TEXT", nullable = false)
    private String optionA;

    @Column(name = "option_b", columnDefinition = "TEXT", nullable = false)
    private String optionB;

    @Column(name = "option_c", columnDefinition = "TEXT", nullable = false)
    private String optionC;

    @Column(name = "option_d", columnDefinition = "TEXT", nullable = false)
    private String optionD;

    @Column(name = "result", columnDefinition = "TEXT", nullable = false)
    private String result;

    @Column(name = "instruction", columnDefinition = "TEXT")
    private String instruction;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "question")
    private List<Test_Question> testQuestions;

//    @OneToMany(mappedBy = "question",
//            fetch = FetchType.LAZY
//            , cascade = {
//            CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH
//    })
//    private List<TestUserAnswer> testUserAnswerList;
}