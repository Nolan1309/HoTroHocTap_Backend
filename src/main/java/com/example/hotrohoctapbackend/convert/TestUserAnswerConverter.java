package com.example.hotrohoctapbackend.convert;

import com.example.hotrohoctapbackend.DTO.User.TestUserAnswerDTO_User;
import com.example.hotrohoctapbackend.entity.TestUserAnswer;

public class TestUserAnswerConverter {
    public static TestUserAnswerDTO_User toDTO(TestUserAnswer entity) {
        return new TestUserAnswerDTO_User(
                entity.getId(),
                entity.getTest().getId(),
                entity.getQuestion().getId(),
                entity.getAccount().getId(),
                entity.getCourse().getId(),
                entity.getResult(),
                entity.getTestResult() != null ? entity.getTestResult().getId() : null // Thêm testResultId
        );
    }
}
