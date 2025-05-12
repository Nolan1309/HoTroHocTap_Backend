package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.DTO.LearningPathSuggestionAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class PythonScriptService {


    @Autowired
    private RestTemplate restTemplate;


    public List<LearningPathSuggestionAPI> sendStudentData(Object studentData) {
//        String flaskApiUrl = "http://103.166.143.198:5000/predict"; // URL của API Flask
        String flaskApiUrl = "http://localhost:5000/predict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(studentData, headers);

        // Gửi POST request tới Flask và nhận phản hồi dưới dạng danh sách LearningPathSuggestion
        ResponseEntity<List<LearningPathSuggestionAPI>> response = restTemplate.exchange(
                flaskApiUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<List<LearningPathSuggestionAPI>>() {
                });

        return response.getBody();
    }
}
