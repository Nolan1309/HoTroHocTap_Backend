package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV2.AdminQuestionGetDTO_V2;
import com.example.hotrohoctapbackend.dao.Test_QuestionRepository;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.entity.Test_Question;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestQuestionService {

    @Autowired
    private Test_QuestionRepository testQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;


    @Transactional
    public String addQuestionsToTest(Integer testId, List<Integer> questionIds) {
        // Validate that the test exists
        List<Test_Question> list = testQuestionRepository.findTestAnswersByTestId(testId);
        if (list.size() == 0) {
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
        } else {
            // Nếu bài kiểm tra đã có câu hỏi, tiến hành xóa tất cả các câu hỏi cũ trước khi thêm câu hỏi mới
            testQuestionRepository.deleteByTestId(testId);

            // Sau khi xóa, tiếp tục thêm các câu hỏi mới vào
            Optional<Test> testOptional = testRepository.findById(testId);
            if (testOptional.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy bài kiểm tra với ID: " + testId);
            }

            Test test = testOptional.get();
            StringBuilder response = new StringBuilder("Kết quả thêm câu hỏi vào bài kiểm tra:\n");

            for (Integer questionId : questionIds) {
                // Kiểm tra xem câu hỏi có tồn tại hay không
                Optional<Question> questionOptional = questionRepository.findById(questionId);
                if (questionOptional.isEmpty()) {
                    response.append("Không tìm thấy câu hỏi với ID: ").append(questionId).append("\n");
                    continue;
                }

                Question question = questionOptional.get();

                // Kiểm tra xem câu hỏi đã được thêm vào bài kiểm tra hay chưa
                Optional<Test_Question> existingTestQuestion = testQuestionRepository.findByTestIdAndQuestionId(testId, questionId);
                if (existingTestQuestion.isPresent()) {
                    response.append("Câu hỏi với ID ").append(questionId).append(" đã có trong bài kiểm tra với ID ").append(testId).append("\n");
                    continue;
                }

                // Thêm câu hỏi vào bài kiểm tra
                Test_Question testQuestion = new Test_Question();
                testQuestion.setTest(test); // Gán bài kiểm tra
                testQuestion.setQuestion(question); // Gán câu hỏi
                testQuestionRepository.save(testQuestion);

                response.append("Thành công thêm câu hỏi với ID ").append(questionId).append(" vào bài kiểm tra với ID ").append(testId).append("\n");
            }

            return response.toString();
        }
    }


    @Transactional
    public String addQuestionsToTest_V2(Integer testId, List<AdminQuestionGetDTO_V2> questionDTOs) {
        // Kiểm tra bài kiểm tra tồn tại
        Optional<Test> testOptional = testRepository.findById(testId);
        if (testOptional.isEmpty()) {
            return "Bài kiểm tra không tồn tại.";
        }
        Test test = testOptional.get();

        // Khởi tạo StringBuilder để lưu thông tin phản hồi
        StringBuilder response = new StringBuilder("Kết quả thêm câu hỏi vào bài kiểm tra:\n");

        for (AdminQuestionGetDTO_V2 item : questionDTOs) {
            // Kiểm tra xem câu hỏi có tồn tại không
            Optional<Question> questionOptional = questionRepository.findById(item.getId());
            if (questionOptional.isEmpty()) {
                response.append("Không tìm thấy câu hỏi với ID: ").append(item.getId()).append("\n");
                continue; // Bỏ qua câu hỏi này
            }

            Question question = questionOptional.get();

            // Kiểm tra xem câu hỏi đã có trong bài kiểm tra chưa
            Optional<Test_Question> existingTestQuestion = testQuestionRepository.findByTestIdAndQuestionId(testId, item.getId());
            if (existingTestQuestion.isPresent()) {
                response.append("Câu hỏi với ID ").append(item.getId()).append(" đã tồn tại trong bài kiểm tra.\n");
                continue; // Bỏ qua câu hỏi này
            }

            // Tạo mới Test_Question
            Test_Question testQuestion = new Test_Question();
            testQuestion.setTest(test); // Gán bài kiểm tra
            testQuestion.setQuestion(question); // Gán câu hỏi
            // Lưu vào cơ sở dữ liệu
            testQuestionRepository.save(testQuestion);
            response.append("Thêm thành công câu hỏi với ID ").append(item.getId()).append("\n");
        }

        return response.toString();
    }

    @Transactional
    public String updateTestQuestions(Integer testId, List<AdminQuestionGetDTO_V2> questionDTOs) {
        // Kiểm tra bài kiểm tra tồn tại
        Optional<Test> testOptional = testRepository.findById(testId);
        if (testOptional.isEmpty()) {
            return "Bài kiểm tra không tồn tại.";
        }
        Test test = testOptional.get();

        // Khởi tạo StringBuilder để lưu thông tin phản hồi
        StringBuilder response = new StringBuilder("Kết quả thêm và chỉnh sửa câu hỏi vào bài kiểm tra:\n");

        // Lấy danh sách câu hỏi hiện tại trong bài kiểm tra từ DB
//        List<Test_Question> currentTestQuestions = test.getTestQuestions();

        // Trường hợp nếu bài kiểm tra chưa có câu hỏi nào
        if (test.getTestQuestions().isEmpty()) {
            // Nếu chưa có câu hỏi nào, chúng ta chỉ cần thêm tất cả câu hỏi từ FE
            for (AdminQuestionGetDTO_V2 item : questionDTOs) {
                Optional<Question> questionOptional = questionRepository.findById(item.getId());
                if (questionOptional.isEmpty()) {
                    response.append("Không tìm thấy câu hỏi với ID: ").append(item.getId()).append("\n");
                    continue; // Bỏ qua câu hỏi này nếu không tìm thấy trong DB
                }

                Question question = questionOptional.get();

                // Kiểm tra xem câu hỏi đã có trong bài kiểm tra chưa
                Optional<Test_Question> existingTestQuestion = testQuestionRepository.findByTestIdAndQuestionId(testId, item.getId());
                if (existingTestQuestion.isPresent()) {
                    // Nếu câu hỏi đã tồn tại, không làm gì
                    response.append("Câu hỏi với ID ").append(item.getId()).append(" đã có trong bài kiểm tra.\n");
                    continue;
                }

                // Tạo mới Test_Question và thêm vào bài kiểm tra
                Test_Question testQuestion = new Test_Question();
                testQuestion.setTest(test); // Gán bài kiểm tra
                testQuestion.setQuestion(question); // Gán câu hỏi
                // Lưu vào cơ sở dữ liệu
                testQuestionRepository.save(testQuestion);
                response.append("Thêm thành công câu hỏi với ID ").append(item.getId()).append("\n");
            }
            return response.toString(); // Trả về kết quả sau khi thêm các câu hỏi
        }

        // Nếu bài kiểm tra đã có câu hỏi, xử lý việc thêm, giữ nguyên hoặc xóa câu hỏi
        // Duyệt qua danh sách câu hỏi từ FE
        for (AdminQuestionGetDTO_V2 item : questionDTOs) {
            Optional<Question> questionOptional = questionRepository.findById(item.getId());
            if (questionOptional.isEmpty()) {
                response.append("Không tìm thấy câu hỏi với ID: ").append(item.getId()).append("\n");
                continue; // Bỏ qua câu hỏi này nếu không tìm thấy trong DB
            }

            Question question = questionOptional.get();

            // Kiểm tra xem câu hỏi đã có trong bài kiểm tra chưa
            Optional<Test_Question> existingTestQuestion = testQuestionRepository.findByTestIdAndQuestionId(testId, item.getId());
            if (existingTestQuestion.isPresent()) {
                // Nếu câu hỏi đã tồn tại, không làm gì
                response.append("Câu hỏi với ID ").append(item.getId()).append(" đã có trong bài kiểm tra.\n");
                continue;
            }

            // Tạo mới Test_Question và thêm vào bài kiểm tra
            Test_Question testQuestion = new Test_Question();
            testQuestion.setTest(test); // Gán bài kiểm tra
            testQuestion.setQuestion(question); // Gán câu hỏi
            // Lưu vào cơ sở dữ liệu
            testQuestionRepository.save(testQuestion);
            response.append("Thêm thành công câu hỏi với ID ").append(item.getId()).append("\n");
        }

        deleteQuestionsNotInFE(test, questionDTOs, response);

        return response.toString();
    }

    @Transactional
    public void deleteQuestionsNotInFE(Test test, List<AdminQuestionGetDTO_V2> questionDTOs, StringBuilder response) {
        // Lấy danh sách câu hỏi hiện tại trong bài kiểm tra từ DB
        List<Test_Question> currentTestQuestions = test.getTestQuestions();

        // Duyệt qua các câu hỏi hiện tại và kiểm tra xem câu hỏi có trong danh sách FE không
        for (Test_Question currentTestQuestion : currentTestQuestions) {
            boolean found = false;
            // Duyệt qua câu hỏi từ frontend (FE) và kiểm tra
            for (AdminQuestionGetDTO_V2 item : questionDTOs) {
                if (currentTestQuestion.getQuestion().getId() == item.getId()) {
                    found = true;
                    break;
                }
            }

            // Nếu không tìm thấy câu hỏi trong danh sách FE, xóa câu hỏi khỏi bài kiểm tra
            if (!found) {

                // Xóa câu hỏi khỏi bài kiểm tra
                Integer check = testQuestionRepository.deleteQuestionsByTestIdAndQuestionId(currentTestQuestion.getTest().getId(), currentTestQuestion.getQuestion().getId());
                response.append("Xóa câu hỏi với ID ").append(currentTestQuestion.getQuestion().getId()).append("\n");
            }
        }
    }


    public List<AdminQuestionGetDTO_V2> getQuestionsByTestId(Integer testId) {
        List<Test_Question> testQuestions = testQuestionRepository.findTestAnswersByTestId(testId);


        // Chuyển đổi từ Test_Question sang AdminQuestionGetDTO_V2 , có mapping tự dong
        return testQuestions.stream()
                .map(testQuestion -> {
                    var question = testQuestion.getQuestion();
                    LocalDateTime createdAt = question.getCreatedAt().toInstant()
                            .atZone(ZoneId.systemDefault()) // Sử dụng múi giờ mặc định
                            .toLocalDateTime();
                    return new AdminQuestionGetDTO_V2(
                            question.getId(),
                            question.getContent(),
                            question.getOptionA(),
                            question.getOptionB(),
                            question.getOptionC(),
                            question.getOptionD(),
                            question.getResult(),
                            question.getInstruction(),
                            question.getResult_check(),
                            question.getLevel(),
                            question.getType(),
                            question.getAccount().getId(),
                            question.getCourse().getId(),
                            question.getTopic(),
                            createdAt

                    );
                })
                .collect(Collectors.toList());
    }
}
