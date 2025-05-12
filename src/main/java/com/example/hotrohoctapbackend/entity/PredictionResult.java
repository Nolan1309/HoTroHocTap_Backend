package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "prediction_results")
public class PredictionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tạo giá trị cho trường id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;  // Liên kết với Account

    @Column(name = "student_id", nullable = false)
    private String studentId;  // Mã sinh viên

    @Column(name = "cluster", nullable = false)
    private String cluster;  // Cụm (0)

    @Column(name = "cluster_description", columnDefinition = "TEXT")
    private String clusterDescription;  // Mô tả cụm

    @Column(name = "cluster_label", nullable = false)
    private String clusterLabel;  // Nhãn cụm

    @ElementCollection
    @CollectionTable(name = "learning_path_suggestions", joinColumns = @JoinColumn(name = "prediction_result_id"))
    @Column(name = "suggestion")
    private List<String> learningPathSuggestion;  // Gợi ý lộ trình học tập

    @Column(name = "prediction", nullable = false)
    private Integer prediction;  // Dự đoán (1)

    @Column(name = "probability", nullable = false)
    private Double probability;  // Xác suất

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;  // Mức độ rủi ro (High)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // Thời gian tạo (thời điểm dự đoán)


}
