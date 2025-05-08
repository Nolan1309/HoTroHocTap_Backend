package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class LearningPathSuggestionAPI {
    private String base_url;
    private String cluster;
    private String cluster_description;
    private String cluster_label;
    private List<String> learning_path_suggestion;
    private int prediction;
    private double probability;
    private String risk_level;
    private String student_id;

    public LearningPathSuggestionAPI() {
    }

    public LearningPathSuggestionAPI(String base_url, String cluster, String cluster_description, String cluster_label, List<String> learning_path_suggestion, int prediction, double probability, String risk_level, String student_id) {
        this.base_url = base_url;
        this.cluster = cluster;
        this.cluster_description = cluster_description;
        this.cluster_label = cluster_label;
        this.learning_path_suggestion = learning_path_suggestion;
        this.prediction = prediction;
        this.probability = probability;
        this.risk_level = risk_level;
        this.student_id = student_id;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getCluster_description() {
        return cluster_description;
    }

    public void setCluster_description(String cluster_description) {
        this.cluster_description = cluster_description;
    }

    public String getCluster_label() {
        return cluster_label;
    }

    public void setCluster_label(String cluster_label) {
        this.cluster_label = cluster_label;
    }

    public List<String> getLearning_path_suggestion() {
        return learning_path_suggestion;
    }

    public void setLearning_path_suggestion(List<String> learning_path_suggestion) {
        this.learning_path_suggestion = learning_path_suggestion;
    }

    public int getPrediction() {
        return prediction;
    }

    public void setPrediction(int prediction) {
        this.prediction = prediction;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(String risk_level) {
        this.risk_level = risk_level;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
