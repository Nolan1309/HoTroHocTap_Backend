package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.Test_QuestionRepository;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.entity.Test_Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestQuestionService {

    @Autowired
    private Test_QuestionRepository testQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    public String addQuestionsToTest(Integer testId, List<Integer> questionIds) {
        // Validate that the test exists
        Optional<Test> testOptional = testRepository.findById(testId);
        if (testOptional.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy bài kiểm tra với ID: " + testId);
        }

        Test test = testOptional.get();
        StringBuilder response = new StringBuilder("Kết quả thêm câu hỏi vào bài kiểm tra:\n");

        for (Integer questionId : questionIds) {
            // Validate that the question exists
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            if (questionOptional.isEmpty()) {
                response.append("Không tìm thấy câu hỏi với ID: ").append(questionId).append("\n");
                continue;
            }

            Question question = questionOptional.get();

            // Check if the question is already added to the test
            Optional<Test_Question> existingTestQuestion = testQuestionRepository.findByTestIdAndQuestionId(testId, questionId);
            if (existingTestQuestion.isPresent()) {
                response.append("Câu hỏi với ID ").append(questionId).append(" đã có trong bài kiểm tra với ID ").append(testId).append("\n");
                continue;
            }

            // Add the question to the test
            Test_Question testQuestion = new Test_Question();
            testQuestion.setTest(test); // Set the Test object
            testQuestion.setQuestion(question); // Set the Question object
            testQuestionRepository.save(testQuestion);

            response.append("Thành công thêm câu hỏi với ID ").append(questionId).append(" vào bài kiểm tra với ID ").append(testId).append("\n");
        }

        return response.toString();
    }
}
