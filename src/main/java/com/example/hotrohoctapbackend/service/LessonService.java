package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminLessonGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.LessonDTOVideo_Admin;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminChapterDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLessonsDTOList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLesssonDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Lesson.LessonDTO;
import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.entity.Test;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;


    @Autowired
    private TestRepository testRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Lesson saveLesson(Lesson item) {
        return lessonRepository.save(item);
    }

    public Lesson addLesson(LessonDTO2 lessonDTO2) {
        // Tìm chapter dựa trên chapter_id
        Chapter chapter = chapterRepository.findById(lessonDTO2.getChapter_id())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Tạo lesson mới và gán các thuộc tính từ DTO
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO2.getTitle());
        lesson.setChapter(chapter);
        lesson.setDuration(lessonDTO2.getDuration());
        // Thiết lập CreatedAt và UpdatedAt
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreatedAt(now);
        lesson.setUpdatedAt(now);
        lesson.setIsTestExcluded(lessonDTO2.getIsTestExcluded());
        lesson.setStatus(lessonDTO2.getStatus());
        lesson.setTopic(lessonDTO2.getTopic());
        Optional<Course> course = courseRepository.findById(lessonDTO2.getCourse_id());

        lesson.setCourse(course.get());
        lesson.setDeleted(false);
        lesson.setDeletedDate(LocalDateTime.now());
        // Lưu lesson vào cơ sở dữ liệu
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Integer lessonId, LessonDTO2 lessonDTO2) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Chapter chapter = chapterRepository.findById(lessonDTO2.getChapter_id())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        Course course = courseRepository.findById(lessonDTO2.getCourse_id())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        lesson.setTitle(lessonDTO2.getTitle());
        lesson.setChapter(chapter);
        lesson.setDuration(lessonDTO2.getDuration());
        lesson.setTopic(lessonDTO2.getTopic());
        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setIsTestExcluded(lessonDTO2.getIsTestExcluded());
        lesson.setStatus(lessonDTO2.getStatus());
        lesson.setCourse(course);

        return lessonRepository.save(lesson);
    }

    public LessonDTO2 getLessonByIdAdmin(int id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            return convertToDTO(lesson);
        } else {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }

    public List<LessonDTOVideo_Admin> getLessonVideoTestDataByLessonId(int lessonId) {

        List<Object[]> list = lessonRepository.findLessonVideoTestDataByLessonId(lessonId);

        List<LessonDTOVideo_Admin> lessonDTOVideoAdmins = new ArrayList<>();

        for (Object[] item : list) {
            LessonDTOVideo_Admin lessonDTOVideoAdmin = new LessonDTOVideo_Admin();

            // Ánh xạ các giá trị từ Object[] vào LessonDTOVideo_Admin
            lessonDTOVideoAdmin.setId((Integer) item[0]);            // id
            lessonDTOVideoAdmin.setTitle((String) item[1]);          // title
            lessonDTOVideoAdmin.setCreatedAt(((Timestamp) item[2]).toLocalDateTime()); // createdAt
            lessonDTOVideoAdmin.setUpdatedAt(((Timestamp) item[3]).toLocalDateTime());
            lessonDTOVideoAdmin.setDuration((Integer) item[4]);      // duration
            lessonDTOVideoAdmin.setChapter_id((Integer) item[5]);    // chapter_id
            lessonDTOVideoAdmin.setCourse_id((Integer) item[6]);     // course_id
            lessonDTOVideoAdmin.setVideo_id((Integer) item[7]);      // video_id
            lessonDTOVideoAdmin.setVideo_title((String) item[8]);    // video_title
            lessonDTOVideoAdmin.setVideo_url((String) item[9]);      // video_url

            lessonDTOVideoAdmin.setDocument_short((String) item[10]);      // video_url
            lessonDTOVideoAdmin.setDocument_url((String) item[11]);      // video_url

            lessonDTOVideoAdmin.setTest_id((Integer) item[12]);      // test_id
            lessonDTOVideoAdmin.setTopic((String) item[13]);
            lessonDTOVideoAdmin.setTest_title((String) item[14]);    // test_title

            lessonDTOVideoAdmins.add(lessonDTOVideoAdmin);

        }


        return lessonDTOVideoAdmins;
    }

    private LessonDTO2 convertToDTO(Lesson lesson) {
        return new LessonDTO2(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getCreatedAt(),
                lesson.getUpdatedAt(),
                lesson.getDuration(),
                lesson.getChapter().getId(),
                lesson.getCourse().getId(),
                lesson.getIsTestExcluded(),
                lesson.isStatus(),
                lesson.getTopic()
        );
    }

    @Transactional
    public Lesson updateLessonAdmin(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson deleteLessonAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Lesson> accountOpt = lessonRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Lesson account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return lessonRepository.save(account);
        } else {
            throw new RuntimeException("Lesson not found with id: " + testID);
        }
    }

    public Lesson activeLessonAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Lesson> lessonOpt = lessonRepository.findById(testID);

        if (lessonOpt.isPresent()) {
            Lesson lesson = lessonOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            lesson.setDeleted(false);
            lesson.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return lessonRepository.save(lesson);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }

    public Lesson LockLessonAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Lesson> accountOpt = lessonRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Lesson lesson = accountOpt.get();
            lesson.setStatus(false);
            return lessonRepository.save(lesson);
        } else {
            throw new RuntimeException("Lesson not found with id: " + testID);
        }
    }

    //Mở khóa bài học
    public Boolean UnlockLessonAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Lesson> lessonOpt = lessonRepository.findById(testID);
        if (lessonOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bài học với ID: " + testID);
        }
        Lesson lesson = lessonOpt.get();
        if (lesson.getTopic() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bài học chưa có topic!");
        }
        if ("EMPTYTEST".equals(lesson.getIsTestExcluded())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bài học chưa có bài kiểm tra!");
        }
        lesson.setStatus(true);
        Lesson lesson1 = lessonRepository.save(lesson);
        if (lesson1 != null) {
            return true;
        }
        return false;

    }

    public Page<AdminLessonGetDTO> getLessonWithCourseAndChapter(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> dataPage = lessonRepository.findLessonCourseChapterData(pageable);
        Page<AdminLessonGetDTO> resultPage = dataPage.map(row -> {
            AdminLessonGetDTO dto = new AdminLessonGetDTO();
            dto.setId((Integer) row[0]);
            dto.setLessonTitle((String) row[1]);
            dto.setCourseName((String) row[2]);
            dto.setChapterName((String) row[3]);
            dto.setDeleted((Boolean) row[4]);
            return dto;
        });

        return resultPage;
    }

    public List<AdminLessonGetDTO> getLessonWithCourseAndChapterList() {
        // Lấy tất cả dữ liệu từ repository mà không phân trang
        List<Object[]> dataList = lessonRepository.findLessonCourseChapterDataList();

        // Ánh xạ dữ liệu từ Object[] sang AdminLessonGetDTO
        List<AdminLessonGetDTO> resultList = new ArrayList<>();
        for (Object[] row : dataList) {
            AdminLessonGetDTO dto = new AdminLessonGetDTO();
            dto.setId((Integer) row[0]);
            dto.setLessonTitle((String) row[1]);
            dto.setCourseName((String) row[2]);
            dto.setChapterName((String) row[3]);
            dto.setDeleted((Boolean) row[4]);
            resultList.add(dto);
        }

        return resultList;
    }

    //DAnh sach bai học theo chapterId và courseId - Lesson chưa có bài TEST - EMPTYTEST
    public List<AdminLessonsDTOList> getLessonByChapterId(Integer chapterId, Integer courseId) {
        // Lấy tất cả dữ liệu từ repository mà không phân trang
        List<Object[]> dataList = lessonRepository.findLessonsByChapterId_V2EMPTYTEST(chapterId, courseId);
        List<AdminLessonsDTOList> adminLessonsDTOLists = new ArrayList<>();
        for (Object[] item : dataList) {
            AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();

            adminLessonsDTOList.setId((Integer) item[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(item[1]);
            adminLessonsDTOList.setCreatedAt(createAt);
            adminLessonsDTOList.setDuration((Integer) item[2]);
            adminLessonsDTOList.setLessonTitle((String) item[3]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(item[4]);
            adminLessonsDTOList.setUpdatedAt(updateAt);
            adminLessonsDTOList.setChapterId((Integer) item[5]);
            adminLessonsDTOList.setCourseId((Integer) item[6]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(item[7]);
            adminLessonsDTOList.setDeletedDate(deleteAt);
            adminLessonsDTOList.setIsDeleted((Boolean) item[8]);
            adminLessonsDTOList.setIsTestExcluded((String) item[9]);
            adminLessonsDTOList.setTopic((String) item[10]);

            // Thêm vào danh sách kết quả
            adminLessonsDTOLists.add(adminLessonsDTOList);
        }
        return adminLessonsDTOLists;
    }


    public Page<AdminLessonsDTOList> getLessonByChapterIdPage(Integer chapterId, Integer courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository có phân trang
        Page<Object[]> results = lessonRepository.findLessonsByChapterId_V2Page(chapterId, courseId, pageable);

        // Chuyển đổi Object[] thành AdminLessonsDTOList
        List<AdminLessonsDTOList> adminLessonsDTOLists = results.getContent().stream().map(item -> {
            AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();
            adminLessonsDTOList.setId((Integer) item[0]);
            adminLessonsDTOList.setCreatedAt(convertTimestampToLocalDateTime(item[1]));
            adminLessonsDTOList.setDuration((Integer) item[2]);
            adminLessonsDTOList.setLessonTitle((String) item[3]);
            adminLessonsDTOList.setUpdatedAt(convertTimestampToLocalDateTime(item[4]));
            adminLessonsDTOList.setChapterId((Integer) item[5]);
            adminLessonsDTOList.setCourseId((Integer) item[6]);
            adminLessonsDTOList.setDeletedDate(convertTimestampToLocalDateTime(item[7]));
            adminLessonsDTOList.setIsDeleted(item[8] != null ? (Boolean) item[8] : false);
            adminLessonsDTOList.setIsTestExcluded(item[9] != null ? (String) item[9] : "");
            adminLessonsDTOList.setTopic((String) item[10]);
            adminLessonsDTOList.setStatus(item[11] != null ? (Boolean) item[11] : false);
            return adminLessonsDTOList;
        }).collect(Collectors.toList());

        // Trả về dữ liệu đã phân trang
        return new PageImpl<>(adminLessonsDTOLists, pageable, results.getTotalElements());
    }

    public Boolean updateExcludedChapterLesson(AdminLessonsDTOList[] adminLessonsDTOList) {
        try {
            for (AdminLessonsDTOList item : adminLessonsDTOList) {
                Lesson lesson = lessonRepository.findById(item.getId()).orElse(null);
                if (lesson == null) {
                    return false;  // If lesson not found, return false
                }

                if ("NOTTEST".equals(item.getIsTestExcluded())) {
                    List<Object[]> test = testRepository.findTestByLessonId_V2(item.getId());
                    if (test.size() != 0) {
                        for (Object[] item2 : test) {
                            Integer testId = (Integer) item2[0];
                            int deleteCount = testRepository.deleteTest(testId);
                        }
                    }

                    lesson.setIsTestExcluded(item.getIsTestExcluded());
                    int updateExcluded = lessonRepository.updateExcludedLesson(lesson.getId(), item.getIsTestExcluded());
                } else if ("FULLTEST".equals(item.getIsTestExcluded())) {
                    List<Object[]> test = testRepository.findTestByLessonId_V2(item.getId());
                    if (test.size() == 0) {
                        Test createNew = new Test();
                        createNew.setAssigned(true);
                        createNew.setChapter(chapterRepository.findById(item.getChapterId()).orElse(null));
                        createNew.setCourse(courseRepository.findById(item.getCourseId()).orElse(null));
                        createNew.setLesson(lesson);
                        createNew.setSummary(false);
                        createNew.setDeleted(false);
                        createNew.setCreatedAt(new Date());
                        createNew.setUpdatedAt(new Date());
                        createNew.setDeletedDate(LocalDateTime.now());
                        createNew.setTitle(item.getLessonTitle());
                        testRepository.save(createNew);
                    }
                    lesson.setIsTestExcluded(item.getIsTestExcluded());
                    int updateExcluded = lessonRepository.updateExcludedLesson(lesson.getId(), item.getIsTestExcluded());
                }
            }
        } catch (Exception e) {
            return false;  // Return false if any exception occurs
        }
        return true;  // Return true if no exceptions occur
    }


    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    //Restore
    public Page<AdminLesssonDTORestoreList> getLessons(Integer courseId, Integer chapterId, String lessonTitle, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = lessonRepository.findLessons(courseId, chapterId, lessonTitle, deletedDate, pageable);
        List<AdminLesssonDTORestoreList> adminLesssonDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminLesssonDTORestoreList dto = new AdminLesssonDTORestoreList();
            dto.setId((Integer) result[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[1]);
            dto.setCreatedAt(createAt);
            dto.setDuration((Integer) result[2]);
            dto.setLessonTitle((String) result[3]);

            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[4]);
            dto.setUpdatedAt(updateAt);

            dto.setChapterId((Integer) result[5]);
            dto.setCourseId((Integer) result[6]);

            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[7]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[8]);
            dto.setIsTestExcluded((String) result[9]);
            adminLesssonDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminLesssonDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    @Transactional
    public Lesson updateRestoreLesson(AdminLesssonDTORestoreList adminLesssonDTORestoreList) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(adminLesssonDTORestoreList.getId());
        if (lessonOptional.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + adminLesssonDTORestoreList.getId());
        } else {
            Lesson chapter = lessonOptional.get();
            chapter.setDeleted(false);
            return lessonRepository.save(chapter);
        }
    }

    public void deleteRestoreLesson(AdminLesssonDTORestoreList lesssonDTORestoreList) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lesssonDTORestoreList.getId());
        if (lessonOptional.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + lesssonDTORestoreList.getId());
        } else {
            lessonRepository.delete(lessonOptional.get());
        }
    }

    //    public List<AdminLessonsDTOList> getLessonDetails(Integer testId, Integer lessonId, Integer chapterId) {
//        List<Object[]> result = lessonRepository.findLessonByTestIdAndLessonId(testId, lessonId, chapterId);
//        List<AdminLessonsDTOList> adminLessonsDTOLists = new ArrayList<>();
//        for (Object[] item : result) {
//            AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();
//
//            adminLessonsDTOList.setId((Integer) item[0]);
//            LocalDateTime createAt = convertTimestampToLocalDateTime(item[1]);
//            adminLessonsDTOList.setCreatedAt(createAt);
//            adminLessonsDTOList.setDuration((Integer) item[2]);
//            adminLessonsDTOList.setLessonTitle((String) item[3]);
//            LocalDateTime updateAt = convertTimestampToLocalDateTime(item[4]);
//            adminLessonsDTOList.setUpdatedAt(updateAt);
//            adminLessonsDTOList.setChapterId((Integer) item[5]);
//            adminLessonsDTOList.setCourseId((Integer) item[6]);
//            LocalDateTime deleteAt = convertTimestampToLocalDateTime(item[7]);
//            adminLessonsDTOList.setDeletedDate(deleteAt);
//            adminLessonsDTOList.setIsDeleted((Boolean) item[8]);
//            adminLessonsDTOList.setIsTestExcluded((String) item[9]);
//            adminLessonsDTOList.setTopic((String) item[10]);
//            adminLessonsDTOList.setStatus((Boolean) item[11]);
//
//            // Thêm vào danh sách kết quả
//            adminLessonsDTOLists.add(adminLessonsDTOList);
//        }
//        return adminLessonsDTOLists;
//    }

//    public List<LessonDTO> getLessonsByChapterId(Integer chapterId) {
//        List<Lesson> lessons = lessonRepository.findLessonsByChapterIdAndIsDeleted(chapterId);
//
//        return lessons.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }
//
//    // Chuyển đổi từ Entity Lesson sang DTO
//    private LessonDTO convertToDTO(Lesson lesson) {
//        LessonDTO dto = new LessonDTO();
//        dto.setId(lesson.getId());
//        dto.setCreatedAt(lesson.getCreatedAt().toString());
//        dto.setDuration(lesson.getDuration());
//        dto.setLessonTitle(lesson.getTitle());
//        dto.setUpdatedAt(lesson.getUpdatedAt().toString());
//        dto.setChapterId(lesson.getChapter().getId());
//        dto.setCourseId(lesson.getCourse().getId());
//        dto.setDeletedDate(lesson.getDeletedDate().toString());
//        dto.setIsDeleted(lesson.isDeleted());
//        dto.setIsTestExcluded(lesson.getIsTestExcluded());
//        dto.setTopic(lesson.getTopic());
//        dto.setStatus(lesson.isStatus() ? "active" : "inactive");
//
//        return dto;
//    }

    @Transactional
    public List<AdminLessonsDTOList> getLessonDetails(Integer testId, Integer lessonId, Integer chapterId) {
        List<AdminLessonsDTOList> adminLessonsDTOLists = new ArrayList<>();

        if (lessonId != null) {
            // Trường hợp bài kiểm tra có bài học (lessonId không null)
            List<Object[]> result = lessonRepository.findLessonByTestIdAndLessonId(testId, lessonId, chapterId);

            for (Object[] item : result) {
                AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();
                adminLessonsDTOList.setId((Integer) item[0]);
                adminLessonsDTOList.setCreatedAt(convertTimestampToLocalDateTime(item[1]));
                adminLessonsDTOList.setDuration((Integer) item[2]);
                adminLessonsDTOList.setLessonTitle((String) item[3]);
                adminLessonsDTOList.setUpdatedAt(convertTimestampToLocalDateTime(item[4]));
                adminLessonsDTOList.setChapterId((Integer) item[5]);
                adminLessonsDTOList.setCourseId((Integer) item[6]);
                adminLessonsDTOList.setDeletedDate(convertTimestampToLocalDateTime(item[7]));
                adminLessonsDTOList.setIsDeleted((Boolean) item[8]);
                adminLessonsDTOList.setIsTestExcluded((String) item[9]);
                adminLessonsDTOList.setTopic((String) item[10]);
                adminLessonsDTOList.setStatus((Boolean) item[11]);

                // Thêm vào danh sách kết quả
                adminLessonsDTOLists.add(adminLessonsDTOList);
            }
        } else {
            // Trường hợp bài kiểm tra không có bài học (lessonId là null)
            List<Lesson> lessons = lessonRepository.findLessonsByChapterIdAndIsDeleted(chapterId);

            for (Lesson lesson : lessons) {
                AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();
                adminLessonsDTOList.setId(lesson.getId());
                adminLessonsDTOList.setCreatedAt(lesson.getCreatedAt());
                adminLessonsDTOList.setDuration(lesson.getDuration());
                adminLessonsDTOList.setLessonTitle(lesson.getTitle());
                adminLessonsDTOList.setUpdatedAt(lesson.getUpdatedAt());
                adminLessonsDTOList.setChapterId(lesson.getChapter().getId());
                adminLessonsDTOList.setCourseId(lesson.getCourse().getId());
                adminLessonsDTOList.setDeletedDate(lesson.getDeletedDate());
                adminLessonsDTOList.setIsDeleted(lesson.isDeleted());
                adminLessonsDTOList.setIsTestExcluded(lesson.getIsTestExcluded());
                adminLessonsDTOList.setTopic(lesson.getTopic());
                adminLessonsDTOList.setStatus(lesson.isStatus());

                // Thêm vào danh sách kết quả
                adminLessonsDTOLists.add(adminLessonsDTOList);
            }
        }

        return adminLessonsDTOLists;
    }


    public AdminLessonsDTOList getLessonDetailById(int lessonId) {
        List<Object[]> result = lessonRepository.findLessonByLessonId(lessonId);
        List<AdminLessonsDTOList> adminLessonsDTOLists = new ArrayList<>();
        for (Object[] item : result) {
            AdminLessonsDTOList adminLessonsDTOList = new AdminLessonsDTOList();

            adminLessonsDTOList.setId((Integer) item[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(item[1]);
            adminLessonsDTOList.setCreatedAt(createAt);
            adminLessonsDTOList.setDuration((Integer) item[2]);
            adminLessonsDTOList.setLessonTitle((String) item[3]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(item[4]);
            adminLessonsDTOList.setUpdatedAt(updateAt);
            adminLessonsDTOList.setChapterId((Integer) item[5]);
            adminLessonsDTOList.setCourseId((Integer) item[6]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(item[7]);
            adminLessonsDTOList.setDeletedDate(deleteAt);
            adminLessonsDTOList.setIsDeleted((Boolean) item[8]);
            adminLessonsDTOList.setIsTestExcluded((String) item[9]);
            adminLessonsDTOList.setTopic((String) item[10]);
            adminLessonsDTOList.setStatus((Boolean) item[11]);

            // Thêm vào danh sách kết quả
            adminLessonsDTOLists.add(adminLessonsDTOList);
        }
        return adminLessonsDTOLists.get(0);
    }

    public List<LessonDTO> getLessonsByCourseId(int courseId) {
        List<Lesson> lessons = lessonRepository.findLessonsByCourseId(courseId);
        return lessons.stream().map(this::convertToDTO_V3).collect(Collectors.toList());
    }

    private LessonDTO convertToDTO_V3(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setCreatedAt(lesson.getCreatedAt().toString()); // Assuming the format is OK
        dto.setDuration(lesson.getDuration());
        dto.setLessonTitle(lesson.getTitle());
        dto.setUpdatedAt(lesson.getUpdatedAt().toString());
        dto.setChapterId(lesson.getChapter() != null ? lesson.getChapter().getId() : 0);
        dto.setCourseId(lesson.getCourse() != null ? lesson.getCourse().getId() : 0);
        dto.setDeletedDate(lesson.getDeletedDate() != null ? lesson.getDeletedDate().toString() : null);
        dto.setIsDeleted(lesson.isDeleted());
        dto.setIsTestExcluded(lesson.getIsTestExcluded());
        dto.setTopic(lesson.getTopic());
        dto.setStatus(lesson.isStatus());

        return dto;
    }
}
