package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminQuestionGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.User.QuestionDTO_User;
import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.DTO.User.UserQuestionExamDTO;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.google.type.Decimal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.Lvl;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;
import org.docx4j.wml.Text;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.data.domain.Page;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.Numbering.AbstractNum;
import org.docx4j.wml.Numbering.Num;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private TestRepository testRepository;

    @Autowired
    private Test_QuestionRepository testQuestionRepository;

    private static final Map<String, String> typeMapping = new HashMap<>();

    static {
        typeMapping.put("Tự luận", "essay");
        typeMapping.put("Trắc nghiệm", "multiple-choice");
        typeMapping.put("Điền khuyết", "fill-in-the-blank");
        typeMapping.put("Checkbox", "checkbox");
    }

    //    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
//        if (timestampObj instanceof Timestamp) {
//            Timestamp timestamp = (Timestamp) timestampObj;
//            return timestamp.toLocalDateTime();
//        }
//        return null;
//    }
    private UserQuestionExamDTO convertToDTOUserQuestion(Object[] row) {
        int id = (Integer) row[0]; // The first element is the ID
        String content = (String) row[1]; // The second element is content
        LocalDateTime createdAt = convertTimestampToLocalDateTime(row[2]); // The third element is createdAt
        LocalDateTime deletedDate = convertTimestampToLocalDateTime(row[3]); // The fourth element is deletedDate
        String instruction = (String) row[4]; // The fifth element is instruction
        boolean isDeleted = (Boolean) row[5]; // The sixth element is isDeleted
        String level = (String) row[6]; // The seventh element is level
        String optionA = (String) row[7]; // The eighth element is optionA
        String optionB = (String) row[8]; // The ninth element is optionB
        String optionC = (String) row[9]; // The tenth element is optionC
        String optionD = (String) row[10]; // The eleventh element is optionD
        String result = (String) row[11]; // The twelfth element is result
        String resultCheck = (String) row[12]; // The thirteenth element is resultCheck
        String topic = (String) row[13]; // The fourteenth element is topic
        String type = (String) row[14]; // The fifteenth element is type
        LocalDateTime updatedAt = convertTimestampToLocalDateTime(row[15]); // The sixteenth element is updatedAt
        int accountId = (Integer) row[16]; // The seventeenth element is accountId
        int courseId = (Integer) row[17]; // The eighteenth element is courseId

        // Create and return the UserQuestionExamDTO object
        return new UserQuestionExamDTO(id, content, createdAt, deletedDate, instruction, isDeleted,
                level, optionA, optionB, optionC, optionD, result, resultCheck,
                topic, type, updatedAt, accountId, courseId);
    }

    public List<QuestionDTO_User> getQuestionsByTestId(int testId) {
        // Lấy danh sách câu hỏi theo testId và chuyển đổi sang QuestionDTO_User
        List<Object[]> results = questionRepository.findQuestionsByTestId(testId);
        return results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserQuestionExamDTO> getQuestionsByTestId_Exam(int testId) {
        List<Object[]> results = questionRepository.findQuestionsByTestId_Exam(testId);
        return results.stream().map(this::convertToDTOUserQuestion).collect(Collectors.toList());
    }

    //Get result check for test after submit from user
    public List<QuestionResponseDTO_User> getQuestionsByTestId(Integer testId) {
        List<Object[]> list = questionRepository.findQuestionsResponsiveByTestId(testId);
        List<QuestionResponseDTO_User> list_answer = new ArrayList<>();
        for (Object[] item : list) {
            QuestionResponseDTO_User questionResponse = new QuestionResponseDTO_User();
            questionResponse.setId((Integer) item[0]);
            questionResponse.setInstruction((String) item[1]);
            questionResponse.setCorrect_show((String) item[2]);
            questionResponse.setCorrect_check((String) item[3]);
            list_answer.add(questionResponse);
        }
        return list_answer;
    }

    //    SELECT q.id AS questionId, q.instruction AS instruction, q.result AS correctShow, q.result_check AS correctCheck
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

    //    Hàm chuẩn hóa tiếng việt
    public static String normalizeVietnamese(String input) {
        // Loại bỏ dấu trong tiếng Việt
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase();
    }

    private int questionCounter = 0;

    public List<Question> parseDocxFile(MultipartFile file, String type) {
        List<Question> questions = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            List<Object> paragraphs = wordMLPackage.getMainDocumentPart().getContent();
            Question currentQuestion = null;
            String currentLevel = "2";  // Mặc định là Trung bình

            for (Object obj : paragraphs) {
                if (obj instanceof org.docx4j.wml.P) {
                    String text = extractTextWithBullet(obj).trim();
                    org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;
//                    String numbering = getNumberingSymbolWithCounter(paragraph, wordMLPackage);
//                    String text = numbering.equals("No numbering") ? texting : numbering + " " + texting;

                    if (text.isEmpty()) continue;

                    // **Nhận diện mức độ câu hỏi**
                    if (text.matches("Mức độ: .*")) {
                        currentLevel = text.replace("Mức độ:", "").trim();
                        continue;
                    }

                    // **Loại bỏ số thứ tự "Câu x:"**
                    text = text.replaceAll("^(Câu \\d+[.•\\s]*\\s*)", "").trim();

                    // **Nhận diện hướng dẫn**
                    if (text.matches("Hướng dẫn: .*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setInstruction(text.replace("Hướng dẫn:", "").trim());
                        }
                        continue;
                    }

                    if (text.matches("Chủ đề: .*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setTopic(text.replace("Chủ đề:", "").trim());
                        }
                        continue;
                    }

                    // **Nhận diện các lựa chọn đáp án**
                    else if (text.matches("^[A-D]\\.\\s?.*")) {
                        if (currentQuestion != null) {
                            String answerOption = text.substring(0, 1);
                            String answerText = text.substring(3).trim();

                            switch (answerOption) {
                                case "A":
                                    currentQuestion.setOptionA(answerText);
                                    break;
                                case "B":
                                    currentQuestion.setOptionB(answerText);
                                    break;
                                case "C":
                                    currentQuestion.setOptionC(answerText);
                                    break;
                                case "D":
                                    currentQuestion.setOptionD(answerText);
                                    break;
                            }

                            if (isBoldOrRedText(obj)) {
                                currentQuestion.setResult(answerText);
                                currentQuestion.setResult_check(answerOption);
                            }
                        }
                    } else {
                        // **Nếu là câu hỏi mới**
                        if (currentQuestion != null) {
                            questions.add(currentQuestion);
                        }

                        currentQuestion = new Question();
                        currentQuestion.setContent(text);

                        // **Gán mức độ vào câu hỏi**
                        if (currentLevel.equalsIgnoreCase("Dễ")) {
                            currentQuestion.setLevel("1");
                        } else if (currentLevel.equalsIgnoreCase("Trung bình")) {
                            currentQuestion.setLevel("2");
                        } else {
                            currentQuestion.setLevel("3");
                        }

                        currentQuestion.setType(type);
                    }
                }
            }

            // **Thêm câu hỏi cuối cùng vào danh sách**
            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Question> parseDocxFileFill(MultipartFile file, String type, String courseId, String accountId) {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            List<Object> paragraphs = wordMLPackage.getMainDocumentPart().getContent();

            String currentLevel = "2";  // Mức độ mặc định
            Question currentQuestion = null;

            for (int i = 0; i < paragraphs.size(); i++) {
                Object obj = paragraphs.get(i);
                if (obj instanceof org.docx4j.wml.P) {
                    String text = extractTextWithBulletFill(obj).trim();
//                    text = normalizeText(text);
                    // Nhận diện mức độ
                    if (text.matches("Mức độ: .*")) {
                        currentLevel = text.replace("Mức độ:", "").trim();
                    }

                    // Nhận diện câu hỏi
                    else if (text.matches("Câu \\d+\\..*")) {
                        if (currentQuestion != null) {
                            questions.add(currentQuestion);  // Lưu câu hỏi trước đó
                        }

                        currentQuestion = new Question();
                        String cleanedContent = text.replaceFirst("Câu \\d+: ", "").trim();
                        currentQuestion.setContent(cleanedContent);
                        currentQuestion.setType(type);
                        if (currentLevel.equals("Dễ")) {
                            currentQuestion.setLevel("1");
                        } else if (currentLevel.equals("Trung bình")) {
                            currentQuestion.setLevel("2");
                        } else {
                            currentQuestion.setLevel("3");
                        }

                    }

                    // Nhận diện từ khuyết
                    else if (text.matches("Từ khuyết:\\s?.*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setResult(text.replace("Từ khuyết:", "").trim());
                            currentQuestion.setResult_check(text.replace("Từ khuyết:", "").trim());
                        }
                    }

                    // Nhận diện hướng dẫn
                    else if (text.matches("Hướng dẫn: .*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setInstruction(text.replace("Hướng dẫn:", "").trim());
                        }
                    } else if (text.matches("Chủ đề: .*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setTopic(text.replace("Chủ đề:", "").trim());
                        }
                    }

                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Question> parseDocxFileEssay(MultipartFile file, String dialogType) {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            List<Object> paragraphs = wordMLPackage.getMainDocumentPart().getContent();

            String currentLevel = "2";  // Mức độ mặc định
            Question currentQuestion = null;

            for (Object obj : paragraphs) {
                if (obj instanceof org.docx4j.wml.P) {
                    String text = extractTextWithBulletFill(obj).trim();

                    // Nhận diện mức độ
                    if (text.matches("Mức độ: .*")) {
                        currentLevel = text.replace("Mức độ:", "").trim();
                    }

                    // Nhận diện câu hỏi mới
                    else if (text.matches("Câu \\d+\\..*")) {
                        if (currentQuestion != null) {
                            questions.add(currentQuestion);
                        }

                        currentQuestion = new Question();
                        String cleanedContent = text.replaceFirst("Câu \\d+. ", "").trim();
                        currentQuestion.setContent(cleanedContent);
                        if (currentLevel.equals("Dễ")) {
                            currentQuestion.setLevel("1");
                        } else if (currentLevel.equals("Trung bình")) {
                            currentQuestion.setLevel("2");
                        } else {
                            currentQuestion.setLevel("3");
                        }
                        currentQuestion.setType(dialogType);  // Gán loại câu hỏi từ dialogType
                    }

                    // Nhận diện hướng dẫn (áp dụng cho câu hỏi tự luận)
                    else if (text.matches("Hướng dẫn: .*")) {
                        if (currentQuestion != null && "essay".equals(dialogType)) {
                            currentQuestion.setInstruction(text.replace("Hướng dẫn:", "").trim());
                        }
                    } else if (text.matches("Chủ đề: .*")) {
                        if (currentQuestion != null && "essay".equals(dialogType)) {
                            currentQuestion.setTopic(text.replace("Chủ đề:", "").trim());
                        }
                    }

                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    private void saveOptionsToQuestion(Question question, Map<Integer, String> options, List<Integer> correctIndexes) {
        // Gán các lựa chọn vào các trường option_a, option_b, option_c, option_d
        question.setOptionA(options.getOrDefault(1, ""));
        question.setOptionB(options.getOrDefault(2, ""));
        question.setOptionC(options.getOrDefault(3, ""));
        question.setOptionD(options.getOrDefault(4, ""));

        // Lưu đáp án đúng vào trường result (dạng text)
        String result = options.entrySet().stream()
                .filter(entry -> correctIndexes.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(", "));
        question.setResult(result);

        // Lưu các chỉ số đáp án đúng vào result_check
        String resultCheck = correctIndexes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        question.setResult_check(resultCheck);
    }

    public List<Question> parseDocxFileCheckbox(MultipartFile file, String dialogType) {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            List<Object> paragraphs = wordMLPackage.getMainDocumentPart().getContent();

            String currentLevel = "2";
            Question currentQuestion = null;
            Map<Integer, String> options = new LinkedHashMap<>();
            List<Integer> correctIndexes = new ArrayList<>();

            for (Object obj : paragraphs) {
                if (obj instanceof org.docx4j.wml.P) {
                    String text = extractTextWithBulletFill(obj).trim();

                    // Nhận diện mức độ
                    if (text.matches("Mức độ: .*")) {
                        currentLevel = text.replace("Mức độ:", "").trim();
                    }
                    // Nhận diện câu hỏi mới
                    else if (text.matches("Câu \\d+[\\.:]\\s?.*")) {
                        if (currentQuestion != null) {
                            // Gán các lựa chọn và đáp án vào câu hỏi hiện tại
                            saveOptionsToQuestion(currentQuestion, options, correctIndexes);
                            questions.add(currentQuestion);
                        }

                        // Tạo câu hỏi mới
                        currentQuestion = new Question();
                        String cleanedContent = text.replaceFirst("Câu \\d+[\\.:]\\s?", "").trim();
                        currentQuestion.setContent(cleanedContent);

                        if (currentLevel.equals("Dễ")) {
                            currentQuestion.setLevel("1");
                        } else if (currentLevel.equals("Trung bình")) {
                            currentQuestion.setLevel("2");
                        } else {
                            currentQuestion.setLevel("3");
                        }
                        currentQuestion.setType(dialogType);
                        options.clear();
                        correctIndexes.clear();
                    }

                    // Nhận diện lựa chọn và kiểm tra đáp án đúng
                    else if (text.matches("Lựa chọn \\d+[\\.\\:]\\s?.*")) {  // Cập nhật regex để chấp nhận cả dấu chấm và dấu hai chấm
                        if (currentQuestion != null) {
                            // Trích xuất chỉ số lựa chọn từ "Lựa chọn 1." hoặc "Lựa chọn 1:"
                            int choiceIndex = Integer.parseInt(text.replaceAll("Lựa chọn (\\d+)[\\.\\:]?.*", "$1"));

                            // Trích xuất nội dung lựa chọn
                            String choiceText = text.replaceFirst("Lựa chọn \\d+[\\.\\:]\\s?", "").trim();

                            boolean isCorrect = false;
                            // Kiểm tra xem văn bản có phải là lựa chọn đúng không
                            if (isBoldOrRedText(obj)) {
                                isCorrect = true;
                            }

                            // Lưu lựa chọn vào map
                            options.put(choiceIndex, choiceText);

                            // Nếu là lựa chọn đúng, thêm vào danh sách đáp án đúng
                            if (isCorrect) {
                                correctIndexes.add(choiceIndex);
                            }
                        }
                    }

                    // Nhận diện hướng dẫn (áp dụng cho câu hỏi tự luận)
                    else if (text.matches("Hướng dẫn: .*")) {
                        if (currentQuestion != null && "checkbox".equals(dialogType)) {
                            currentQuestion.setInstruction(text.replace("Hướng dẫn:", "").trim());
                        }
                    } else if (text.matches("Chủ đề: .*")) {
                        if (currentQuestion != null && "checkbox".equals(dialogType)) {
                            currentQuestion.setTopic(text.replace("Chủ đề:", "").trim());
                        }

                    }

                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (currentQuestion != null) {
                saveOptionsToQuestion(currentQuestion, options, correctIndexes);
                questions.add(currentQuestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    private boolean isBoldOrRedText(Object obj) {
        if (obj instanceof org.docx4j.wml.P) {
            org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;

            // Duyệt qua các phần tử trong đoạn văn bản
            for (Object o : paragraph.getContent()) {
                if (o instanceof javax.xml.bind.JAXBElement) {
                    o = ((javax.xml.bind.JAXBElement<?>) o).getValue();
                }

                if (o instanceof org.docx4j.wml.R) {
                    org.docx4j.wml.R run = (org.docx4j.wml.R) o;

                    // Kiểm tra in đậm
                    if (run.getRPr() != null && run.getRPr().getB() != null && run.getRPr().getB().isVal()) {
                        return true;
                    }

                    // Kiểm tra màu đỏ
                    if (run.getRPr() != null && run.getRPr().getColor() != null) {
                        String color = run.getRPr().getColor().getVal();
                        if ("FF0000".equalsIgnoreCase(color)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getNumberingSymbolWithCounter(org.docx4j.wml.P paragraph, WordprocessingMLPackage wordMLPackage) {
        if (paragraph.getPPr() != null && paragraph.getPPr().getNumPr() != null) {
            org.docx4j.wml.PPr.NumPr numPr = paragraph.getPPr().getNumPr();
            if (numPr.getNumId() != null && numPr.getNumId().getVal() != null) {
                questionCounter = questionCounter + 1;
                return "Câu " + questionCounter;
            }
        }
        return "No numbering";
    }

    private String extractTextWithBulletFill(Object obj) {
        if (obj instanceof org.docx4j.wml.P) {
            org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;
            StringBuilder sb = new StringBuilder();

            for (Object o : paragraph.getContent()) {
                if (o instanceof javax.xml.bind.JAXBElement) {
                    Object value = ((javax.xml.bind.JAXBElement<?>) o).getValue();
                    if (value instanceof org.docx4j.wml.R) {
                        o = value;
                    }
                }

                if (o instanceof org.docx4j.wml.R) {
                    org.docx4j.wml.R run = (org.docx4j.wml.R) o;
                    for (Object textObj : run.getContent()) {
                        if (textObj instanceof javax.xml.bind.JAXBElement) {
                            textObj = ((javax.xml.bind.JAXBElement<?>) textObj).getValue();
                        }
                        if (textObj instanceof org.docx4j.wml.Text) {
                            sb.append(((org.docx4j.wml.Text) textObj).getValue());
                        }
                    }
                }
            }

            // Trả về chuỗi đầy đủ, loại bỏ khoảng trắng thừa ở đầu và cuối
            return sb.toString().trim();
        }
        return "";
    }

    private String extractTextWithBullet(Object obj) {
        if (obj instanceof org.docx4j.wml.P) {
            org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;
            StringBuilder sb = new StringBuilder();

            // Kiểm tra nếu đoạn văn bản có bullet hoặc numbering
            String bulletSymbol = getBulletSymbol(paragraph);
            if (bulletSymbol != null) {
                sb.append(bulletSymbol).append(" ");
            }

            for (Object o : paragraph.getContent()) {
                if (o instanceof javax.xml.bind.JAXBElement) {
                    Object value = ((javax.xml.bind.JAXBElement<?>) o).getValue();
                    if (value instanceof org.docx4j.wml.R) {
                        o = value;
                    }
                }

                if (o instanceof org.docx4j.wml.R) {
                    org.docx4j.wml.R run = (org.docx4j.wml.R) o;
                    for (Object textObj : run.getContent()) {
                        if (textObj instanceof javax.xml.bind.JAXBElement) {
                            textObj = ((javax.xml.bind.JAXBElement<?>) textObj).getValue();
                        }
                        if (textObj instanceof org.docx4j.wml.Text) {
                            sb.append(((org.docx4j.wml.Text) textObj).getValue()).append(" ");
                        }
                    }
                }
            }
            return sb.toString().trim();
        }
        return "";
    }

    private String getBulletSymbol(org.docx4j.wml.P paragraph) {
        if (paragraph.getPPr() != null && paragraph.getPPr().getNumPr() != null) {
            org.docx4j.wml.PPr.NumPr numPr = paragraph.getPPr().getNumPr();
            if (numPr.getNumId() != null) {
                String numId = numPr.getNumId().getVal().toString();
                return "•";  // Trả về ký hiệu bullet (có thể thay đổi thành ký hiệu khác)
            }
        }
        return null;
    }

    //Upload Excel
    public void saveQuestionsFromExcel(MultipartFile file) throws IOException {
        List<Question> questionList = new ArrayList<>();

        // Tạo Workbook từ file Excel
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

        // Lặp qua các dòng trong sheet
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Bỏ qua hàng tiêu đề (header)
            if (row.getRowNum() == 0) {
                continue;
            }

            Question question = new Question();

            // Set thông tin câu hỏi từ file Excel
            question.setContent(getCellValue(row.getCell(0))); // Content
            question.setInstruction(getCellValue(row.getCell(1))); // Instruction
            question.setOptionA(getCellValue(row.getCell(2))); // Option A
            question.setOptionB(getCellValue(row.getCell(3))); // Option B
            question.setOptionC(getCellValue(row.getCell(4))); // Option C
            question.setOptionD(getCellValue(row.getCell(5))); // Option D
            String resultCheck = getCellValue(row.getCell(6)); // Result check (A, B, C, D)

            // Xử lý kết quả câu hỏi dựa vào Result Check
            if ("A".equals(resultCheck)) {
                question.setResult(question.getOptionA()); // Set đáp án đúng (Option A)
                question.setResult_check(resultCheck); // Set Result check là A
            } else if ("B".equals(resultCheck)) {
                question.setResult(question.getOptionB()); // Set đáp án đúng (Option B)
                question.setResult_check(resultCheck); // Set Result check là B
            } else if ("C".equals(resultCheck)) {
                question.setResult(question.getOptionC()); // Set đáp án đúng (Option C)
                question.setResult_check(resultCheck); // Set Result check là C
            } else if ("D".equals(resultCheck)) {
                question.setResult(question.getOptionD()); // Set đáp án đúng (Option D)
                question.setResult_check(resultCheck); // Set Result check là D
            }

            // Set thêm thông tin tạo và cập nhật
            question.setCreatedAt(new Date());
            question.setUpdatedAt(new Date());

            // Thêm câu hỏi vào danh sách
            questionList.add(question);
        }

        workbook.close();

        // Lưu tất cả câu hỏi vào cơ sở dữ liệu
        questionRepository.saveAll(questionList);
    }

    // Phương thức hỗ trợ để lấy giá trị của cell
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    public void deleteQuestions(List<Integer> ids) {
        questionRepository.deleteQuestionsByIds(ids);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public byte[] exportQuestionsToExcel() {
        List<Question> questions = getAllQuestions(); // Lấy tất cả câu hỏi từ database

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Questions");

            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Content", "Instruction", "Option A", "Option B", "Option C", "Option D", "Result Check"};

            // Ghi tiêu đề vào từng cột
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Ghi dữ liệu câu hỏi vào các dòng
            int rowNum = 1;
            for (Question question : questions) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(question.getContent()); // Content
                row.createCell(1).setCellValue(question.getInstruction()); // Instruction
                row.createCell(2).setCellValue(question.getOptionA()); // Option A
                row.createCell(3).setCellValue(question.getOptionB()); // Option B
                row.createCell(4).setCellValue(question.getOptionC()); // Option C
                row.createCell(5).setCellValue(question.getOptionD()); // Option D

                // Cột Result Check (A, B, C, D)
                String resultCheck = question.getResult_check(); // Lấy giá trị của result_check
                row.createCell(6).setCellValue(resultCheck); // Result Check
            }

            // Ghi Workbook vào byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi xuất dữ liệu ra file Excel");
        }
    }

    public byte[] exportQuestionsToExcelByID(List<String> listID) {
        List<Question> questions = new ArrayList<>();
        for (String item : listID) {

            Optional<Question> questionOptional = questionRepository.findById(Integer.parseInt(item));
            if (questionOptional.isPresent()) {
                questions.add(questionOptional.get());
            }
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Questions");

            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Content", "Instruction", "Option A", "Option B", "Option C", "Option D", "Result Check"};

            // Ghi tiêu đề vào từng cột
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Ghi dữ liệu câu hỏi vào các dòng
            int rowNum = 1;
            for (Question question : questions) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(question.getContent()); // Content
                row.createCell(1).setCellValue(question.getInstruction()); // Instruction
                row.createCell(2).setCellValue(question.getOptionA()); // Option A
                row.createCell(3).setCellValue(question.getOptionB()); // Option B
                row.createCell(4).setCellValue(question.getOptionC()); // Option C
                row.createCell(5).setCellValue(question.getOptionD()); // Option D

                // Cột Result Check (A, B, C, D)
                String resultCheck = question.getResult_check(); // Lấy giá trị của result_check
                row.createCell(6).setCellValue(resultCheck); // Result Check
            }

            // Ghi Workbook vào byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi xuất dữ liệu ra file Excel");
        }
    }

    private void handleCheckboxType(MainDocumentPart mainDocumentPart, Question question) {
        List<String> correctIndexes = Arrays.asList(question.getResult_check().split(","));
        addOption(mainDocumentPart, "Lựa chọn 1: " + question.getOptionA(), correctIndexes.contains("1"));
        addOption(mainDocumentPart, "Lựa chọn 2: " + question.getOptionB(), correctIndexes.contains("2"));
        addOption(mainDocumentPart, "Lựa chọn 3: " + question.getOptionC(), correctIndexes.contains("3"));
        addOption(mainDocumentPart, "Lựa chọn 4: " + question.getOptionD(), correctIndexes.contains("4"));
        // Đáp án đúng được tô màu đỏ và in đậm
//        addParagraph(mainDocumentPart, "Đáp án đúng: " + question.getResult(), true, "22", "FF0000", false);
    }

    private void addOptionMulti(MainDocumentPart mainDocumentPart, String text, boolean isCorrect) {
        try {
            org.docx4j.wml.P paragraph = mainDocumentPart.createParagraphOfText(text);

            org.docx4j.wml.RPr runProperties = new org.docx4j.wml.RPr();

            // Nếu là lựa chọn đúng, định dạng in đậm và màu đỏ
            if (isCorrect) {
                org.docx4j.wml.BooleanDefaultTrue bold = new org.docx4j.wml.BooleanDefaultTrue();
                runProperties.setB(bold);

                org.docx4j.wml.Color colorProperty = new org.docx4j.wml.Color();
                colorProperty.setVal("FF0000");  // Màu đỏ
                runProperties.setColor(colorProperty);
            }

            org.docx4j.wml.R run = (org.docx4j.wml.R) paragraph.getContent().get(0);
            run.setRPr(runProperties);

            mainDocumentPart.addObject(paragraph);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi thêm lựa chọn câu hỏi");
        }
    }

    private void handleMultipleChoiceType(MainDocumentPart mainDocumentPart, Question question) {
        String correctAnswer = question.getResult_check();
        addOptionMulti(mainDocumentPart, "A. " + question.getOptionA(), "A".equalsIgnoreCase(correctAnswer));
        addOptionMulti(mainDocumentPart, "B. " + question.getOptionB(), "B".equalsIgnoreCase(correctAnswer));
        addOptionMulti(mainDocumentPart, "C. " + question.getOptionC(), "C".equalsIgnoreCase(correctAnswer));
        addOptionMulti(mainDocumentPart, "D. " + question.getOptionD(), "D".equalsIgnoreCase(correctAnswer));
    }

    public byte[] exportQuestionsToDocxByID(List<String> listID) {
        List<Question> questions = new ArrayList<>();
        for (String item : listID) {
            Optional<Question> questionOptional = questionRepository.findById(Integer.parseInt(item));
            questionOptional.ifPresent(questions::add);
        }

        try {
            // Tạo tài liệu DOCX
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();

            // Tạo tiêu đề cho tài liệu
            addParagraph(mainDocumentPart, "Danh sách câu hỏi", true, "28", "000000", true);

            int questionNumber = 1;
            for (Question question : questions) {
                // Thêm nội dung câu hỏi
                addParagraph(mainDocumentPart, "Câu " + questionNumber + ": " + question.getContent(), true, "24", "000000", false);
                String type = question.getType();
                type = switch (type) {
                    case "essay" -> "Tự luận";
                    case "checkbox" -> "Checkbox";
                    case "fill-in-the-blank" -> "Điền khuyết";
                    default -> "Trắc nghiệm";
                };
                switch (question.getType()) {
                    case "essay":
                        addParagraph(mainDocumentPart, "Loại câu hỏi: " + type, false, "20", "000000", false);
                        break;

                    case "checkbox":
                        addParagraph(mainDocumentPart, "Loại câu hỏi: " + type, false, "20", "000000", false);
                        handleCheckboxType(mainDocumentPart, question);
                        break;

                    case "multiple-choice":
                        addParagraph(mainDocumentPart, "Loại câu hỏi: " + type, false, "20", "000000", false);
                        handleMultipleChoiceType(mainDocumentPart, question);
                        break;

                    case "fill-in-the-blank":
                        addParagraph(mainDocumentPart, "Loại câu hỏi: " + type, false, "20", "000000", false);
                        addParagraph(mainDocumentPart, "Từ khuyết: " + question.getResult(), false, "20", "000000", false);
                        break;

                    default:
                        addParagraph(mainDocumentPart, "Loại câu hỏi chưa được hỗ trợ", false, "20", "FF0000", false);
                        break;
                }


                // Thêm hướng dẫn
                addParagraph(mainDocumentPart, "Hướng dẫn: " + question.getInstruction(), false, "20", "000000", false);
                String level = question.getLevel();
                level = switch (level) {
                    case "1" -> "Dễ";
                    case "2" -> "Trung bình";
                    default -> "Khó";
                };
                String topic = question.getTopic();

//                normalizeVietnamese

                addParagraph(mainDocumentPart, "Mức độ: " + level, false, "20", "000000", false);
                addParagraph(mainDocumentPart, "Chủ đề: " + topic, false, "20", "000000", false);
                questionNumber++;
            }

            // Xuất file ra byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            wordMLPackage.save(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi xuất dữ liệu ra file DOCX");
        }
    }

    private void addParagraph(MainDocumentPart mainDocumentPart, String text, boolean isBold, String fontSize, String color, boolean isCentered) {
        try {
            // Tạo đoạn văn mới
            org.docx4j.wml.P paragraph = mainDocumentPart.createParagraphOfText(text);

            // Kiểm tra và khởi tạo PPr
            if (paragraph.getPPr() == null) {
                paragraph.setPPr(new org.docx4j.wml.PPr());
            }

            org.docx4j.wml.PPr paragraphProperties = paragraph.getPPr();

            // Định dạng đoạn văn bản
            org.docx4j.wml.RPr runProperties = new org.docx4j.wml.RPr();

            if (isBold) {
                org.docx4j.wml.BooleanDefaultTrue bold = new org.docx4j.wml.BooleanDefaultTrue();
                runProperties.setB(bold);
            }

            // Thiết lập màu sắc
            org.docx4j.wml.Color colorProperty = new org.docx4j.wml.Color();
            colorProperty.setVal(color);
            runProperties.setColor(colorProperty);

            // Thiết lập kích thước phông chữ
            org.docx4j.wml.HpsMeasure size = new org.docx4j.wml.HpsMeasure();
            size.setVal(new BigInteger(fontSize));
            runProperties.setSz(size);

            // Gắn thuộc tính vào đoạn văn
            org.docx4j.wml.R run = (org.docx4j.wml.R) paragraph.getContent().get(0);
            run.setRPr(runProperties);

            // Căn giữa nếu cần
            if (isCentered) {
                org.docx4j.wml.Jc alignment = new org.docx4j.wml.Jc();
                alignment.setVal(org.docx4j.wml.JcEnumeration.CENTER);
                paragraphProperties.setJc(alignment);
            }

            mainDocumentPart.addObject(paragraph);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi thêm đoạn văn bản");
        }
    }

    private void addOption(MainDocumentPart mainDocumentPart, String text, boolean isCorrect) {
        try {
            org.docx4j.wml.P paragraph = mainDocumentPart.createParagraphOfText(text);

            // Kiểm tra và khởi tạo RPr
            org.docx4j.wml.RPr runProperties = new org.docx4j.wml.RPr();

            if (isCorrect) {
                org.docx4j.wml.BooleanDefaultTrue bold = new org.docx4j.wml.BooleanDefaultTrue();
                runProperties.setB(bold);

                org.docx4j.wml.Color colorProperty = new org.docx4j.wml.Color();
                colorProperty.setVal("FF0000");
                runProperties.setColor(colorProperty);
            }

            org.docx4j.wml.R run = (org.docx4j.wml.R) paragraph.getContent().get(0);
            run.setRPr(runProperties);

            mainDocumentPart.addObject(paragraph);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi thêm lựa chọn câu hỏi");
        }
    }

    //Import hon hop
    public List<Question> importQuestionsFromDocx(MultipartFile file) {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            List<Object> paragraphs = wordMLPackage.getMainDocumentPart().getContent();

            Question currentQuestion = null;
            String currentLevel = "2"; // Mặc định trung bình
            String questionType = "";
            Map<Integer, String> options = new LinkedHashMap<>();
            List<Integer> correctIndexes = new ArrayList<>();
            for (Object obj : paragraphs) {
                if (obj instanceof org.docx4j.wml.P) {
                    String text = extractTextWithBulletFillMixed(obj).trim();

                    // Nhận diện tiêu đề và loại câu hỏi
                    if (text.startsWith("Danh sách câu hỏi")) {
                        continue;
                    }

                    // Nhận diện câu hỏi mới
                    if (text.matches("^Câu \\d+: .*")) {
                        if (currentQuestion != null) {
                            // Gán các lựa chọn và đáp án vào câu hỏi hiện tại
                            if (currentQuestion.getType().equals("checkbox")) {
                                saveOptionsToQuestion(currentQuestion, options, correctIndexes);
                                questions.add(currentQuestion);
                            } else {
                                questions.add(currentQuestion);  // Lưu câu hỏi trước đó
                            }
                        }

                        currentQuestion = new Question();
                        currentQuestion.setContent(text.replaceFirst("^Câu \\d+: ", "").trim());
                        options.clear();
                        correctIndexes.clear();
                    }

                    // Nhận diện loại câu hỏi
                    if (text.startsWith("Loại câu hỏi:")) {
                        questionType = text.replace("Loại câu hỏi:", "").trim().toLowerCase();
                        questionType = switch (questionType) {
                            case "tự luận" -> "essay";
                            case "checkbox" -> "checkbox";
                            case "điền khuyết" -> "fill-in-the-blank";
                            default -> "multiple-choice";
                        };
                        currentQuestion.setType(questionType);
                    }

                    if (text.matches("Lựa chọn \\d+: .*")) {
                        if (currentQuestion != null) {
                            int choiceIndex = Integer.parseInt(text.replaceAll("Lựa chọn (\\d+): .*", "$1"));
                            String choiceText = text.replaceFirst("Lựa chọn \\d+: ", "").trim();

                            boolean isCorrect = false;
                            if (isBoldOrRedText(obj)) {
                                isCorrect = true;
                            }
                            options.put(choiceIndex, choiceText);
                            if (isCorrect) {
                                correctIndexes.add(choiceIndex);
                            }
                        }
                    } else if (text.matches("A\\. .*|B\\. .*|C\\. .*|D\\. .*")) {
                        if (currentQuestion != null) {
                            String answerOption = text.substring(0, 1);
                            String answerText = text.substring(3).trim();

                            switch (answerOption) {
                                case "A":
                                    currentQuestion.setOptionA(answerText);
                                    break;
                                case "B":
                                    currentQuestion.setOptionB(answerText);
                                    break;
                                case "C":
                                    currentQuestion.setOptionC(answerText);
                                    break;
                                case "D":
                                    currentQuestion.setOptionD(answerText);
                                    break;
                            }

                            if (isBoldOrRedText(obj)) {
                                currentQuestion.setResult(answerText);
                                currentQuestion.setResult_check(answerOption);
                            }
                        }
                    }

                    // Nhận diện từ khuyết nếu là câu điền khuyết
                    if (text.startsWith("Từ khuyết:")) {
                        if (currentQuestion != null) {
                            currentQuestion.setResult(text.replace("Từ khuyết:", "").trim());
                            currentQuestion.setResult_check(text.replace("Từ khuyết:", "").trim());
                        }
                    }

                    // Nhận diện hướng dẫn
                    if (text.startsWith("Hướng dẫn:")) {
                        if (currentQuestion != null) {
                            currentQuestion.setInstruction(text.replace("Hướng dẫn:", "").trim());
                        }
                    }
                    if (text.matches("Chủ đề: .*")) {
                        if (currentQuestion != null) {
                            currentQuestion.setTopic(text.replace("Chủ đề:", "").trim());
                        }
                    }


                    // Nhận diện mức độ
                    if (text.startsWith("Mức độ:")) {
                        if (currentQuestion != null) {
                            String levelText = text.replace("Mức độ:", "").trim();
                            switch (levelText) {
                                case "Dễ":
                                    currentQuestion.setLevel("1");
                                    break;
                                case "Trung bình":
                                    currentQuestion.setLevel("2");
                                    break;
                                default:
                                    currentQuestion.setLevel("3");
                                    break;
                            }
                        }
                    }
                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (currentQuestion != null) {
                saveOptionsToQuestion(currentQuestion, options, correctIndexes);
                questions.add(currentQuestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi đọc dữ liệu từ file DOCX");
        }

        return questions;
    }

    private String extractTextWithBulletFillMixed(Object obj) {
        if (obj instanceof org.docx4j.wml.P) {
            org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;
            StringBuilder sb = new StringBuilder();

            for (Object o : paragraph.getContent()) {
                if (o instanceof javax.xml.bind.JAXBElement) {
                    Object value = ((javax.xml.bind.JAXBElement<?>) o).getValue();
                    if (value instanceof org.docx4j.wml.R) {
                        o = value;
                    }
                }

                if (o instanceof org.docx4j.wml.R) {
                    org.docx4j.wml.R run = (org.docx4j.wml.R) o;
                    for (Object textObj : run.getContent()) {
                        if (textObj instanceof javax.xml.bind.JAXBElement) {
                            textObj = ((javax.xml.bind.JAXBElement<?>) textObj).getValue();
                        }
                        if (textObj instanceof org.docx4j.wml.Text) {
                            sb.append(((org.docx4j.wml.Text) textObj).getValue()).append(" ");
                        }
                    }
                }
            }

            return sb.toString().trim();
        }
        return "";
    }

    public Page<Question> getQuestionsByTestIdAdmin(Integer testId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Create a Pageable object
        return questionRepository.findQuestionsByTestIdAdmin(testId, pageable);
    }

    public AdminQuestionGetDTO getQuestionDetailsByIdAdmin(int id) {
        return questionRepository.getQuestionDetailsById(id).stream()
                .map(result -> new AdminQuestionGetDTO(
                        (int) result[0],                // id
                        (String) result[1],              // content
                        (String) result[2],              // optionA
                        (String) result[3],              // optionB
                        (String) result[4],              // optionC
                        (String) result[5],              // optionD
                        (String) result[6],              // result
                        (String) result[7],              // instruction
                        (String) result[8]               // resultCheck
                ))
                .findFirst()
                .orElse(null);
    }

    public boolean updateQuestionAdmin(int id, AdminQuestionGetDTO adminQuestionGetDTO) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return false;
        }
        question.setContent(adminQuestionGetDTO.getContent());
        question.setResult(adminQuestionGetDTO.getResult());
        question.setInstruction(adminQuestionGetDTO.getInstruction());
        question.setResult_check(adminQuestionGetDTO.getResultCheck());
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
        return true;
    }

    public boolean updateQuestionAdminV2(int id, AdminQuestionGetDTO_V2 adminQuestionGetDTO) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return false;
        }

        if (question.getType().equals("fill-in-the-blank")) {
            question.setContent(adminQuestionGetDTO.getContent());
            question.setResult(adminQuestionGetDTO.getResult());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
            question.setInstruction(adminQuestionGetDTO.getInstruction());
            question.setLevel(adminQuestionGetDTO.getLevel());
        } else if (question.getType().equals("essay")) {
            question.setContent(adminQuestionGetDTO.getContent());
            question.setInstruction(adminQuestionGetDTO.getInstruction());
            question.setLevel(adminQuestionGetDTO.getLevel());
        } else {
            question.setContent(adminQuestionGetDTO.getContent());
            question.setResult(adminQuestionGetDTO.getResult());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
            question.setInstruction(adminQuestionGetDTO.getInstruction());

            question.setOptionA(adminQuestionGetDTO.getOptionA());
            question.setOptionB(adminQuestionGetDTO.getOptionB());
            question.setOptionC(adminQuestionGetDTO.getOptionC());
            question.setOptionD(adminQuestionGetDTO.getOptionD());

            question.setLevel(adminQuestionGetDTO.getLevel());
        }


        question.setUpdatedAt(new Date());
        questionRepository.save(question);
        return true;
    }

    public boolean updateQuestionAdminV2_Checkbox(int id, CheckboxQuestionDTO_V3 adminQuestionGetDTO) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return false;
        }
        question.setContent(adminQuestionGetDTO.getContent());
        question.setLevel(adminQuestionGetDTO.getLevel());
        question.setInstruction(adminQuestionGetDTO.getInstruction());

        question.setUpdatedAt(new Date());

        List<OptionDTO> list = adminQuestionGetDTO.getOptions();
        StringBuilder list_resultCheck = new StringBuilder();
        StringBuilder list_result = new StringBuilder();
        char[] mapping = {'1', '2', '3', '4'};
        for (int i = 0; i < list.size(); i++) { // Fix: Chỉ mục bắt đầu từ 0
            String optionText = list.get(i).getText();

            // Gán giá trị cho các option tương ứng
            switch (i) {
                case 0 -> question.setOptionA(optionText);
                case 1 -> question.setOptionB(optionText);
                case 2 -> question.setOptionC(optionText);
                case 3 -> question.setOptionD(optionText);
            }

            // Nếu option đúng, thêm vào danh sách kết quả, có dấu phẩy nếu không phải giá trị đầu tiên
            if (list.get(i).isCorrect()) {

                if (!list_resultCheck.isEmpty()) {
                    list_resultCheck.append(",");
                    list_result.append(",");
                }
                list_resultCheck.append(mapping[i]);
                switch (i) {
                    case 0 -> list_result.append(question.getOptionA());
                    case 1 -> list_result.append(question.getOptionB());
                    case 2 -> list_result.append(question.getOptionC());
                    case 3 -> list_result.append(question.getOptionD());
                }
            }
        }
        question.setResult_check(list_resultCheck.toString());
        question.setResult(list_result.toString());

        questionRepository.save(question);
        return true;
    }

    public void addQuestionMultiAdmin(AdminQuestionMultiPostDTO_V2 adminQuestionGetDTO) {
        Question question = new Question();
        Optional<Account> accountOptional = accountRepository.findById(adminQuestionGetDTO.getAccountId());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không tồn tại với ID: " + adminQuestionGetDTO.getAccountId());
        }

        // Kiểm tra và lấy Course
        Optional<Course> courseOptional = courseRepository.findById(adminQuestionGetDTO.getCourseId());
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + adminQuestionGetDTO.getCourseId());
        }

        question.setContent(adminQuestionGetDTO.getContent());
        question.setOptionA(adminQuestionGetDTO.getOptionA());
        question.setOptionB(adminQuestionGetDTO.getOptionB());
        question.setOptionC(adminQuestionGetDTO.getOptionC());
        question.setOptionD(adminQuestionGetDTO.getOptionD());

        if (adminQuestionGetDTO.getResultCheck().equals("A")) {
            question.setResult(adminQuestionGetDTO.getOptionA());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else if (adminQuestionGetDTO.getResultCheck().equals("B")) {
            question.setResult(adminQuestionGetDTO.getOptionB());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else if (adminQuestionGetDTO.getResultCheck().equals("C")) {
            question.setResult(adminQuestionGetDTO.getOptionC());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else {
            question.setResult(adminQuestionGetDTO.getOptionD());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        }

        question.setInstruction(adminQuestionGetDTO.getInstruction());
        question.setType(adminQuestionGetDTO.getType());
        question.setLevel(adminQuestionGetDTO.getLevel());
        question.setTopic(adminQuestionGetDTO.getTopic());

        question.setCourse(courseOptional.get());
        question.setAccount(accountOptional.get());
        question.setCreatedAt(new Date());
        question.setUpdatedAt(new Date());

        questionRepository.save(question);
    }

    public void addQuestionFillAdmin(AdminQuestionMultiPostDTO_V2 adminQuestionGetDTO) {
        Question question = new Question();
        Optional<Account> accountOptional = accountRepository.findById(adminQuestionGetDTO.getAccountId());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không tồn tại với ID: " + adminQuestionGetDTO.getAccountId());
        }

        // Kiểm tra và lấy Course
        Optional<Course> courseOptional = courseRepository.findById(adminQuestionGetDTO.getCourseId());
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + adminQuestionGetDTO.getCourseId());
        }

        question.setContent(adminQuestionGetDTO.getContent());
        question.setInstruction(adminQuestionGetDTO.getInstruction());
        question.setType(adminQuestionGetDTO.getType());
        question.setLevel(adminQuestionGetDTO.getLevel());
        question.setTopic(adminQuestionGetDTO.getTopic());
        question.setResult_check(adminQuestionGetDTO.getResult());
        question.setResult(adminQuestionGetDTO.getResult());
        question.setCourse(courseOptional.get());
        question.setAccount(accountOptional.get());
        question.setCreatedAt(new Date());
        question.setUpdatedAt(new Date());

        questionRepository.save(question);
    }

    public void addQuestionCheckboxAdmin(CheckboxQuestionDTO_V2 adminQuestionGetDTO) {
        Question question = new Question();
        Optional<Account> accountOptional = accountRepository.findById(adminQuestionGetDTO.getAccountId());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không tồn tại với ID: " + adminQuestionGetDTO.getAccountId());
        }

        Optional<Course> courseOptional = courseRepository.findById(adminQuestionGetDTO.getCourseId());
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + adminQuestionGetDTO.getCourseId());
        }

        question.setContent(adminQuestionGetDTO.getContent());
        question.setType(adminQuestionGetDTO.getType());
        question.setLevel(adminQuestionGetDTO.getLevel());
        question.setTopic(adminQuestionGetDTO.getTopic());
        question.setCourse(courseOptional.get());
        question.setAccount(accountOptional.get());
        question.setCreatedAt(new Date());
        question.setUpdatedAt(new Date());
        question.setInstruction(adminQuestionGetDTO.getInstruction());

        List<OptionDTO> checkboxs = adminQuestionGetDTO.getOptions();
        char[] mapping = {'1', '2', '3', '4'};
        StringBuilder list_resultCheck = new StringBuilder();
        StringBuilder list_result = new StringBuilder();

        for (int i = 0; i < checkboxs.size(); i++) { // Fix: Chỉ mục bắt đầu từ 0
            String optionText = checkboxs.get(i).getText();

            // Gán giá trị cho các option tương ứng
            switch (i) {
                case 0 -> question.setOptionA(optionText);
                case 1 -> question.setOptionB(optionText);
                case 2 -> question.setOptionC(optionText);
                case 3 -> question.setOptionD(optionText);
            }

            // Nếu option đúng, thêm vào danh sách kết quả, có dấu phẩy nếu không phải giá trị đầu tiên
            if (checkboxs.get(i).isCorrect()) {

                if (!list_resultCheck.isEmpty()) {
                    list_resultCheck.append(",");
                    list_result.append(",");
                }
                list_resultCheck.append(mapping[i]);
                switch (i) {
                    case 0 -> list_result.append(question.getOptionA());
                    case 1 -> list_result.append(question.getOptionB());
                    case 2 -> list_result.append(question.getOptionC());
                    case 3 -> list_result.append(question.getOptionD());
                }
            }
        }

        question.setResult_check(list_resultCheck.toString());
        question.setResult(list_result.toString());
        questionRepository.save(question);
    }

    public void addQuestionEssayAdmin(AdminQuestionMultiPostDTO_V2 adminQuestionGetDTO) {
        Question question = new Question();
        Optional<Account> accountOptional = accountRepository.findById(adminQuestionGetDTO.getAccountId());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không tồn tại với ID: " + adminQuestionGetDTO.getAccountId());
        }

        Optional<Course> courseOptional = courseRepository.findById(adminQuestionGetDTO.getCourseId());
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + adminQuestionGetDTO.getCourseId());
        }

        question.setContent(adminQuestionGetDTO.getContent());
        question.setType(adminQuestionGetDTO.getType());
        question.setLevel(adminQuestionGetDTO.getLevel());
        question.setTopic(adminQuestionGetDTO.getTopic());
        question.setCourse(courseOptional.get());
        question.setAccount(accountOptional.get());
        question.setCreatedAt(new Date());
        question.setUpdatedAt(new Date());
        question.setInstruction(adminQuestionGetDTO.getInstruction());
        questionRepository.save(question);
    }

    public Page<AdminQuestionGetDTO_V2> getAllQuestionsAdmin(int page, int size) {
        // Tạo đối tượng Pageable từ page và size
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để lấy dữ liệu phân trang
        Page<Object[]> result = questionRepository.getAllQuestions(pageable);

        // Chuyển đổi từ Object[] sang DTO
        List<AdminQuestionGetDTO_V2> dtoList = result.getContent().stream()
                .map(row -> new AdminQuestionGetDTO_V2(
                        (Integer) row[0],  // questionId
                        (String) row[1],   // content
                        (String) row[2],   // optionA
                        (String) row[3],   // optionB
                        (String) row[4],   // optionC
                        (String) row[5],   // optionD
                        (String) row[6],   // result
                        (String) row[7],   // instruction
                        (String) row[8],    // resultCheck
                        (String) row[9],
                        (String) row[10],
                        (Integer) row[11],
                        (Integer) row[12],
                        (String) row[13],
                        (LocalDateTime) row[14]

                ))
                .collect(Collectors.toList());

        // Trả về Page<AdminQuestionGetDTO>
        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }

    //    public Page<AdminQuestionGetDTO_V2> getQuestionsByFilter(List<String> topics, Integer courseId, Integer accountId, String type, String level, String content, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        // Gọi repository để lấy kết quả từ database
//        Page<Object[]> result = questionRepository.findQuestionsByConditions(topics, courseId, accountId, type, level, content, pageable);
//
//        // Chuyển đổi từ Object[] sang DTO
//        List<AdminQuestionGetDTO_V2> dtoList = result.getContent().stream()
//                .map(row -> new AdminQuestionGetDTO_V2(
//                        (Integer) row[0],  // questionId
//                        (String) row[1],   // content
//                        (String) row[2],   // optionA
//                        (String) row[3],   // optionB
//                        (String) row[4],   // optionC
//                        (String) row[5],   // optionD
//                        (String) row[6],   // result
//                        (String) row[7],   // instruction
//                        (String) row[8],    // resultCheck
//                        (String) row[9],
//                        (String) row[10],
//                        (Integer) row[11],
//                        (Integer) row[12],
//                        (String) row[13],
//                        row[14] instanceof Timestamp ? ((Timestamp) row[14]).toLocalDateTime() : null
//
//                ))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
//    }
    public Page<AdminQuestionGetDTO_V2> getQuestionsByFilter(List<String> topics, Integer courseId, Integer accountId, String type, String level, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy câu hỏi từ repository
        Page<Question> questionPage = questionRepository.findQuestionsByConditions(topics, courseId, accountId, type, level, content, pageable);

        // Chuyển từ Entity Question sang DTO AdminQuestionGetDTO_V2
        List<AdminQuestionGetDTO_V2> dtoList = questionPage.getContent().stream()
                .map(row -> new AdminQuestionGetDTO_V2(
                        row.getId(),  // questionId
                        row.getContent(),  // content
                        row.getOptionA(),  // optionA
                        row.getOptionB(),  // optionB
                        row.getOptionC(),  // optionC
                        row.getOptionD(),  // optionD
                        row.getResult(),  // result
                        row.getInstruction(),  // instruction
                        row.getResult_check(),  // resultCheck
                        row.getLevel(),  // level
                        row.getType(),  // type
                        row.getAccount().getId(),  // accountId
                        row.getCourse().getId(),  // courseId
                        row.getTopic(),  // topic
                        convertDateToLocalDateTime(row.getCreatedAt())
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, questionPage.getTotalElements());
    }

    public Page<AdminQuestionGetDTO_V2> getQuestionsByFilterExam(Integer courseId, Integer accountId, String type, String level, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy câu hỏi từ repository
        Page<Question> questionPage = questionRepository.findQuestionsByConditionsExam(courseId, accountId, type, level, content, pageable);

        // Chuyển từ Entity Question sang DTO AdminQuestionGetDTO_V2
        List<AdminQuestionGetDTO_V2> dtoList = questionPage.getContent().stream()
                .map(row -> new AdminQuestionGetDTO_V2(
                        row.getId(),  // questionId
                        row.getContent(),  // content
                        row.getOptionA(),  // optionA
                        row.getOptionB(),  // optionB
                        row.getOptionC(),  // optionC
                        row.getOptionD(),  // optionD
                        row.getResult(),  // result
                        row.getInstruction(),  // instruction
                        row.getResult_check(),  // resultCheck
                        row.getLevel(),  // level
                        row.getType(),  // type
                        row.getAccount().getId(),  // accountId
                        row.getCourse().getId(),  // courseId
                        row.getTopic(),  // topic
                        convertDateToLocalDateTime(row.getCreatedAt())
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, questionPage.getTotalElements());
    }

    public Page<AdminQuestionGetDTO_V2> getQuestionsByFilterBank(String topics, Integer courseId, Integer accountId, String type, String level, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy câu hỏi từ repository
        Page<Question> questionPage = questionRepository.findQuestionsByConditionsBank(topics, courseId, accountId, type, level, content, pageable);

        // Chuyển từ Entity Question sang DTO AdminQuestionGetDTO_V2
        List<AdminQuestionGetDTO_V2> dtoList = questionPage.getContent().stream()
                .map(row -> new AdminQuestionGetDTO_V2(
                        row.getId(),  // questionId
                        row.getContent(),  // content
                        row.getOptionA(),  // optionA
                        row.getOptionB(),  // optionB
                        row.getOptionC(),  // optionC
                        row.getOptionD(),  // optionD
                        row.getResult(),  // result
                        row.getInstruction(),  // instruction
                        row.getResult_check(),  // resultCheck
                        row.getLevel(),  // level
                        row.getType(),  // type
                        row.getAccount().getId(),  // accountId
                        row.getCourse().getId(),  // courseId
                        row.getTopic(),  // topic
                        convertDateToLocalDateTime(row.getCreatedAt())
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, questionPage.getTotalElements());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        // Chuyển Date sang Instant và sau đó chuyển thành LocalDateTime với múi giờ hệ thống
        return date.toInstant()
                .atZone(ZoneId.systemDefault())  // Sử dụng múi giờ hệ thống
                .toLocalDateTime();
    }

    public CheckboxQuestionDTO_V3 mapQuestionToCheckboxDTO(Question item) {
        // Lấy danh sách các đáp án đúng từ resultCheck
        String resultCheck = item.getResult_check();
        List<Character> correctAnswers = resultCheck != null
                ? resultCheck.chars().mapToObj(c -> (char) c).toList()
                : new ArrayList<>();

        // Mapping các đáp án (A, B, C, D)
        List<OptionDTO> options = new ArrayList<>();

        if (item.getOptionA() != null) {
            options.add(new OptionDTO(item.getOptionA(), correctAnswers.contains('1')));
        }
        if (item.getOptionB() != null) {
            options.add(new OptionDTO(item.getOptionB(), correctAnswers.contains('2')));
        }
        if (item.getOptionC() != null) {
            options.add(new OptionDTO(item.getOptionC(), correctAnswers.contains('3')));
        }
        if (item.getOptionD() != null) {
            options.add(new OptionDTO(item.getOptionD(), correctAnswers.contains('4')));
        }

        // Tạo DTO và trả về
        return new CheckboxQuestionDTO_V3(
                item.getId(),
                item.getContent(),
                item.getType(),
                item.getCourse().getId(),
                item.getAccount().getId(),
                item.getLevel(),
                item.getInstruction(),
                new ArrayList<>(options)
        );
    }

    public Optional<CheckboxQuestionDTO_V3> getCheckboxQuestionByIdAndType(Long id, String type) {
        Optional<Question> item = questionRepository.findByIdAndType(id, type);

        if (item.isPresent()) {
            return Optional.of(mapQuestionToCheckboxDTO(item.get()));
        }

        return Optional.empty();
    }

//    @Autowired
//    private EntityManager entityManager;
//
//    public Page<AdminQuestionGetDTO_V2> getQuestionsByFilter(
//            Integer courseId, Integer accountId, String type, String level, String content, int page, int size) {
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
//        Root<Question> root = cq.from(Question.class);
//
//        // Danh sách các điều kiện lọc
//        List<Predicate> predicates = new ArrayList<>();
//        List<Question> questions = new ArrayList<>();
//        if (courseId != null && accountId != null) {
//            predicates.add(cb.equal(root.get("course").get("id"), courseId)); // Truy cập ID của khóa học
//            predicates.add(cb.equal(root.get("account").get("id"), accountId)); // Truy cập ID của tài khoản
//            if (type != null) {
//                predicates.add(cb.equal(root.get("type"), type));
//            }
//            if (level != null) {
//                predicates.add(cb.equal(root.get("level"), level));
//            }
//            if (content != null) {
//                predicates.add(cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
//            }
//
//            // Kết hợp các điều kiện lọc
//            cq.where(cb.and(predicates.toArray(new Predicate[0])));
//            // Sắp xếp (nếu cần)
//            cq.orderBy(cb.asc(root.get("id")));
//            // Tạo truy vấn
//            TypedQuery<Question> query = entityManager.createQuery(cq);
//            // Áp dụng phân trang
//            query.setFirstResult(page * size);
//            query.setMaxResults(size);
//            // Lấy kết quả
//            questions = query.getResultList();
//        }
//
//
//        // Đếm tổng số bản ghi (phục vụ phân trang)
//        long total = countQuestionsByFilter(courseId, accountId, type, level, content);
//
//            List<AdminQuestionGetDTO_V2> dtoList = questions.stream()
//                    .map(q -> new AdminQuestionGetDTO_V2(
//                            q.getId(),
//                            q.getContent(),
//                            q.getOptionA(),
//                            q.getOptionB(),
//                            q.getOptionC(),
//                            q.getOptionD(),
//                            q.getResult(),
//                            q.getInstruction(),
//                            q.getResult_check(),
//                            q.getLevel(),
//                            q.getType(),
//                            q.getAccount().getId(),
//                            q.getCourse().getId()
//                    ))
//                    .collect(Collectors.toList());
//            return new PageImpl<>(dtoList, PageRequest.of(page, size), total);
//
//    }
//
//    private long countQuestionsByFilter(Integer courseId, Integer accountId, String type, String level, String content) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
//        Root<Question> root = cq.from(Question.class);
//
//        // Danh sách các điều kiện lọc
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (courseId != null) {
//            predicates.add(cb.equal(root.get("course").get("id"), courseId)); // Truy cập ID của khóa học
//        }
//        if (accountId != null) {
//            predicates.add(cb.equal(root.get("account").get("id"), accountId)); // Truy cập ID của tài khoản
//        }
//
//
//        // Lọc thêm: type, level, content
//        if (type != null) {
//            predicates.add(cb.equal(root.get("type"), type));
//        }
//        if (level != null) {
//            predicates.add(cb.equal(root.get("level"), level));
//        }
//        if (content != null) {
//            predicates.add(cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
//        }
//
//        // Kết hợp các điều kiện lọc
//        cq.select(cb.count(root)).where(cb.and(predicates.toArray(new Predicate[0])));
//
//        return entityManager.createQuery(cq).getSingleResult();
//    }


    public List<AdminQuestionGetDTO> getAllQuestionsAdminList() {

        // Gọi repository để lấy dữ liệu phân trang
        List<Object[]> result = questionRepository.getAllQuestionsList();

        List<AdminQuestionGetDTO> dtoList = result.stream()
                .map(row -> new AdminQuestionGetDTO(
                        (Integer) row[0],  // questionId
                        (String) row[1],   // content
                        (String) row[2],   // optionA
                        (String) row[3],   // optionB
                        (String) row[4],   // optionC
                        (String) row[5],   // optionD
                        (String) row[6],   // result
                        (String) row[7],   // instruction
                        (String) row[8]    // resultCheck
                ))
                .collect(Collectors.toList());

        // Trả về List<AdminQuestionGetDTO>
        return dtoList;
    }

    public Question deleteQuestionAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Question> accountOpt = questionRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Question account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return questionRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + testID);
        }
    }

    public Question activeQuestionAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Question> accountOpt = questionRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Question account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return questionRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }

    public boolean copyQuestionsToCourse(List<Integer> questionIds, Integer targetCourseId) {
        try {
            List<Question> questions = new ArrayList<>();
            for (Integer item : questionIds) {
                Optional<Question> questionOptional = questionRepository.findById(item);
                if (questionOptional.isPresent()) {
                    questions.add(questionOptional.get());
                }
            }

            // Kiểm tra và lấy Course
            Optional<Course> courseOptional = courseRepository.findById(targetCourseId);
            if (courseOptional.isEmpty()) {
                throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + targetCourseId);
            }

            for (Question question : questions) {
                Question newQuestion = new Question();
                newQuestion.setInstruction(question.getInstruction());
                newQuestion.setType(question.getType());
                newQuestion.setLevel(question.getLevel());
                newQuestion.setCourse(courseOptional.get());
                newQuestion.setAccount(question.getAccount());
                newQuestion.setCreatedAt(new Date());
                newQuestion.setUpdatedAt(new Date());
                if (question.getType().equals("fill-in-the-blank")) {
                    newQuestion.setContent(question.getContent());
                    newQuestion.setResult_check(question.getResult());
                    newQuestion.setResult(question.getResult());

                } else if (question.getType().equals("essay")) {
                    newQuestion.setContent(question.getContent());

                } else if (question.getType().equals("multiple-choice")) {
                    newQuestion.setContent(question.getContent());
                    newQuestion.setOptionA(question.getOptionA());
                    newQuestion.setOptionB(question.getOptionB());
                    newQuestion.setOptionC(question.getOptionC());
                    newQuestion.setOptionD(question.getOptionD());
                    newQuestion.setResult(question.getOptionD());
                    newQuestion.setResult_check(question.getResult_check());
                } else {
                    newQuestion.setContent(question.getContent());
                    newQuestion.setOptionA(question.getOptionA());
                    newQuestion.setOptionB(question.getOptionB());
                    newQuestion.setOptionC(question.getOptionC());
                    newQuestion.setOptionD(question.getOptionD());
                    newQuestion.setResult_check(question.getResult_check());
                    newQuestion.setResult(question.getResult());
                }
                questionRepository.save(newQuestion);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminQuestionDTORestoreList> getQuestions(Integer courseId, Integer accountId, String content, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = questionRepository.findQuestionRestoreByCourseIdAndAccountId(courseId, accountId, content, deletedDate, pageable);
        List<AdminQuestionDTORestoreList> adminQuestionDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminQuestionDTORestoreList dto = new AdminQuestionDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setContent((String) result[1]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[2]);
            dto.setCreatedAt(createAt);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);

            dto.setInstruction((String) result[4]);
            dto.setIsDeleted((Boolean) result[5]);

            dto.setOptionA((String) result[6]);
            dto.setOptionB((String) result[7]);
            dto.setOptionC((String) result[8]);
            dto.setOptionD((String) result[9]);

            dto.setResult((String) result[10]);
            dto.setResultCheck((String) result[11]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[12]);
            dto.setUpdatedAt(updateAt);

            dto.setLevel((String) result[13]);
            dto.setType((String) result[14]);

            dto.setAccountId((Integer) result[15]);
            dto.setCourseId((Integer) result[16]);

            adminQuestionDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminQuestionDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    @Transactional
    public Question updateRestoreQuestion(AdminQuestionDTORestoreList adminQuestionDTORestoreList) {
        Optional<Question> questionOptional = questionRepository.findById(adminQuestionDTORestoreList.getId());
        if (questionOptional.isEmpty()) {
            throw new RuntimeException("Question not found with id: " + adminQuestionDTORestoreList.getId());
        } else {
            Question question = questionOptional.get();
            question.setDeleted(false);
            return questionRepository.save(question);
        }
    }

    public void deleteRestoreQuestion(AdminQuestionDTORestoreList adminQuestionDTORestoreList) {
        Optional<Question> lessonOptional = questionRepository.findById(adminQuestionDTORestoreList.getId());
        if (lessonOptional.isEmpty()) {
            throw new RuntimeException("Question not found with id: " + adminQuestionDTORestoreList.getId());
        } else {
            questionRepository.delete(lessonOptional.get());
        }
    }

    public List<QuestionCountDTO> getQuestionsCountByLevel(Integer chapterID) {
        List<Object[]> results = questionRepository.getQuestionsCountByTypeAndLevel(chapterID);

        List<QuestionCountDTO> questionCountDTOs = new ArrayList<>();

        for (Object[] row : results) {
            String questionType = (String) row[0];  // question_type
            Long totalQuestions = getLongValue(row[1]);    // total_questions
            Long easyQuestions = getLongValue(row[2]);   // easy_questions
            Long mediumQuestions = getLongValue(row[3]);   // medium_questions
            Long hardQuestions = getLongValue(row[4]);  // hard_questions

            questionCountDTOs.add(new QuestionCountDTO(questionType, totalQuestions, easyQuestions, mediumQuestions, hardQuestions));
        }

        return questionCountDTOs;
    }

    public Question convertToEntity(AdminQuestionGetDTO_V2 dto) {
        Question question = new Question();

        // Ánh xạ dữ liệu từ DTO vào entity
        question.setId(dto.getId());
        question.setContent(dto.getContent());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setResult(dto.getResult());
        question.setInstruction(dto.getInstruction());
        question.setResult_check(dto.getResultCheck());
        question.setLevel(dto.getLevel());
        question.setType(dto.getType());
        question.setTopic(dto.getTopic());

        // Nếu bạn có logic để gán Account và Course
        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
            question.setAccount(account);
        }

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId()).orElse(null);
            question.setCourse(course);
        }

        return question;
    }

    public List<Test> createExams(AdminTestAddDTO_V2 examData, List<String> types, Integer chapterId, Integer estimateTest) throws Exception {

        List<Test> createdExams = new ArrayList<>();
        List<AdminQuestionGetDTO_V2> allQuestionsDTO = getQuestionsForExam(examData, types, chapterId);

        List<Question> allQuestions = new ArrayList<>();
        for (AdminQuestionGetDTO_V2 item : allQuestionsDTO) {
            Question question = convertToEntity(item);
            allQuestions.add(question);
        }


        if (allQuestions.size() < examData.getTotalQuestion()) {
            throw new Exception("Không đủ câu hỏi để tạo bài kiểm tra!");
        }

        // Tạo 5 bài kiểm tra
        for (int i = 0; i < estimateTest; i++) {
            Test exam = new Test();
            exam.setTitle("Bài thi " + (i + 1) + " - Chương " + chapterId);

            exam.setDescription(examData.getDescription());

            exam.setTotalQuestion(examData.getTotalQuestion());

            exam.setEasyQuestion(examData.getEasyQuestion());  // Easy = Level 1
            exam.setMediumQuestion(examData.getMediumQuestion()); // Medium = Level 2
            exam.setHardQuestion(examData.getHardQuestion());  // Hard = Level 3
            exam.setDuration(examData.getDuration());
            exam.setFormat(examData.getFormat());
            exam.setAssigned(true);
            String result = types.stream()
                    .map(type -> typeMapping.getOrDefault(type, type)) // Ánh xạ tên hoặc giữ nguyên nếu không tìm thấy
                    .collect(Collectors.joining(", "));
            exam.setType(result);
            exam.setCreatedAt(new Date());
            exam.setUpdatedAt(new Date());

            exam.setCourse(courseRepository.findById(examData.getCourseId()).get());
            exam.setChapter(chapterRepository.findById(examData.getChapterId()).get());

            exam = testRepository.save(exam); // Lưu bài kiểm tra mới

            // Lấy câu hỏi cho bài kiểm tra này (không trùng lặp câu hỏi)
            List<Question> selectedQuestions = distributeQuestionsForExam(allQuestions, examData);

            // Thêm câu hỏi vào bài kiểm tra
            for (Question question : selectedQuestions) {

                Test_Question examQuestion = new Test_Question();
                examQuestion.setTest(exam);
                examQuestion.setQuestion(question);
                testQuestionRepository.save(examQuestion);
            }

            createdExams.add(exam);
        }
        addedQuestions.clear();

        return createdExams;
    }


    public List<AdminQuestionGetDTO_V2> convertDTOQuestion(List<Object[]> items) {
        List<AdminQuestionGetDTO_V2> result = new ArrayList<>();

        for (Object[] item : items) {
            // Ánh xạ từng phần tử từ Object[] vào DTO
            AdminQuestionGetDTO_V2 dto = new AdminQuestionGetDTO_V2();
            dto.setId((Integer) item[0]);                // questionId
            dto.setContent((String) item[1]);             // content
            dto.setOptionA((String) item[2]);             // optionA
            dto.setOptionB((String) item[3]);             // optionB
            dto.setOptionC((String) item[4]);             // optionC
            dto.setOptionD((String) item[5]);             // optionD
            dto.setResult((String) item[6]);              // result
            dto.setInstruction((String) item[7]);         // instruction
            dto.setResultCheck((String) item[8]);         // resultCheck
            dto.setLevel((String) item[9]);               // level
            dto.setType((String) item[10]);               // type
            dto.setAccountId((Integer) item[11]);         // account_id
            dto.setCourseId((Integer) item[12]);          // course_id
            // Nếu cần ánh xạ thêm topic, bạn có thể thêm ở đây nếu query của bạn lấy topic
            // dto.setTopic((String) item[13]); // topic, nếu có
            result.add(dto);
        }

        return result;
    }


    //Lấy ra tất cả question thuộc dạng đó , kèm theo của chương nào
    private List<AdminQuestionGetDTO_V2> getQuestionsForExam(AdminTestAddDTO_V2 examData, List<String> types, Integer chapterID) {
//        List<Question> questions = new ArrayList<>();

        List<AdminQuestionGetDTO_V2> adminQuestionGetDTOV2s = new ArrayList<>();


        List<String> normalizedTypes = new ArrayList<>();
        for (String type : types) {
            String normalizedType = typeMapping.get(type);
            if (normalizedType != null) {
                normalizedTypes.add(normalizedType);
            }
        }

        // Lọc câu hỏi theo loại
        if (normalizedTypes.contains("essay")) {
            adminQuestionGetDTOV2s.addAll(convertDTOQuestion(questionRepository.findQuestionsByChapter(chapterID, "essay")));
        }
        if (normalizedTypes.contains("multiple-choice")) {
            adminQuestionGetDTOV2s.addAll(convertDTOQuestion(questionRepository.findQuestionsByChapter(chapterID, "multiple-choice")));
        }
        if (normalizedTypes.contains("fill-in-the-blank")) {
            adminQuestionGetDTOV2s.addAll(convertDTOQuestion(questionRepository.findQuestionsByChapter(chapterID, "fill-in-the-blank")));
        }
        if (normalizedTypes.contains("checkbox")) {
            adminQuestionGetDTOV2s.addAll(convertDTOQuestion(questionRepository.findQuestionsByChapter(chapterID, "checkbox")));
        }
        return adminQuestionGetDTOV2s;
    }

    Set<Integer> addedQuestions = new HashSet<>();

    private List<Question> distributeQuestionsForExam(List<Question> allQuestions, AdminTestAddDTO_V2 examData) {
        List<Question> selectedQuestions = new ArrayList<>();
        List<Question> selectedQuestionsReturn = new ArrayList<>();

        List<Question> easyQuestions = allQuestions.stream()
                .filter(q -> q.getLevel().equals("1") && !addedQuestions.contains(q.getId())) // Ensure not already added
                .limit(examData.getEasyQuestion())
                .collect(Collectors.toList());

        List<Question> mediumQuestions = allQuestions.stream()
                .filter(q -> q.getLevel().equals("2") && !addedQuestions.contains(q.getId())) // Ensure not already added
                .limit(examData.getMediumQuestion())
                .collect(Collectors.toList());

        List<Question> hardQuestions = allQuestions.stream()
                .filter(q -> q.getLevel().equals("3") && !addedQuestions.contains(q.getId())) // Ensure not already added
                .limit(examData.getHardQuestion())
                .collect(Collectors.toList());
        // Gộp tất cả câu hỏi vào một mảng để phân phối
        selectedQuestions.addAll(easyQuestions);
        selectedQuestions.addAll(mediumQuestions);
        selectedQuestions.addAll(hardQuestions);


        for (Question question : selectedQuestions) {
            if (!addedQuestions.contains(question.getId())) {
                selectedQuestionsReturn.add(question);
                addedQuestions.add(question.getId()); // Mark this question as added
            }
        }
        // Trộn các câu hỏi để đảm bảo không bị trùng lặp trong các bài kiểm tra
        Collections.shuffle(selectedQuestionsReturn);

        return selectedQuestionsReturn;
    }


    private Long getLongValue(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        } else if (value instanceof Long) {
            return (Long) value;
        }
        return 0L; // Default value if it's neither Long nor BigDecimal
    }

}