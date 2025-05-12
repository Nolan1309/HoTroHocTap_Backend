package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.*;

import com.example.hotrohoctapbackend.convert.TestUserAnswerConverter;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.hotrohoctapbackend.util.VietnameseNormalization.normalizeVietnamese;

@Service
public class TestUserAnswerService {

    @Autowired
    private TestUserAnswerRepository testUserAnswerRepository;
    @Autowired
    private ProgressRepository progressRepository;
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

    @Autowired
    private SettingService settingService;


    public Map<String, Object> saveTestUserAnswer(TestUserAnswerRequestDTO_User requestDTO) {
        ScoreResponseDTO_User scoreResponseDTOUser = calculateScore(requestDTO);
        TestResultDTO_User testResultDTOUser = new TestResultDTO_User();
        Map<String, Object> result = new HashMap<>();
        if (scoreResponseDTOUser != null) {
            testResultDTOUser.setCorrect_answers(scoreResponseDTOUser.getCorrect());
            testResultDTOUser.setIncorrect_answers(scoreResponseDTOUser.getUncorrect());
            UserAnswerDTO_User item = requestDTO.getUserAnswers().get(0);
            testResultDTOUser.setTestID(item.getTestId());

            testResultDTOUser.setAccountID(requestDTO.getAccountId());
            testResultDTOUser.setCourseID(requestDTO.getCourseId());


            testResultDTOUser.setScore(scoreResponseDTOUser.getScore());
            String scoreType = settingService.getScore("score");
            Double scoreCheck = Double.parseDouble(scoreType);
            if (scoreResponseDTOUser.getScore() >= scoreCheck) {
                testResultDTOUser.setResult("Pass");
            } else testResultDTOUser.setResult("Fail");
            testResultDTOUser.setTotal_questions(scoreResponseDTOUser.getTotal());
            testResultDTOUser.setCompletedAt(LocalDateTime.now());
            testResultDTOUser.setChapterTest(requestDTO.isChapterTest());
            //Lưu ket qua bai test
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

                    //Luu dap an cua bai test
                    TestUserAnswerDTO_User testResultDTOUser1 = saveTestUserAnswer(testUserAnswerDTOUser);

                }
                if ("Pass".equals(testResult.getResult())) {

//                    boolean isLastChapter = progressService.isLastChapter(requestDTO.getCourseId(), requestDTO.getChapterId());


                    ProgressDTO_User progressDTOUser = new ProgressDTO_User();
                    progressDTOUser.setAccountId(requestDTO.getAccountId());
                    progressDTOUser.setCourseId(requestDTO.getCourseId());
                    progressDTOUser.setChapterId(requestDTO.getChapterId());
                    progressDTOUser.setLessonId(requestDTO.getLessonId());
                    progressDTOUser.setVideoStatus(requestDTO.isVideoStatus());
                    progressDTOUser.setTestStatus(requestDTO.isTestStatus());
                    progressDTOUser.setTestScore(null);
                    progressDTOUser.setChapterTest(requestDTO.isChapterTest());
                    progressService.UpdateScore(requestDTO, testResultDTOUser);

                    boolean nextProgressExists = progressService.isNextProgressExists(requestDTO.getAccountId(), requestDTO.getCourseId(), requestDTO.getChapterId(), requestDTO.getLessonId(), requestDTO.isChapterTest());


                    if (!nextProgressExists) {
                        Map<String, Object> progressResult = progressService.addOrUpdateProgress(progressDTOUser);
                        if ("course_completed".equals(progressResult.get("status"))) {
                            result.put("status", "course_completed");
                            result.put("scoreResponse", scoreResponseDTOUser);
                            return result;
                        } else {
                            result.put("status", "unlocked");
                            result.put("scoreResponse", scoreResponseDTOUser);
                            return result;
                        }
                    }
                    result.put("status", "already_completed");
                    result.put("scoreResponse", scoreResponseDTOUser);
                    return result;
                } else {
                    result.put("status", "locked");
                    result.put("scoreResponse", scoreResponseDTOUser);
                    return result;
                }
            }
        }


        result.put("status", "error");
        result.put("scoreResponse", null);
        return result;
    }

    public void saveProgress(UserAutoPassProgressDTO requestDTO) {
        ProgressDTO_User progressDTOUser = new ProgressDTO_User();
        progressDTOUser.setAccountId(requestDTO.getAccountId());
        progressDTOUser.setCourseId(requestDTO.getCourseId());
        progressDTOUser.setChapterId(requestDTO.getChapterId());
        progressDTOUser.setLessonId(requestDTO.getLessonId());
        progressDTOUser.setVideoStatus(requestDTO.isVideoStatus());
        progressDTOUser.setTestStatus(requestDTO.isTestStatus());
        progressDTOUser.setTestScore(null);
        progressDTOUser.setChapterTest(requestDTO.isChapterTest());
        progressService.UpdateScoreNoTest(requestDTO);

        boolean nextProgressExists = progressService.isNextProgressExists(requestDTO.getAccountId(), requestDTO.getCourseId(), requestDTO.getChapterId(), requestDTO.getLessonId(), requestDTO.isChapterTest());


        if (!nextProgressExists) {
            Map<String, Object> progressResult = progressService.addOrUpdateProgressNoTest(progressDTOUser);
        }
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

    //Chuyển đổi 0,1,2 sang A,B,C
    public static String convertIndexToChar(int index) {

        char[] chars = {'A', 'B', 'C', 'D'};
        if (index >= 0 && index < chars.length) {
            return String.valueOf(chars[index]);
        } else {
            return "A";
        }
    }


    public ScoreResponseDTO_User calculateScoreExam(QuestionUserExamPayload testUserAnswerDTO) {
        Integer totalQuestion = 0;
        Integer totalCorrect = 0;
        Integer totalInCorrect = 0;
        for (QuestionUserExamPayloadDTO item : testUserAnswerDTO.getUserAnswers()) {
            Question question = questionRepository.findById(Integer.parseInt(item.getQuestionId())).get();
            totalQuestion++;
            if (item.getType().equals("checkbox")) {
                List<QuestionUserExamPayloadDTOItem> itemAnswers = item.getAnswer();
                StringBuilder indexCheck = new StringBuilder();
                for (QuestionUserExamPayloadDTOItem dtoItem : itemAnswers) {
                    if (indexCheck.length() > 0) {
                        indexCheck.append(",");
                    }
                    indexCheck.append(dtoItem.getIndex() + 1);
                }
                String indexCheckStr = indexCheck.toString();
                boolean check = question.getResult_check().equals(indexCheckStr);
                if (check) {
                    totalCorrect++;
                } else {
                    totalInCorrect++;
                }
            } else if (item.getType().equals("multiple-choice")) {
                List<QuestionUserExamPayloadDTOItem> questionUserExamPayloadDTOItem = item.getAnswer();
                if (!questionUserExamPayloadDTOItem.isEmpty()) {
                    QuestionUserExamPayloadDTOItem firstItem = questionUserExamPayloadDTOItem.get(0);
                    String result = convertIndexToChar(firstItem.getIndex());
                    boolean check = question.getResult_check().equals(result);
                    if (check) {
                        totalCorrect++;
                    } else {
                        totalInCorrect++;
                    }
                }
            } else if (item.getType().equals("fill-in-the-blank")) {
                List<QuestionUserExamPayloadDTOItem> questionUserExamPayloadDTOItem = item.getAnswer();
                if (!questionUserExamPayloadDTOItem.isEmpty()) {
                    QuestionUserExamPayloadDTOItem firstItem = questionUserExamPayloadDTOItem.get(0);
                    String result = normalizeVietnamese(firstItem.getAnswer());
                    String result_check = normalizeVietnamese(question.getResult_check());
                    boolean check = result_check.equals(result);
                    if (check) {
                        totalCorrect++;
                    } else {
                        totalInCorrect++;
                    }
                }
            } else {
                //Dành cho Essay thì không cần kiểm tra
                totalCorrect++;
            }
        }

        double score = ((double) totalCorrect / totalQuestion) * 10;
        ScoreResponseDTO_User scoreResponseDTOUser = new ScoreResponseDTO_User();
        scoreResponseDTOUser.setCorrect(totalCorrect);
        scoreResponseDTOUser.setUncorrect(totalInCorrect);
        scoreResponseDTOUser.setScore(score);
        scoreResponseDTOUser.setTotal(totalQuestion);

        return scoreResponseDTOUser;
    }

    public TestResultDTO_User saveTestUserAnswerExam(QuestionUserExamPayload testUserAnswerDTO) {
        ScoreResponseDTO_User scoreResponseDTOUser = calculateScoreExam(testUserAnswerDTO);
        List<QuestionUserExamPayloadDTO> questionUserExamPayloadDTOList = testUserAnswerDTO.getUserAnswers();
        TestResultDTO_User testResultDTOUser = new TestResultDTO_User();

        testResultDTOUser.setTestID(testUserAnswerDTO.getTestId());
        testResultDTOUser.setChapterTest(false);
        testResultDTOUser.setCorrect_answers(scoreResponseDTOUser.getCorrect());
        testResultDTOUser.setIncorrect_answers(scoreResponseDTOUser.getUncorrect());
        testResultDTOUser.setScore(scoreResponseDTOUser.getScore());
        testResultDTOUser.setTotal_questions(scoreResponseDTOUser.getTotal());
        testResultDTOUser.setCompletedAt(LocalDateTime.now());
        testResultDTOUser.setAccountID(testUserAnswerDTO.getAccountId());
        testResultDTOUser.setCourseID(testUserAnswerDTO.getCourseId());
        String scoreType = settingService.getScore("score");
        Double scoreCheck = Double.parseDouble(scoreType);
        if (scoreResponseDTOUser.getScore() >= scoreCheck) {
            testResultDTOUser.setResult("Pass");
        } else testResultDTOUser.setResult("Fail");

        TestResultDTO_User testResult = testResultService.addTestResult(testResultDTOUser);

        for (QuestionUserExamPayloadDTO item : questionUserExamPayloadDTOList) {

            TestUserAnswerDTO_User testUserAnswerDTOUser = new TestUserAnswerDTO_User();
            testUserAnswerDTOUser.setTestResultId(testResult.getId());
            testUserAnswerDTOUser.setCourseId(testUserAnswerDTO.getCourseId());
            testUserAnswerDTOUser.setAccountId(testUserAnswerDTO.getAccountId());
            testUserAnswerDTOUser.setTestId(testUserAnswerDTO.getTestId());
            testUserAnswerDTOUser.setQuestionId(Integer.parseInt(item.getQuestionId()));
            if (item.getType().equals("checkbox")) {
                List<QuestionUserExamPayloadDTOItem> itemAnswers = item.getAnswer();
                StringBuilder indexCheck = new StringBuilder();
                for (QuestionUserExamPayloadDTOItem dtoItem : itemAnswers) {
                    if (indexCheck.length() > 0) {
                        indexCheck.append(",");
                    }
                    indexCheck.append(dtoItem.getIndex() + 1);
                }
                String indexCheckStr = indexCheck.toString();
                testUserAnswerDTOUser.setResult(indexCheckStr);
            } else if (item.getType().equals("multiple-choice")) {
                List<QuestionUserExamPayloadDTOItem> questionUserExamPayloadDTOItem = item.getAnswer();
                if (!questionUserExamPayloadDTOItem.isEmpty()) {
                    QuestionUserExamPayloadDTOItem firstItem = questionUserExamPayloadDTOItem.get(0);
                    String result = convertIndexToChar(firstItem.getIndex());
                    testUserAnswerDTOUser.setResult(result);
                }


            } else {
                List<QuestionUserExamPayloadDTOItem> questionUserExamPayloadDTOItem = item.getAnswer();
                if (!questionUserExamPayloadDTOItem.isEmpty()) {
                    QuestionUserExamPayloadDTOItem firstItem = questionUserExamPayloadDTOItem.get(0);
                    testUserAnswerDTOUser.setResult(firstItem.getAnswer());
                }
            }
            //Luu dap an cua bai test
            TestUserAnswerDTO_User testResultDTOUser1 = saveTestUserAnswer(testUserAnswerDTOUser);
        }
        return testResult;
    }

    public boolean isDuplicateAnswer(TestUserAnswerDTO_User testUserAnswerDTO) {
        return testUserAnswerRepository.existsByTestIdAndQuestionIdAndAccountId(
                testUserAnswerDTO.getTestId(),
                testUserAnswerDTO.getQuestionId(),
                testUserAnswerDTO.getAccountId()
        );
    }
}