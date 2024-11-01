package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.*;

import com.example.hotrohoctapbackend.convert.TestUserAnswerConverter;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestUserAnswerService {

    @Autowired
    private TestUserAnswerRepository testUserAnswerRepository;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TestResultRepository testResultRepository;

    public ScoreResponseDTO_User saveTestUserAnswer(TestUserAnswerRequestDTO_User requestDTO) {
        ScoreResponseDTO_User scoreResponseDTOUser = calculateScore(requestDTO);
        TestResultDTO_User testResultDTOUser = new TestResultDTO_User();
        if (scoreResponseDTOUser != null) {
            testResultDTOUser.setCorrect_answers(scoreResponseDTOUser.getCorrect());
            testResultDTOUser.setIncorrect_answers(scoreResponseDTOUser.getUncorrect());
            UserAnswerDTO_User item = requestDTO.getUserAnswers().get(0);
            testResultDTOUser.setTestID(item.getTestId());

            testResultDTOUser.setAccountID(requestDTO.getAccountId());
            testResultDTOUser.setCourseID(requestDTO.getCourseId());

            testResultDTOUser.setScore(scoreResponseDTOUser.getScore());
            if (scoreResponseDTOUser.getScore() >= 8.0) {
                testResultDTOUser.setResult("Pass");
            } else testResultDTOUser.setResult("Fail");
            testResultDTOUser.setTotal_questions(scoreResponseDTOUser.getTotal());
            testResultDTOUser.setCompletedAt(LocalDateTime.now());
            TestResultDTO_User testResult = testResultService.addTestResult(testResultDTOUser);

            if (testResult != null) {
                for (UserAnswerDTO_User userAnswerDTOUser : requestDTO.getUserAnswers()) {
                    TestUserAnswerDTO_User testUserAnswerDTOUser = new TestUserAnswerDTO_User();
                    testUserAnswerDTOUser.setTestResultId(testResult.getId());
                    testUserAnswerDTOUser.setCourseId(requestDTO.getCourseId());
                    testUserAnswerDTOUser.setAccountId(requestDTO.getAccountId());
                    testUserAnswerDTOUser.setTestId(userAnswerDTOUser.getTestId());
                    testUserAnswerDTOUser.setQuestionId(userAnswerDTOUser.getQuestionId());
                    testUserAnswerDTOUser.setResult(userAnswerDTOUser.getResult());

                    TestUserAnswerDTO_User testResultDTOUser1 = saveTestUserAnswer(testUserAnswerDTOUser);

                }
                if (testResult.getResult() == "Pass") {
                    ProgressDTO_User progressDTOUser = new ProgressDTO_User();
                    progressDTOUser.setAccountId(requestDTO.getAccountId());
                    progressDTOUser.setCourseId(requestDTO.getCourseId());
                    progressDTOUser.setChapterId(requestDTO.getChapterId());
                    progressDTOUser.setLessonId(requestDTO.getLessonId());
                    progressDTOUser.setVideoStatus(requestDTO.isVideoStatus());
                    progressDTOUser.setTestStatus(requestDTO.isTestStatus());
                    progressDTOUser.setTestScore(null);
                    progressDTOUser.setChapterTest(requestDTO.isChapterTest());
                    Progress progress = progressService.addOrUpdateProgress(progressDTOUser);
                    return scoreResponseDTOUser;
                }
            }
        }

        return scoreResponseDTOUser;
    }


    public ScoreResponseDTO_User calculateScore(TestUserAnswerRequestDTO_User requestDTO) {
        int correctCount = 0;
        int uncorrectCount = 0;
        int totalQuestions = requestDTO.getTotalQuestion();

        for (UserAnswerDTO_User userAnswer : requestDTO.getUserAnswers()) {
            Question question = questionRepository.findById(userAnswer.getQuestionId()).orElse(null);

            if (question != null) {
                if (question.getResult_check().equals(userAnswer.getResult())) {
                    correctCount++;
                } else {
                    uncorrectCount++;
                }
            }
        }

        double score = ((double) correctCount / totalQuestions) * 10;
        ScoreResponseDTO_User scoreResponseDTOUser = new ScoreResponseDTO_User();
        scoreResponseDTOUser.setCorrect(correctCount);
        scoreResponseDTOUser.setUncorrect(uncorrectCount);
        scoreResponseDTOUser.setScore(score);
        scoreResponseDTOUser.setTotal(totalQuestions);

        return scoreResponseDTOUser;
    }


    public TestUserAnswerDTO_User saveTestUserAnswer(TestUserAnswerDTO_User testUserAnswerDTO) {
        if (isDuplicateAnswer(testUserAnswerDTO)) {
            throw new IllegalArgumentException("Câu trả lời cho câu hỏi này đã tồn tại.");
        }
        if (testUserAnswerDTO.getTestResultId() == null) {
            throw new IllegalArgumentException("TestResultId không được để trống");
        }
        TestUserAnswer testUserAnswer = new TestUserAnswer();

        // Kiểm tra và set test, question, account, và course từ repository
        Test test = testRepository.findById(testUserAnswerDTO.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));
        testUserAnswer.setTest(test);

        Question question = questionRepository.findById(testUserAnswerDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
        testUserAnswer.setQuestion(question);

        Account account = accountRepository.findById(testUserAnswerDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        testUserAnswer.setAccount(account);

        Course course = courseRepository.findById(testUserAnswerDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        testUserAnswer.setCourse(course);
        if (testUserAnswerDTO.getTestResultId() != null) {
            TestResult testResult = testResultRepository.findById(testUserAnswerDTO.getTestResultId())
                    .orElseThrow(() -> new RuntimeException("Test result not found"));
            testUserAnswer.setTestResult(testResult);
        }
        testUserAnswer.setResult(testUserAnswerDTO.getResult());
        TestUserAnswer savedAnswer = testUserAnswerRepository.save(testUserAnswer);
        return TestUserAnswerConverter.toDTO(savedAnswer);
    }

    public boolean isDuplicateAnswer(TestUserAnswerDTO_User testUserAnswerDTO) {
        return testUserAnswerRepository.existsByTestIdAndQuestionIdAndAccountId(
                testUserAnswerDTO.getTestId(),
                testUserAnswerDTO.getQuestionId(),
                testUserAnswerDTO.getAccountId()
        );
    }
}