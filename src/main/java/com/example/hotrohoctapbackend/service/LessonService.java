package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminLessonGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.LessonDTOVideo_Admin;
import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Lesson addLesson(LessonDTO2 lessonDTO2) {
        // Tìm chapter dựa trên chapter_id
        Chapter chapter = chapterRepository.findById(lessonDTO2.getChapter_id())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Tạo lesson mới và gán các thuộc tính từ DTO
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO2.getTitle());
        lesson.setChapter(chapter); // Gán chapter tìm được

        // Thiết lập CreatedAt và UpdatedAt
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreatedAt(now);
        lesson.setUpdatedAt(now); // Khi tạo mới, CreatedAt và UpdatedAt sẽ giống nhau

        lesson.setDuration(lessonDTO2.getDuration());
        Optional<Course> course = courseRepository.findById(lessonDTO2.getCourse_id());


        lesson.setCourse(course.get());

        lesson.setDeleted(false);
        lesson.setDeletedDate(LocalDateTime.now());
        // Lưu lesson vào cơ sở dữ liệu
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
            lessonDTOVideoAdmin.setTest_title((String) item[13]);    // test_title

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
                lesson.getCourse().getId()
        );
    }

    public Lesson updateLessonAdmin(Lesson lesson) {
        lesson.setUpdatedAt(LocalDateTime.now());
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



}
