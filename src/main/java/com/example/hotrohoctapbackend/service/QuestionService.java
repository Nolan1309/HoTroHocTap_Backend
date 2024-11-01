package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.QuestionDTO_User;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionDTO_User> getQuestionsByTestId(int testId) {
        // Lấy danh sách câu hỏi theo testId và chuyển đổi sang QuestionDTO_User
        List<Object[]> results = questionRepository.findQuestionsByTestId(testId);
        return results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private QuestionDTO_User convertToDTO(Object[] result) {
        QuestionDTO_User dto = new QuestionDTO_User();
        dto.setQuestionId((Integer) result[0]);
        dto.setContent((String) result[1]);
        dto.setOptionA((String) result[2]);
        dto.setOptionB((String) result[3]);
        dto.setOptionC((String) result[4]);
        dto.setOptionD((String) result[5]);
        dto.setCreatedAt((Date) result[6]);
        dto.setUpdatedAt((Date) result[7]);
        return dto;
    }
}