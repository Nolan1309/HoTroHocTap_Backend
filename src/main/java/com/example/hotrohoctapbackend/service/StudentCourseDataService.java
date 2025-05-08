package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDetailDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.StudentCourseDataDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.StudentCourseDataImportDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.StudentStatisticsDTO;
import com.example.hotrohoctapbackend.DTO.ProgressDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseCodeActivationRequestDTO;
import com.example.hotrohoctapbackend.DTO.User.StudentBehaviorRequestDTO;
import com.example.hotrohoctapbackend.DTO.User.StudentCourseProgressDTO;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentCourseDataService {

    @Autowired
    private StudentCourseDataRepository repository;


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PredictionResultRepository predictionRepo;

    @Autowired
    private AccountRepository accountRepository;

    //Save data lúc học viên kích hoạt khóa học
    public StudentCourseData saveStudentCourseDataActive(StudentBehaviorRequestDTO request, StudentCourseData studentCourseData, Integer accountId) {
        try {
            // Lấy thông tin từ DTO và gán vào đối tượng StudentCourseData
            studentCourseData.setAge(request.getBirthday()); // Cập nhật tuổi nếu có
            studentCourseData.setStudyHoursPerWeek(request.getStudyHoursPerWeek());
            studentCourseData.setTimeSpentOnSocialMedia(request.getTimeSpentOnSocialMedia());
            studentCourseData.setSleepHoursPerNight(request.getSleepHoursPerNight());

            // Chuyển đổi giới tính từ String sang Integer
            studentCourseData.setGender(Integer.parseInt(request.getGender()));

            studentCourseData.setPreferredLearningStyle(request.getPreferredLearningStyle());
            studentCourseData.setUseOfEducationalTech(request.isUseOfEducationalTech() ? 1 : 0);
            studentCourseData.setSelfReportedStressLevel(request.getSelfReportedStressLevel());

            studentCourseData.setAccount(accountRepository.findById(accountId).get());
            // Lưu dữ liệu vào cơ sở dữ liệu
            return repository.saveAndFlush(studentCourseData); // Trả về đối tượng đã lưu
        } catch (Exception e) {
            // Xử lý lỗi và ném ra ngoại lệ với thông báo lỗi chi tiết
            throw new RuntimeException("Error saving student data: " + e.getMessage(), e);
        }
    }


    public List<StudentCourseData> getAllStudent() {
        return repository.findAll();
    }

    // Lưu dữ liệu sinh viên và tiến trình học
    public String saveStudentCourseData(CourseCodeActivationRequestDTO request) {
        try {
            StudentCourseData studentData = new StudentCourseData();
            studentData.setStudentId(request.getStudentId());
            studentData.setAge(request.getAge());
            studentData.setStudyHoursPerWeek(request.getStudyHoursPerWeek());
            studentData.setTimeSpentOnSocialMedia(request.getTimeSpentOnSocialMedia());
            studentData.setSleepHoursPerNight(request.getSleepHoursPerNight());
            studentData.setGender(request.getGender());
            studentData.setPreferredLearningStyle(request.getPreferredLearningStyle());
            studentData.setUseOfEducationalTech(request.getUseOfEducationalTech());
            studentData.setSelfReportedStressLevel(request.getSelfReportedStressLevel());
            studentData.setCourseProgress(request.getCourseProgress());

            repository.save(studentData);
            return "Student data saved successfully.";
        } catch (Exception e) {
            return "Error saving student data: " + e.getMessage();
        }
    }

    // Lấy thông tin sinh viên theo `studentId`
    public StudentCourseData getStudentCourseDataByAccountIdAndStudentId(Integer accountId, String studentId) {
        return repository.findByAccount_IdAndStudentId(accountId, studentId).get();
    }

    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

    // Hiển thị danh sách sinh viên HUIT
    public Page<StudentCourseDataDTO> getAllStudentHuitByCourseId(Integer courseId, String classRoom, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentCourseData> studentCourseDataPage = repository.findByCourse_IdAndClassRoomContaining(courseId, classRoom, pageable);

        List<StudentCourseDataDTO> studentCourseDataDTOList = new ArrayList<>();

        for (StudentCourseData item : studentCourseDataPage.getContent()) {
            StudentCourseDataDTO studentCourseDataDTO = new StudentCourseDataDTO();
            studentCourseDataDTO.setId(item.getId());
            studentCourseDataDTO.setStudentId(item.getStudentId());
            studentCourseDataDTO.setEmail(item.getEmail());
            studentCourseDataDTO.setFullname(item.getFullname());
            studentCourseDataDTO.setClassRoom(item.getClassRoom());
            studentCourseDataDTO.setCourseId(item.getCourse().getId());

            Integer accountId = (item.getAccount() != null) ? item.getAccount().getId() : null;
            studentCourseDataDTO.setAccountId(accountId);

            studentCourseDataDTO.setAge(item.getAge());
            studentCourseDataDTO.setStudyHoursPerWeek(item.getStudyHoursPerWeek());
            studentCourseDataDTO.setOnlineCoursesCompleted(item.getOnlineCoursesCompleted());
            studentCourseDataDTO.setAssignmentCompletionRate(item.getAssignmentCompletionRate());
            studentCourseDataDTO.setExamScore(item.getExamScore());
            studentCourseDataDTO.setAttendanceRate(item.getAttendanceRate());
            studentCourseDataDTO.setTimeSpentOnSocialMedia(item.getTimeSpentOnSocialMedia());
            studentCourseDataDTO.setSleepHoursPerNight(item.getSleepHoursPerNight());
            studentCourseDataDTO.setGender(item.getGender());
            studentCourseDataDTO.setPreferredLearningStyle(item.getPreferredLearningStyle());
            studentCourseDataDTO.setParticipationInDiscussions(item.getParticipationInDiscussions());
            studentCourseDataDTO.setUseOfEducationalTech(item.getUseOfEducationalTech());
            studentCourseDataDTO.setSelfReportedStressLevel(item.getSelfReportedStressLevel());
            studentCourseDataDTO.setCourseProgress(item.getCourseProgress());

            studentCourseDataDTOList.add(studentCourseDataDTO);
        }

        // Create a Page of DTO
        return new PageImpl<>(studentCourseDataDTOList, pageable, studentCourseDataPage.getTotalElements());
    }

    // Hiển thị danh sách sinh viên HUIT đã và chưa dự đoán kết quả , dashboard HUIT
    public Page<StudentExportStudentHuitDTO> getAllStudentHuitByCourseIdAndResultPrediction(Integer courseId, int page, int size, String sortBy, String classRoom) {
        Pageable pageable;  // Khai báo biến pageable chỉ một lần

        if (sortBy.equals("nameDesc")) {
            pageable = PageRequest.of(page, size, Sort.by("fullname").descending());
        } else if (sortBy.equals("scoreHigh")) {
            pageable = PageRequest.of(page, size, Sort.by("examScore").descending());
        } else if (sortBy.equals("scoreLow")) {
            pageable = PageRequest.of(page, size, Sort.by("examScore").ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("fullname").ascending()); // Mặc định là sắp xếp tên theo thứ tự tăng dần
        }

        if (classRoom != null && !classRoom.isEmpty()) {
            // Lọc theo classRoom và chỉ lấy những sinh viên đã có dự đoán
            Page<StudentCourseData> studentCourseDataPage = repository.findStudentCourseDataByCourse_IdAndClassRoomAndAccount_Id(courseId, classRoom, pageable);
            List<StudentExportStudentHuitDTO> studentCourseDataDTOList = mapStudentCourseDataToDTO(courseId, studentCourseDataPage.getContent(), true); // true: chỉ lấy sinh viên đã dự đoán

            // Tính tổng số trang
            int totalPages = (int) Math.ceil((double) studentCourseDataPage.getTotalElements() / pageable.getPageSize());

            return new PageImpl<StudentExportStudentHuitDTO>(studentCourseDataDTOList, pageable, studentCourseDataPage.getTotalElements());

        } else {
            // Nếu không lọc theo lớp, chỉ lọc theo courseId và chỉ lấy những sinh viên đã có dự đoán
            Page<StudentCourseData> studentCourseDataPage = repository.findStudentCourseDataByCourse_IdAndAccount_Id(courseId, pageable);
            List<StudentExportStudentHuitDTO> studentCourseDataDTOList = mapStudentCourseDataToDTO(courseId, studentCourseDataPage.getContent(), true); // true: chỉ lấy sinh viên đã dự đoán

            // Tính tổng số trang
            int totalPages = (int) Math.ceil((double) studentCourseDataPage.getTotalElements() / pageable.getPageSize());

            return new PageImpl<StudentExportStudentHuitDTO>(studentCourseDataDTOList, pageable, studentCourseDataPage.getTotalElements());

        }

    }

    //Mapping List ENtity sang ExportStudent
    private List<StudentExportStudentHuitDTO> mapStudentCourseDataToDTO(Integer courseId, List<StudentCourseData> studentCourseDataList, boolean filterPredicted) {
        List<StudentExportStudentHuitDTO> studentCourseDataDTOList = new ArrayList<>();
        for (StudentCourseData item : studentCourseDataList) {
            // Kiểm tra xem sinh viên có dự đoán hay không
            Optional<PredictionResult> predictionOpt = predictionRepo.findTopByStudentId(item.getStudentId());
            if (!predictionOpt.isPresent()) {
                // Nếu sinh viên không có dự đoán thì bỏ qua nếu filterPredicted == true
                if (filterPredicted) {
                    continue; // Bỏ qua sinh viên không có dự đoán
                }
            }

            StudentExportStudentHuitDTO studentExportStudentHuitDTO = new StudentExportStudentHuitDTO();
            studentExportStudentHuitDTO.setStudentId(item.getStudentId());
            Integer accountIdInteger = (item.getAccount() != null) ? item.getAccount().getId() : null;
            String accountId = String.valueOf(accountIdInteger);
            studentExportStudentHuitDTO.setAccountId(accountId);
            studentExportStudentHuitDTO.setFullname(item.getFullname());
            studentExportStudentHuitDTO.setClassRoom(item.getClassRoom());
            studentExportStudentHuitDTO.setAge(item.getAge());
            studentExportStudentHuitDTO.setGender(Integer.toString(item.getGender()));
            studentExportStudentHuitDTO.setAssignmentCompletionRate(item.getAssignmentCompletionRate());

            // Chỉ lấy dữ liệu từ PredictionResult nếu có
            predictionOpt.ifPresent(prediction -> {
                studentExportStudentHuitDTO.setPrediction(Integer.toString(prediction.getPrediction()));
                if (accountIdInteger != null) {
                    Long accountIdLong = accountIdInteger.longValue();
                    Long courseIdLong = courseId.longValue();
                    Double averageScore = testResultService.getAverageScoreUser(accountIdLong, courseIdLong);
                    if (averageScore == null) {
                        averageScore = 0.0; // Gán giá trị mặc định nếu averageScore là null
                    }
                    studentExportStudentHuitDTO.setExamScore(averageScore);
                }
                studentExportStudentHuitDTO.setProbability(prediction.getProbability());
            });
            studentCourseDataDTOList.add(studentExportStudentHuitDTO);
        }
        return studentCourseDataDTOList;
    }


    public List<StudentExportStudentHuitDTO> getAllStudentHuitByCourseIdAndResultPredictionList(Integer courseId, String classRoom) {
        if (classRoom != null && !classRoom.isEmpty()) {
            List<StudentCourseData> studentCourseDataPage = repository.findStudentCourseDataByCourse_IdAndClassRoom(courseId, classRoom);
            List<StudentExportStudentHuitDTO> studentCourseDataDTOList = mapStudentCourseDataToDTO(courseId, studentCourseDataPage, true);
            return studentCourseDataDTOList;
        } else {
            List<StudentCourseData> studentCourseDataPage = repository.findStudentCourseDataByCourse_Id(courseId);
            List<StudentExportStudentHuitDTO> studentCourseDataDTOList = mapStudentCourseDataToDTO(courseId, studentCourseDataPage, true);
            return studentCourseDataDTOList;
        }
    }

    public Page<StudentExportStudentHuitDetailDTO> getAllStudentHuitDetailByCourseIdAndResultPrediction(
            Integer courseId, Integer accountId, String studentId, int page, int size) {

        List<StudentCourseProgressDTO> progressList = getAllProgressPredict(studentId, accountId, courseId);
        List<StudentExportStudentHuitDetailDTO> detailDTOList = new ArrayList<>();
//        StudentCourseData studentCourseData = repository.findByAccount_IdAndStudentId(accountId, studentId).get();
        StudentCourseData studentCourseData = repository.findByStudentId(studentId).get();

        for (StudentCourseProgressDTO dto : progressList) {
            if (dto.getCourse_progress() == null) continue;

            for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI> chapterEntry : dto.getCourse_progress().entrySet()) {
                String chapterKey = chapterEntry.getKey();
                StudentCourseProgressDTO.CourseProgressDTOAPI chapter = chapterEntry.getValue();

                String chapterTitle = chapter.getChapter_title();
                String chapterQuiz = chapter.getChapter_quiz() != null ? chapter.getChapter_quiz().toString() : "Chưa làm";

                if (chapter.getLessons() == null) continue;

                for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessonEntry : chapter.getLessons().entrySet()) {
                    String lessonTitle = lessonEntry.getKey();
                    StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI lesson = lessonEntry.getValue();

                    List<StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI> attempts = lesson.getAttempts();

                    int countTime = (attempts != null) ? attempts.size() : 0;

                    String lessonQuiz = "Chưa làm";
                    String createDate = "Chưa làm";


                    if (attempts != null && !attempts.isEmpty()) {
                        // 👉 Tính trung bình điểm
                        double totalScore = attempts.stream()
                                .filter(a -> a.getScore() != null)
                                .mapToDouble(StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI::getScore)
                                .sum();

                        long validAttempts = attempts.stream()
                                .filter(a -> a.getScore() != null)
                                .count();

                        if (validAttempts > 0) {
                            double avgScore = totalScore / validAttempts;
                            lessonQuiz = String.format("%.2f", avgScore); // giữ 2 chữ số thập phân
                        } else {
                            lessonQuiz = "Chưa có điểm";
                        }

                        // 👉 Lấy timestamp của lần làm cuối cùng (nếu có)
                        createDate = attempts.get(attempts.size() - 1).getTimestamp() != null
                                ? attempts.get(attempts.size() - 1).getTimestamp()
                                : "Chưa có thời gian";
                    }


                    StudentExportStudentHuitDetailDTO detail = new StudentExportStudentHuitDetailDTO();
                    detail.setStudentId(dto.getStudent_id());
                    detail.setFullname(studentCourseData.getFullname()); // Gán nếu bạn chưa truyền được fullname
                    detail.setChapterTitle(chapterTitle);
                    detail.setChapterQuiz(chapterQuiz);
                    detail.setLessonTitle(lessonTitle);
                    detail.setCountTime(countTime);
                    detail.setLessonQuiz(lessonQuiz);
                    detail.setCreateDate(createDate);

                    detailDTOList.add(detail);
                }
            }
        }

        // Phân trang thủ công
        int start = (int) Math.min((long) page * size, detailDTOList.size());
        int end = (int) Math.min(start + size, detailDTOList.size());
        List<StudentExportStudentHuitDetailDTO> pagedList = detailDTOList.subList(start, end);

        return new PageImpl<>(pagedList, PageRequest.of(page, size), detailDTOList.size());
    }

    public Optional<StudentCourseData> getStudentByAccountIdAndCourseId(Integer accountId, Integer courseId) {
        return repository.findByAccountIdAndCourseId(accountId, courseId);
    }


    //    Đoạn export Danh sách cho giáo viên
//    Sheet 1
    public List<StudentExportStudentHuitDTO> getAllStudentHuitByCourseIdAndResultPredictionListByListClass(Integer courseId, List<String> classRooms) {

        List<StudentCourseData> studentCourseDataPage = repository.findStudentCourseDataByCourse_IdAccNull(courseId);

        List<StudentExportStudentHuitDTO> studentCourseDataDTOList = new ArrayList<>();

        for (StudentCourseData item : studentCourseDataPage) {
            StudentExportStudentHuitDTO studentExportStudentHuitDTO = new StudentExportStudentHuitDTO();

            studentExportStudentHuitDTO.setStudentId(item.getStudentId());

            Integer accountIdInteger = (item.getAccount() != null) ? item.getAccount().getId() : null;
            String accountId = String.valueOf(accountIdInteger);
            studentExportStudentHuitDTO.setAccountId(accountId);

            studentExportStudentHuitDTO.setFullname(item.getFullname());
            studentExportStudentHuitDTO.setClassRoom(item.getClassRoom());
            studentExportStudentHuitDTO.setAge(item.getAge());
            studentExportStudentHuitDTO.setGender(Integer.toString(item.getGender()));
            studentExportStudentHuitDTO.setAssignmentCompletionRate(item.getAssignmentCompletionRate());

            predictionRepo.findTopByStudentId(item.getStudentId()).ifPresent(prediction -> {
                studentExportStudentHuitDTO.setPrediction(Integer.toString(prediction.getPrediction()));

                // ✅ Chỉ parse nếu accountId khác null và không phải "null"
                if (accountIdInteger != null) {
                    Long accountIdLong = accountIdInteger.longValue();
                    Long courseIdLong = courseId.longValue();
                    Double averageScore = testResultService.getAverageScoreUser(accountIdLong, courseIdLong);
                    if (averageScore == null) {
                        averageScore = 0.0;
                    }
                    studentExportStudentHuitDTO.setExamScore(averageScore);
                }

                studentExportStudentHuitDTO.setProbability(prediction.getProbability());
            });
//            studentExportStudentHuitDTO.setChaptersCompleted(2);
//            studentExportStudentHuitDTO.setChaptersLearned(4);
            studentCourseDataDTOList.add(studentExportStudentHuitDTO);
        }

        studentCourseDataDTOList = studentCourseDataDTOList.stream()
                .filter(student -> classRooms.contains(student.getClassRoom())) // Lọc theo lớp học
                .collect(Collectors.toList());


        // Create a Page of DTO
        return studentCourseDataDTOList;
    }

    //    Sheet 2
    public List<StudentExportStudentHuitDetailDTO> getAllStudentHuitDetailByCourseIdAndResultPredictionListByClassRooms(
            Integer courseId, Integer accountId, String studentId) {

        List<StudentCourseProgressDTO> progressList = getAllProgressPredict(studentId, accountId, courseId);
        List<StudentExportStudentHuitDetailDTO> detailDTOList = new ArrayList<>();
//        StudentCourseData studentCourseData = repository.findByAccount_IdAndStudentId(accountId, studentId).get();
        StudentCourseData studentCourseData = repository.findByStudentId(studentId).get();

        for (StudentCourseProgressDTO dto : progressList) {
            if (dto.getCourse_progress() == null) continue;

            for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI> chapterEntry : dto.getCourse_progress().entrySet()) {
                String chapterKey = chapterEntry.getKey();
                StudentCourseProgressDTO.CourseProgressDTOAPI chapter = chapterEntry.getValue();

                String chapterTitle = chapter.getChapter_title();
                String chapterQuiz = chapter.getChapter_quiz() != null ? chapter.getChapter_quiz().toString() : "Chưa làm";

                if (chapter.getLessons() == null) continue;

                for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessonEntry : chapter.getLessons().entrySet()) {
                    String lessonTitle = lessonEntry.getKey();
                    StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI lesson = lessonEntry.getValue();

                    List<StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI> attempts = lesson.getAttempts();

                    int countTime = (attempts != null) ? attempts.size() : 0;

                    String lessonQuiz = "Chưa làm";
                    String createDate = "Chưa làm";


                    if (attempts != null && !attempts.isEmpty()) {
                        // 👉 Tính trung bình điểm
                        double totalScore = attempts.stream()
                                .filter(a -> a.getScore() != null)
                                .mapToDouble(StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI::getScore)
                                .sum();

                        long validAttempts = attempts.stream()
                                .filter(a -> a.getScore() != null)
                                .count();

                        if (validAttempts > 0) {
                            double avgScore = totalScore / validAttempts;
                            lessonQuiz = String.format("%.2f", avgScore); // giữ 2 chữ số thập phân
                        } else {
                            lessonQuiz = "Chưa có điểm";
                        }

                        // 👉 Lấy timestamp của lần làm cuối cùng (nếu có)
                        createDate = attempts.get(attempts.size() - 1).getTimestamp() != null
                                ? attempts.get(attempts.size() - 1).getTimestamp()
                                : "Chưa có thời gian";
                    }


                    StudentExportStudentHuitDetailDTO detail = new StudentExportStudentHuitDetailDTO();
                    detail.setStudentId(dto.getStudent_id());
                    detail.setFullname(studentCourseData.getFullname()); // Gán nếu bạn chưa truyền được fullname
                    detail.setChapterTitle(chapterTitle);
                    detail.setChapterQuiz(chapterQuiz);
                    detail.setLessonTitle(lessonTitle);
                    detail.setCountTime(countTime);
                    detail.setLessonQuiz(lessonQuiz);
                    detail.setCreateDate(createDate);

                    detailDTOList.add(detail);
                }
            }
        }

        return detailDTOList;
    }


    // Cập nhật dữ liệu sinh viên và tiến trình học
    public String updateStudentCourseData(String studentId, CourseCodeActivationRequestDTO request) {
        Optional<StudentCourseData> studentDataOptional = repository.findById(studentId);

        if (studentDataOptional.isPresent()) {
            StudentCourseData studentData = studentDataOptional.get();
            studentData.setAge(request.getAge());
            studentData.setStudyHoursPerWeek(request.getStudyHoursPerWeek());
            studentData.setTimeSpentOnSocialMedia(request.getTimeSpentOnSocialMedia());
            studentData.setSleepHoursPerNight(request.getSleepHoursPerNight());
            studentData.setGender(request.getGender());
            studentData.setPreferredLearningStyle(request.getPreferredLearningStyle());
            studentData.setUseOfEducationalTech(request.getUseOfEducationalTech());
            studentData.setSelfReportedStressLevel(request.getSelfReportedStressLevel());
            studentData.setCourseProgress(request.getCourseProgress());

            repository.save(studentData);
            return "Student data updated successfully.";
        } else {
            return "Student not found.";
        }
    }

    public List<StudentCourseDataImportDTO> readExcelFile(MultipartFile file) throws IOException {
        List<StudentCourseDataImportDTO> students = new ArrayList<>();
        InputStream inputStream = file.getInputStream();

        // Create Workbook instance
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Iterate over rows
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip the header row
        if (rowIterator.hasNext()) {
            rowIterator.next(); // Skip header row
        }

        // Process the rows
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            StudentCourseDataImportDTO student = new StudentCourseDataImportDTO();


            student.setStudentId(getCellValue(row.getCell(0)));
            student.setName(getCellValue(row.getCell(1)));
            student.setClassName(getCellValue(row.getCell(2)));
            student.setEmail(getCellValue(row.getCell(3)));

            students.add(student);
        }

        workbook.close();
        return students;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // If the cell contains a number, ensure it's returned as a string (to avoid scientific notation)
                double numericValue = cell.getNumericCellValue();
                if (isNumericAsString(numericValue)) {
                    return String.format("%.0f", numericValue); // This handles large numeric values as strings
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Helper method to check if the numeric value is likely to be an ID (i.e., large enough to need special handling)
    private boolean isNumericAsString(double value) {
        return value > 1000000000L; // You can adjust this threshold based on your use case
    }


    public Boolean saveImport(MultipartFile file, Integer courseId) {
        try {
            // Read the Excel file and map it to DTOs
            List<StudentCourseDataImportDTO> studentCourseDataImportDTOList = readExcelFile(file);

            for (StudentCourseDataImportDTO item : studentCourseDataImportDTOList) {
                // Create a new StudentCourseData entity
                StudentCourseData studentCourseData = new StudentCourseData();

                // Set course information
                studentCourseData.setCourse(courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học")));

                // Set other student information from the DTO
                studentCourseData.setEmail(item.getEmail());
                studentCourseData.setClassRoom(item.getClassName());
                studentCourseData.setFullname(item.getName());
                studentCourseData.setStudentId(item.getStudentId());

                // Save the student course data to the repository
                repository.save(studentCourseData);
            }
            return true;
        } catch (IOException e) {
            // Handle file read error
            System.err.println("Error reading the Excel file: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            // Handle course not found or other runtime errors
            System.err.println("Error processing the import: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;
    @Autowired
    private Enrolled_CoursesRepository enrolled_coursesRepository;

    @Autowired
    @Lazy
    private ProgressService progressService;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TestResultRepository testResultRepository;


    public StudentCourseProgressDTO calculateStudentProgress(String studentCode, Integer accountId, Integer courseId) {
        StudentCourseData studentCourseData = repository
                .findByAccountIdAndCourseId(accountId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        StudentCourseProgressDTO dto = new StudentCourseProgressDTO();

        dto.setStudent_id(studentCourseData.getStudentId());
        dto.setAge(studentCourseData.getAge());

        dto.setStudy_Hours_per_Week(studentCourseData.getStudyHoursPerWeek());
        dto.setTime_Spent_on_Social_Media(studentCourseData.getTimeSpentOnSocialMedia());
        dto.setSleep_Hours_per_Night(studentCourseData.getSleepHoursPerNight());
        dto.setGender(studentCourseData.getGender());
        dto.setPreferred_Learning_Style(studentCourseData.getPreferredLearningStyle());
        dto.setUse_of_Educational_Tech(studentCourseData.getUseOfEducationalTech());
        dto.setSelf_Reported_Stress_Level(studentCourseData.getSelfReportedStressLevel());


        // cần tính
        Boolean Participated = hasParticipatedInDiscussions(accountId);
        Integer online = getOnlineCoursesCompleted(accountId);
        Double assign = getAssignmentCompletionRate(accountId, courseId);
        Long accountIdLong = accountId.longValue();
        Long courseIdLong = courseId.longValue();
        Double score = getAverageScoreUser(accountIdLong, courseIdLong);
        Double atten = getAttendanceRate(accountId, courseId);
        score = (double) Math.round(score);
        atten = (double) Math.round(atten);

        dto.setParticipation_in_Discussions(Participated ? 1 : 0);
        dto.setOnline_Courses_Completed(online);

        dto.setAssignment_Completion_Rate(assign);

        dto.setExam_Score(score);
        dto.setAttendance_Rate(atten);


        return dto;
    }

    //Lay ra thong tin cbi du doan 1 sinh vien
    public StudentCourseProgressDTO mapToProgressDTO(StudentCourseDataDTO studentCourseDataDTO) {
        StudentCourseProgressDTO studentCourseProgressDTO = new StudentCourseProgressDTO();
        studentCourseProgressDTO.setStudent_id(studentCourseDataDTO.getStudentId());
        studentCourseProgressDTO.setAge(studentCourseDataDTO.getAge());
        studentCourseProgressDTO.setStudy_Hours_per_Week(studentCourseDataDTO.getStudyHoursPerWeek());
        studentCourseProgressDTO.setOnline_Courses_Completed(studentCourseDataDTO.getOnlineCoursesCompleted());
        studentCourseProgressDTO.setAssignment_Completion_Rate(studentCourseDataDTO.getAssignmentCompletionRate());
        studentCourseProgressDTO.setExam_Score(studentCourseDataDTO.getExamScore());
        studentCourseProgressDTO.setAttendance_Rate(studentCourseDataDTO.getAttendanceRate());
        studentCourseProgressDTO.setTime_Spent_on_Social_Media(studentCourseDataDTO.getTimeSpentOnSocialMedia());
        studentCourseProgressDTO.setSleep_Hours_per_Night(studentCourseDataDTO.getSleepHoursPerNight());
        studentCourseProgressDTO.setGender(studentCourseDataDTO.getGender());
        studentCourseProgressDTO.setPreferred_Learning_Style(studentCourseDataDTO.getPreferredLearningStyle());
        studentCourseProgressDTO.setParticipation_in_Discussions(studentCourseDataDTO.getParticipationInDiscussions());
        studentCourseProgressDTO.setUse_of_Educational_Tech(studentCourseDataDTO.getUseOfEducationalTech());
        studentCourseProgressDTO.setSelf_Reported_Stress_Level(studentCourseDataDTO.getSelfReportedStressLevel());
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> courseProgress = parseCourseProgress(studentCourseDataDTO.getCourseProgress());

        studentCourseProgressDTO.setCourse_progress(courseProgress);

        return studentCourseProgressDTO;
    }

    public static Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> parseCourseProgress(String jsonInput) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Chuyển chuỗi JSON thành Map<String, CourseProgressDTOAPI>
            return objectMapper.readValue(
                    jsonInput,
                    new TypeReference<Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI>>() {
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public StudentCourseProgressDTO getCourseProgressFromStudent(String studentCode, Integer accountId, Integer courseId) {
        // Tìm kiếm dữ liệu sinh viên từ repository
        StudentCourseData studentCourseData = repository
                .findByAccountIdAndCourseId(accountId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        // Mapping entity sang DTO
        StudentCourseDataDTO studentCourseDataDTO = mapToDTO(studentCourseData);
        StudentCourseProgressDTO item = mapToProgressDTO(studentCourseDataDTO);
        return item;
    }

    // Phương thức helper để chuyển đổi Entity sang DTO
    private StudentCourseDataDTO mapToDTO(StudentCourseData studentCourseData) {
        StudentCourseDataDTO dto = new StudentCourseDataDTO();
        dto.setId(studentCourseData.getId());
        dto.setAccountId(studentCourseData.getAccount().getId());
        dto.setStudentId(studentCourseData.getStudentId());
        dto.setEmail(studentCourseData.getEmail());
        dto.setFullname(studentCourseData.getFullname());
        dto.setClassRoom(studentCourseData.getClassRoom());
        dto.setCourseId(studentCourseData.getCourse().getId());
        dto.setAge(studentCourseData.getAge());
        dto.setStudyHoursPerWeek(studentCourseData.getStudyHoursPerWeek());
        dto.setOnlineCoursesCompleted(studentCourseData.getOnlineCoursesCompleted());
        dto.setAssignmentCompletionRate(studentCourseData.getAssignmentCompletionRate());
        dto.setExamScore(studentCourseData.getExamScore());
        dto.setAttendanceRate(studentCourseData.getAttendanceRate());
        dto.setTimeSpentOnSocialMedia(studentCourseData.getTimeSpentOnSocialMedia());
        dto.setSleepHoursPerNight(studentCourseData.getSleepHoursPerNight());
        dto.setGender(studentCourseData.getGender());
        dto.setPreferredLearningStyle(studentCourseData.getPreferredLearningStyle());
        dto.setParticipationInDiscussions(studentCourseData.getParticipationInDiscussions());
        dto.setUseOfEducationalTech(studentCourseData.getUseOfEducationalTech());
        dto.setSelfReportedStressLevel(studentCourseData.getSelfReportedStressLevel());
        dto.setCourseProgress(studentCourseData.getCourseProgress());
        return dto;
    }


    public Boolean saveProgressDataHUIT(String email, String studentCode, Integer accountId, Integer courseId) throws JsonProcessingException {

        if (accountId == null) {
            return false;
        }

        StudentCourseData studentCourseData = repository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        // cần tính
        Boolean Participated = hasParticipatedInDiscussions(accountId);
        Integer online = getOnlineCoursesCompleted(accountId);

        Double assign = getAssignmentCompletionRate(accountId, courseId);

        Long accountIdLong = accountId.longValue();
        Long courseIdLong = courseId.longValue();
        Double score = getAverageScoreUser(accountIdLong, courseIdLong);
        Double atten = getAttendanceRate(accountId, courseId);
        score = (double) Math.round(score);
        atten = (double) Math.round(atten);

        studentCourseData.setParticipationInDiscussions(Participated ? 1 : 0);
        studentCourseData.setOnlineCoursesCompleted(online);
        studentCourseData.setAssignmentCompletionRate(assign);
        studentCourseData.setExamScore(score);
        studentCourseData.setAttendanceRate(atten);

//        List<StudentCourseProgressDTO> result = getStudentProgress(studentCode, accountId, courseId);
        List<StudentCourseProgressDTO> result = getAllProgressPredict(studentCode, accountId, courseId);


        Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> courseProgress = result.get(0).getCourse_progress();
        ObjectMapper objectMapper = new ObjectMapper();
        String courseProgressJson = objectMapper.writeValueAsString(courseProgress);
        studentCourseData.setCourseProgress(courseProgressJson);

        StudentCourseData update = repository.saveAndFlush(studentCourseData);
        if (update != null) {
            return true;
        }
        return false;
    }

    private boolean hasParticipatedInDiscussions(Integer accountId) {
        List<Comment> comments = commentRepository.findCommentsByAccount_Id(accountId);
        return !comments.isEmpty();
    }

    private int getOnlineCoursesCompleted(Integer accountId) {
        List<Enrolled_Courses> completedCourses = enrolled_coursesRepository.findByAccountIdAndStatus(accountId, "Completed");
        return completedCourses.isEmpty() ? 0 : completedCourses.size();
    }

    public Double getAssignmentCompletionRate(Integer accountId, Integer courseId) {
        Double progress = progressService.calculateProgress(accountId, courseId);
        if (progress == null || progress.isNaN()) {
            return 0.0;  // Không có tỷ lệ thì trả về 0%
        }
        return progress;
    }

    private Double getAverageScoreUser(Long accountId, Long courseId) {
        Double averageScore = testResultService.getAverageScoreUser(accountId, courseId);
        if (averageScore != null) {
            // Giả sử thang điểm từ 0 đến 10
            return averageScore * 10;  // Chuyển điểm trung bình sang phần trăm
        } else {
            return 0.0;  // Nếu không có điểm, trả về 0%
        }
    }

    private Double getAttendanceRate(Integer accountId, Integer courseId) {
        long loginCount = activityHistoryRepository.countLoginCountByAccountId(accountId);
        long videoWatchedCount = activityHistoryRepository.countVideoWatchedCountByAccountId(accountId);
        long assignmentSubmittedCount = activityHistoryRepository.countAssignmentSubmittedCountByAccountId(accountId);
        int requiredLogins = 8;

        int totalVideos = getTotalVideos(courseId);
        int totalAssignments = getTotalAssignments(courseId);

        double attendanceRate = (loginCount + videoWatchedCount + assignmentSubmittedCount) /
                (double) (requiredLogins + totalVideos + totalAssignments) * 100;

        return attendanceRate;
    }

    public int getTotalVideos(Integer courseId) {
        List<Video> videos = videoRepository.findByLessonCourseIdAndIsDeletedFalse(courseId);
        return videos.size();
    }

    public int getTotalAssignments(Integer courseId) {
        List<Test> tests = testRepository.findByCourseIdAndFormatAndIsDeletedFalse(courseId, "test");
        return tests.size();
    }


    public List<StudentCourseProgressDTO> getAllProgressPredict(String studentId, Integer accountId, Integer courseId) {
        List<StudentCourseProgressDTO> studentCourseProgressDTOListAll = new ArrayList<>();

        // Lấy tiến trình học viên
        List<StudentCourseProgressDTO> studentCourseProgressDTOS = getStudentProgress(studentId, accountId, courseId);

        // Lấy tổng quan khóa học
        List<StudentCourseProgressDTO> courseProgressDTOS = getProgressCourse(courseId, studentId);

        // Duyệt qua các tổng quan khóa học
        for (StudentCourseProgressDTO courseProgressDTO : courseProgressDTOS) {
            Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> courseProgressMap = courseProgressDTO.getCourse_progress();

            // Duyệt qua các chương trong tổng quan khóa học
            for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI> entry : courseProgressMap.entrySet()) {
                StudentCourseProgressDTO.CourseProgressDTOAPI chapterProgress = entry.getValue();
                String titleChapter = entry.getKey();
//                Boolean chapterQuiz = false;
//                double totalChapterScore = 0;
//                int lessonCount = 0;

                // Duyệt qua các bài học trong chương
                for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessonEntry : chapterProgress.getLessons().entrySet()) {
                    StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI lessonProgress = lessonEntry.getValue();
                    Integer lessonTestId = null;

                    // Kiểm tra nếu bài học có bài kiểm tra
                    if (lessonProgress.getAttempts() != null && !lessonProgress.getAttempts().isEmpty()) {
                        lessonTestId = lessonProgress.getAttempts().get(0).getTestId();
                    }

                    boolean hasAttempts = false;
                    List<StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI> studentAttempts = new ArrayList<>();
                    double lessonScore = 0;

                    if (lessonProgress.getAttempts() == null || lessonProgress.getAttempts().isEmpty()) {
                        studentAttempts.add(new StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI(
                                null,  // videoId
                                null,  // testId
                                null,  // score
                                null   // timestamp
                        ));
                    }
                    // Duyệt qua tiến trình học viên
                    for (StudentCourseProgressDTO studentProgress : studentCourseProgressDTOS) {
                        Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> studentCourseProgressMap = studentProgress.getCourse_progress();

                        for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI> studentChapterEntry : studentCourseProgressMap.entrySet()) {
                            StudentCourseProgressDTO.CourseProgressDTOAPI studentChapterProgress = studentChapterEntry.getValue();

                            // Chỉ xử lý chương học có tên trùng
                            if (studentChapterEntry.getKey().equals(titleChapter)) {
                                for (Map.Entry<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> studentLessonEntry : studentChapterProgress.getLessons().entrySet()) {
                                    StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI studentLessonProgress = studentLessonEntry.getValue();

                                    // Duyệt qua tất cả các attempts của bài học trong tiến trình học viên
                                    for (StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI attempt : studentLessonProgress.getAttempts()) {
                                        // So sánh testId giữa tiến trình học viên và tổng quan khóa học
                                        if (attempt.getTestId() != null && attempt.getTestId().equals(lessonTestId)) {
                                            hasAttempts = true;
                                            studentAttempts = studentLessonProgress.getAttempts();
                                            lessonScore = studentLessonProgress.getAttempts().stream()
                                                    .mapToDouble(StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI::getScore)
                                                    .average().orElse(0); // Tính điểm trung bình nếu có nhiều lần thử
                                            break;  // Thoát khỏi vòng lặp nếu đã tìm thấy lần thử tương ứng
                                        }
                                    }
                                }


                                // Cập nhật thông tin chương từ tiến trình học viên
                                chapterProgress.setChapter_title(studentChapterProgress.getChapter_title());
                                chapterProgress.setChapter_quiz(studentChapterProgress.getChapter_quiz());
                                chapterProgress.setCompleted(studentChapterProgress.getCompleted());
                            }
                        }
                    }

                    if (hasAttempts) {
                        lessonProgress.setAttempts(studentAttempts);  // Lưu thông tin attempts vào bài học trong tổng quan khóa học
                    } else if (lessonProgress.getAttempts() == null || lessonProgress.getAttempts().isEmpty()) {
                        lessonProgress.setAttempts(studentAttempts);  // Đảm bảo rằng cấu trúc mặc định được thêm vào
                    }
//
//                    // Cộng điểm của bài học vào tổng điểm chương
//                    totalChapterScore += lessonScore;
//                    lessonCount++;
                }

//                // Tính điểm chương (average score of lessons in the chapter)
//                if (lessonCount > 0) {
//                    chapterQuiz = true;  // Đánh dấu chương có bài kiểm tra
//                    double chapterAverageScore = totalChapterScore / lessonCount; // Tính điểm trung bình của chương
//                    chapterProgress.setChapter_quiz(chapterAverageScore); // Lưu điểm chương vào chapter_quiz
//                }
            }

            // Thêm thông tin vào danh sách kết quả
            studentCourseProgressDTOListAll.add(courseProgressDTO);
        }

        return studentCourseProgressDTOListAll;
    }


    public List<StudentCourseProgressDTO> getStudentProgress(String studentId, Integer accountId, Integer courseId) {
        // Khởi tạo DTO
        StudentCourseProgressDTO studentCourseProgressDTO = new StudentCourseProgressDTO();
        studentCourseProgressDTO.setStudent_id(studentId);

        // Lấy danh sách tiến trình học
        List<Object[]> progressList = progressRepository.findByAccountIdAndCourseId(accountId, courseId);

        // Mỗi chương trong tiến trình học
        Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> courseProgressMap = new LinkedHashMap<>();

        // Duyệt qua tất cả các bản ghi và ánh xạ vào DTO
        for (Object[] row : progressList) {
            // Tạo DTO cho tiến trình
            ProgressDTO dto = new ProgressDTO();
            dto.setId((Integer) row[0]);
            dto.setIsChapterTest((Boolean) row[1]);
            dto.setCompletedAt(convertToLocalDateTime(row[2]));
            dto.setTestCompleted((Boolean) row[3]);
            dto.setTestScore((Double) row[4]);
            dto.setVideoCompleted((Boolean) row[5]);
            dto.setAccountId((Integer) row[6]);
            dto.setChapterId((Integer) row[7]);
            dto.setCourseId((Integer) row[8]);
            dto.setLessonId((Integer) row[9]);

            // Tạo course progress nếu chưa có
            String chapterKey = "Ch" + dto.getChapterId();
            StudentCourseProgressDTO.CourseProgressDTOAPI courseProgress = courseProgressMap.get(chapterKey);

            if (courseProgress == null) {
                // Tạo mới course progress nếu chưa có
                courseProgress = new StudentCourseProgressDTO.CourseProgressDTOAPI();
                courseProgress.setChapter_title("Chương " + dto.getChapterId());  // Lấy tên chương từ ID
                courseProgressMap.put(chapterKey, courseProgress);
            }

            // Tạo lessons map nếu chưa có
            Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessons = courseProgress.getLessons();
            if (lessons == null) {
                lessons = new HashMap<>();
            }

            String lessonKey = "L" + dto.getLessonId();
            List<StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI> attempts = new ArrayList<>();

            if (dto.getIsChapterTest() && dto.getLessonId() == null) {
                courseProgress.setChapter_quiz(dto.getTestScore());
            }
            // Truy vấn lấy điểm số từ bảng test_results và tiến độ học
            List<Object[]> testResults = testResultRepository.findTestResultsByLessonIdAndAccountId(dto.getLessonId(), courseId, accountId);
            Video video = videoRepository.findVideoByLessonId(dto.getLessonId());
            // Duyệt qua các kết quả bài kiểm tra và lưu vào attempts
            for (Object[] testRow : testResults) {
                Integer testId = (Integer) testRow[12];
                Double score = (Double) testRow[8];
                Timestamp timestampObj = (Timestamp) testRow[2];
                String timestamp = null;
                if (timestampObj != null) {
                    timestamp = timestampObj.toString();
                }
                // Tạo attempt cho mỗi bài kiểm tra
                StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI attempt = new StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI();
                attempt.setTestId(testId);
                attempt.setScore(score);
                attempt.setTimestamp(timestamp);
                attempt.setVideoId(video.getId());

                attempts.add(attempt);
            }

            // Cập nhật danh sách attempts vào lesson
            if (!attempts.isEmpty()) {
                StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI lessonProgressDTOAPI = new StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI();
                lessonProgressDTOAPI.setAttempts(attempts);
                lessons.put(lessonKey, lessonProgressDTOAPI);
            }

            // Tính trạng thái hoàn thành bài học
            boolean allLessonsCompleted = lessons.values().stream().allMatch(lesson -> !lesson.getAttempts().isEmpty() && lesson.getAttempts().get(0).getScore() != null);

            // Nếu tất cả bài học trong chương đã hoàn thành, đánh dấu chương là completed
            if (allLessonsCompleted) {
                courseProgress.setCompleted(true);
            } else {
                courseProgress.setCompleted(false);
            }
            courseProgress.setLessons(lessons); // Cập nhật lại lessons vào courseProgress
        }

        // Gán courseProgressMap vào DTO
        studentCourseProgressDTO.setCourse_progress(courseProgressMap);

        // Trả về danh sách DTO
        List<StudentCourseProgressDTO> result = new ArrayList<>();
        result.add(studentCourseProgressDTO);

        return result;
    }


    public List<StudentCourseProgressDTO> getProgressCourse(Integer courseId, String studentId) {
        List<StudentCourseProgressDTO> studentCourseProgressDTOList = new ArrayList<>();

        // Lấy danh sách chương học của khóa học
        List<Chapter> chapterList = chapterRepository.findChaptersByCourseId(courseId);

        // Tạo DTO cho mỗi sinh viên
        StudentCourseProgressDTO studentCourseProgressDTO = new StudentCourseProgressDTO();
        studentCourseProgressDTO.setStudent_id(studentId); // Thêm ID sinh viên

        // Cấu trúc course_progress
        Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI> courseProgressMap = new LinkedHashMap<>();

        // Duyệt qua các chương học
        for (Chapter chapter : chapterList) {
            // Lấy danh sách bài học của mỗi chương
            List<Lesson> lessonList = lessonRepository.findLessonsByChapterId(chapter.getId());


            // Tạo course progress cho chương học
            StudentCourseProgressDTO.CourseProgressDTOAPI courseProgressDTOAPI = new StudentCourseProgressDTO.CourseProgressDTOAPI();
            courseProgressDTOAPI.setChapter_title(chapter.getTitle());

            // Map chứa các bài học trong chương
//            Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessonProgressMap = new HashMap<>();
            Map<String, StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI> lessonProgressMap = new LinkedHashMap<>();

            // Lấy testId cho chương
            List<Test> chapterTests = testRepository.findTestsByChapterIdAndIsChapterTest(chapter.getId());
            Integer chapterTestId = null;
            if (!chapterTests.isEmpty()) {
                chapterTestId = chapterTests.get(0).getId();
                courseProgressDTOAPI.setChapter_quiz(null);
                courseProgressDTOAPI.setCompleted(false);
            }

            // Duyệt qua các bài học trong chương
            for (Lesson lesson : lessonList) {
                StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI lessonProgressDTOAPI = new StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI();
                // Lấy video cho bài học, video luôn có
                Video video = videoRepository.findVideoByLessonId(lesson.getId());
                List<Test> lessonTests = testRepository.findTestsByLessonId(lesson.getId());
                Integer lessonTestId = null;
                if (!lessonTests.isEmpty()) {
                    lessonTestId = lessonTests.get(0).getId();
                }

                List<StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI> attemptDTOAPIS = new ArrayList<>();
                StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI itemAttempt = new StudentCourseProgressDTO.CourseProgressDTOAPI.LessonProgressDTOAPI.LessonAttemptDTOAPI();

                itemAttempt.setVideoId(video.getId());
                itemAttempt.setTestId(lessonTestId != null ? lessonTestId : null);
                itemAttempt.setScore(null);
                itemAttempt.setTimestamp(null);
                attemptDTOAPIS.add(itemAttempt);
                lessonProgressDTOAPI.setAttempts(attemptDTOAPIS);
                lessonProgressMap.put("L" + lesson.getId(), lessonProgressDTOAPI);
            }
            courseProgressDTOAPI.setLessons(lessonProgressMap);

            // Lưu thông tin vào courseProgressMap
            courseProgressMap.put("Ch" + chapter.getId(), courseProgressDTOAPI);
        }

        // Gán courseProgressMap vào DTO
        studentCourseProgressDTO.setCourse_progress(courseProgressMap);

        studentCourseProgressDTOList.add(studentCourseProgressDTO);

        return studentCourseProgressDTOList;
    }

    //    public StudentStatisticsDTO getStatisticsByCourse(Integer courseId, String classRoom) {
//        if (classRoom == "") {
//            classRoom = null;
//        }
//        long total = repository.countTotalStudentsByCourseAndClassRoom(courseId, classRoom);
//        long passed = repository.countPassedStudentsByCourseAndClassRoom(courseId, classRoom);
//        long failed = repository.countFailedStudentsByCourseAndClassRoom(courseId, classRoom);
//        long predicted = predictionRepo.countPredictedStudentsByCourseAndClassRoom(courseId, classRoom);
//        long predictedPass = predictionRepo.countPredictedPassByCourseAndClassRoom(courseId, classRoom);
//        long predictedFail = predictionRepo.countPredictedFailByCourseAndClassRoom(courseId, classRoom);
//
//        return new StudentStatisticsDTO(total, passed, failed, predicted, predictedPass, predictedFail);
//    }
    public StudentStatisticsDTO getStatisticsByCourse(Integer courseId, String classRoom) {
        if (classRoom == "") {
            classRoom = null;  // Nếu classRoom là rỗng thì set lại là null
        }

        long predicted = predictionRepo.countPredictedStudentsByCourseAndClassRoom(courseId, classRoom);

        // Lấy số lượng sinh viên đậu, rớt và số dự đoán

        // Số sinh viên dự đoán qua môn (prediction = 0)
        long passed = predictionRepo.countPredictedPassByCourseAndClassRoom(courseId, classRoom);
//        long passed = repository.countPassedStudentsByCourseAndClassRoom(courseId, classRoom);
//        long failed = repository.countFailedStudentsByCourseAndClassRoom(courseId, classRoom);

        long failed = predictionRepo.countPredictedFailByCourseAndClassRoom(courseId, classRoom);
//        long predicted = predictionRepo.countPredictedStudentsByCourseAndClassRoom(courseId, classRoom);

        return new StudentStatisticsDTO(passed, failed, predicted);  // Trả về số lượng sinh viên đậu, rớt và dự đoán
    }
//    public Page<StudentDTO> getStudentsByCourse(Integer courseId, int page, int size, String sortBy, String direction) {
//        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<StudentCourseData> studentPage = studentRepo.findByCourseId(courseId, pageable);
//
//        return studentPage.map(s -> new StudentDTO(
//                s.getId(),
//                s.getFullname(),
//                s.getAttendanceRate(),  // giả sử đây là percentage
//                s.getExamScore(),
//                s.getClassRoom(),
//                s.getEmail(),
//                s.getStudentId()
//        ));
//    }


}