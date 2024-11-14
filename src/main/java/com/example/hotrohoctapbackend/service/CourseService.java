package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.User.*;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    // Lấy type của khóa học theo ID
    public String getCourseTypeById(int id) {
        return courseRepository.findCourseTypeById(id);
    }

    public Map<String, Integer> getCourseStatistics(Integer courseId) {
        // Lấy kết quả từ repository dưới dạng List<Object[]>
        List<Object[]> resultList = courseRepository.getCourseStatistics(courseId);

        // Khởi tạo Map để chứa kết quả
        Map<String, Integer> statistics = new HashMap<>();

        // Kiểm tra nếu kết quả không rỗng và có chứa giá trị
        if (!resultList.isEmpty()) {
            Object[] result = resultList.get(0);  // Lấy dòng đầu tiên vì chỉ có một kết quả

            // Kiểm tra và ép kiểu từng phần tử của Object[]
            if (result[0] instanceof Number) {
                statistics.put("total_students", ((Number) result[0]).intValue());
            }
            if (result[1] instanceof Number) {
                statistics.put("total_lessons", ((Number) result[1]).intValue());
            }
        }

        return statistics;
    }

    public CourseDetailDTO getCourseDetailById(Integer id) {
        List<Object[]> result = courseRepository.findCourseById(id);

        // Kiểm tra xem có dữ liệu không
        if (!result.isEmpty()) {
            Object[] data = result.get(0); // Lấy dòng đầu tiên
            CourseDetailDTO courseDetailDTO = new CourseDetailDTO();

            // Ánh xạ các phần tử của Object[] vào CourseDetailDTO
            if (data[0] instanceof Integer) {
                courseDetailDTO.setId((Integer) data[0]);
            }
            if (data[1] instanceof String) {
                courseDetailDTO.setAuthor((String) data[1]);
            }
            if (data[2] instanceof BigDecimal) {
                courseDetailDTO.setCost((BigDecimal) data[2]);
            }
            if (data[3] instanceof String) {
                courseDetailDTO.setCourseOutput((String) data[3]);
            }
            if (data[4] instanceof Timestamp) {
                courseDetailDTO.setCreatedAt(convertToLocalDateTime(data[4]));
            }
            if (data[5] instanceof String) {
                courseDetailDTO.setDescription((String) data[5]);
            }
            if (data[6] instanceof String) {
                courseDetailDTO.setDuration((String) data[6]);
            }
            if (data[7] instanceof String) {
                courseDetailDTO.setImage_url((String) data[7]);
            }
            if (data[8] instanceof String) {
                courseDetailDTO.setLanguage((String) data[8]);
            }
            if (data[9] instanceof BigDecimal) {
                courseDetailDTO.setPrice((BigDecimal) data[9]);
            }
            if (data[10] instanceof Boolean) {
                courseDetailDTO.setStatus((Boolean) data[10]);
            }
            if (data[11] instanceof String) {
                courseDetailDTO.setTitle((String) data[11]);
            }
            if (data[12] instanceof Timestamp) {
                courseDetailDTO.setUpdatedAt(convertToLocalDateTime(data[12]));
            }
            if (data[13] instanceof Integer) {
                courseDetailDTO.setCourse_category_id((Integer) data[13]);
            }

            return courseDetailDTO;
        }
        return null; // Xử lý khi không tìm thấy dữ liệu
    }

    // Helper function to convert Object to LocalDateTime
    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

    public List<CourseDTO> getTop_6_CoursesWithDetails() {
        List<Object[]> results = courseRepository.findTopCoursesWithDetails();

        List<CourseDTO> courseSummaries = new ArrayList<>();
        for (Object[] row : results) {
            Integer id = (Integer) row[0];
            Integer danhmucID = (Integer) row[1];

            String imageUrl = (String) row[2];
            BigDecimal price = (BigDecimal) row[3];
            BigDecimal cost = (BigDecimal) row[4];
            String title = (String) row[5];
            String type = (String) row[6];
            Long numberOfStudents = (Long) row[7];
            Long totalLessons = (Long) row[8];

            BigDecimal rate = (BigDecimal) row[9];
            CourseDTO courseSummary = new CourseDTO(id, danhmucID, title, imageUrl, price, cost, numberOfStudents, totalLessons, rate, type);
            courseSummaries.add(courseSummary);
        }

        return courseSummaries;
    }

    public Page<CourseDTO> getCoursesByCategory(Integer courseCategoryId, Pageable pageable) {
        Page<Object[]> results = courseRepository.findByCourseCategoryId(courseCategoryId, pageable);
        return results.map(row -> new CourseDTO(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                (BigDecimal) row[4],
                (String) row[5],
                (Long) row[6],
                (Long) row[7],
                (BigDecimal) row[8],
                (String) row[9],
                (String) row[10],
                (Date) row[11],
                (Date) row[12],
                (String) row[13],
                (String) row[14],
                (String) row[15],
                (Boolean) row[16],
                (String) row[17]
        ));
    }

    public Page<CourseDTO> getCoursesByCategories(List<Integer> courseCategoryIds, Pageable pageable) {
        // Lấy kết quả từ repository
        Page<Object[]> results = courseRepository.findByCourseCategoryIds(courseCategoryIds, pageable);

        // Chuyển đổi từ Object[] sang CourseDTO
        return results.map(row -> new CourseDTO(
                (Integer) row[0], // course_id
                (Integer) row[1], // course_category_id
                (String) row[2],  // image_url
                (BigDecimal) row[3], // price
                (BigDecimal) row[4], // cost
                (String) row[5],  // course_title
                (Long) row[6], // number_of_students
                (Long) row[7], // total_lessons
                (BigDecimal) row[8], // average_rating
                (String) row[9],  // author
                (String) row[10], // course_output
                (Date) row[11], // created_at
                (Date) row[12], // updated_at
                (String) row[13], // description
                (String) row[14], // duration
                (String) row[15], // language
                (Boolean) row[16],// status
                (String) row[17]
        ));
    }

    public Page<CourseDTO> getAllCourse(Pageable pageable) {
        Page<Object[]> results = courseRepository.findAllCourses(pageable);
        return results.map(row -> new CourseDTO(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                (BigDecimal) row[4],
                (String) row[5],
                (Long) row[6],
                (Long) row[7],
                (BigDecimal) row[8],
                (String) row[9],
                (String) row[10],
                (Date) row[11],
                (Date) row[12],
                (String) row[13],
                (String) row[14],
                (String) row[15],
                (Boolean) row[16],
                (String) row[17]
        ));
    }

    public Page<CourseDTO_User_Profile> getCoursesByAccountId(Integer accountId, int page, int size) {
        Page<Object[]> results = courseRepository.findCoursesByAccountId(accountId, PageRequest.of(page, size));

        return results.map(result -> new CourseDTO_User_Profile(
                (Integer) result[0],     // id
                (String) result[1],      // duration
                (String) result[2],      // image_url
                (String) result[3],      // title
                ((Timestamp) result[4]).toLocalDateTime() // enrollment_date
        ));
    }

    //Section Vao Hoc
    public CourseInfoDetailDTO_User getCourseDetails(Integer courseId) {
        List<Chapter> chapters = chapterRepository.findChaptersByCourseId(courseId);
        List<CourseInfoDetailDTO_Chapter_User> chapterDTOs = new ArrayList<>();

        for (Chapter chapter : chapters) {
            List<Lesson> lessons = lessonRepository.findLessonsByChapterId(chapter.getId());
            List<CourseInfoDetailDTO_Lesson_User> lessonDTOs = new ArrayList<>();

            for (Lesson lesson : lessons) {
                Video video = videoRepository.findVideoByLessonId(lesson.getId());
                Test lessonTest = testRepository.findTestsByLessonId(lesson.getId()).isEmpty() ? null : testRepository.findTestsByLessonId(lesson.getId()).get(0);

                CourseInfoDetailDTO_Video_User videoDTO = video != null ? new CourseInfoDetailDTO_Video_User(video.getId(), video.getTitle(), video.getUrl(), video.getDocumentShort(), video.getDocumentUrl()) : null;
                CourseInfoDetailDTO_Test_User testDTO = lessonTest != null ? new CourseInfoDetailDTO_Test_User(lessonTest.getId(), lessonTest.getTitle(), "Test Bài") : null;

                CourseInfoDetailDTO_Lesson_User lessonDTO = new CourseInfoDetailDTO_Lesson_User(lesson.getId(), lesson.getTitle(), lesson.getDuration(), videoDTO, testDTO);
                lessonDTOs.add(lessonDTO);
            }

            Test chapterTest = testRepository.findChapterTestByChapterId(chapter.getId());
            CourseInfoDetailDTO_Test_User chapterTestDTO = chapterTest != null ? new CourseInfoDetailDTO_Test_User(chapterTest.getId(), chapterTest.getTitle(), "Test Chương") : null;

            CourseInfoDetailDTO_Chapter_User chapterDTO = new CourseInfoDetailDTO_Chapter_User(chapter.getId(), chapter.getTitle(), lessonDTOs, chapterTestDTO);
            chapterDTOs.add(chapterDTO);
        }

        return new CourseInfoDetailDTO_User(courseId, "Course Title", chapterDTOs);
    }


    public Course addCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setAuthor(courseDTO.getAuthor());
        course.setStatus(courseDTO.getStatus());
        course.setDescription(courseDTO.getDescription());
        course.setDuration(courseDTO.getDuration());
        course.setLanguage(courseDTO.getLanguage());
        course.setCost(courseDTO.getCost());
        course.setPrice(courseDTO.getPrice());
        course.setCourseOutput(courseDTO.getCourse_output());
        course.setImage_url(courseDTO.getImageUrl());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        CourseCategory courseCategory = courseCategoryRepository.findById(courseDTO.getId_danhmuc())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        course.setCourseCategory(courseCategory); // Đặt CourseCategory

        return courseRepository.save(course);
    }

    public Course updateCourse(Integer courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseDTO.getTitle());
        course.setAuthor(courseDTO.getAuthor());
        course.setStatus(courseDTO.getStatus());
        course.setDescription(courseDTO.getDescription());
        course.setDuration(courseDTO.getDuration());
        course.setLanguage(courseDTO.getLanguage());
        course.setCost(courseDTO.getCost());
        course.setPrice(courseDTO.getPrice());
        course.setCourseOutput(courseDTO.getCourse_output());
        course.setImage_url(courseDTO.getImageUrl());
        course.setUpdatedAt(LocalDateTime.now());

        CourseCategory courseCategory = courseCategoryRepository.findById(courseDTO.getId_danhmuc())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        course.setCourseCategory(courseCategory); // Đặt CourseCategory

        return courseRepository.save(course);
    }

    public Course deleteCourseAdmin(int courseId) {
        // Tìm tài khoản theo ID
        Optional<Course> accountOpt = courseRepository.findById(courseId);

        if (accountOpt.isPresent()) {
            Course account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return courseRepository.save(account);
        } else {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }

    public Course activeCourseAdmin(int courseId) {
        // Tìm tài khoản theo ID
        Optional<Course> lessonOpt = courseRepository.findById(courseId);

        if (lessonOpt.isPresent()) {
            Course lesson = lessonOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            lesson.setDeleted(false);
            lesson.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return courseRepository.save(lesson);
        } else {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }
}
