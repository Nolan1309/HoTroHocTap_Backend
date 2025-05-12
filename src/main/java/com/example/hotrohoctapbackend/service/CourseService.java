package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.Admin.*;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseDTOAdminV3;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseDTOUserPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseForListAdminDTO;
import com.example.hotrohoctapbackend.DTO.User.*;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.enums.DiscountStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import com.example.hotrohoctapbackend.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private Enrolled_CoursesRepository enrolled_coursesRepository;

    @Autowired
    private Course_DiscountRepository courseDiscountRepository;


    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Lấy type của khóa học theo ID
    public String getCourseTypeById(int id) {
        return courseRepository.findCourseTypeById(id);
    }

    public CourseDTOAdminV3 getCourseById(int id, Integer accountId) {
        Course course = courseRepository.findById(id).get();

        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        Integer rating = 0;
        List<Review> reviewList = course.getReviews();
        for (Review review : reviewList) {
            rating += review.getRating();
        }

        Double rate = reviewList.isEmpty() ? 0.0 : (double) rating / reviewList.size();

        Optional<Enrolled_Courses> enrolledCoursesCheck = enrolled_coursesRepository
                .findByAccountIdAndCourseId(accountId, course.getId());  // Assuming you have a method like this

        boolean isPurchased = false;
        if (!enrolledCoursesCheck.isEmpty()) {
            isPurchased = true;
        }
        return new CourseDTOAdminV3(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getImage_url(),
                course.getLanguage(),
                course.getAuthor(),
                course.getCourseOutput(),
                course.getCost(),
                course.getPrice(),
                course.getDuration(),
                course.getType(),
                course.getStatus(),
                course.getCreatedAt().toString(),
                course.getUpdatedAt().toString(),
                course.getDeletedDate().toString(),
                course.isDeleted(),
                String.valueOf(course.getAccount().getId()),
                String.valueOf(course.getCategory().getId()),
                course.getCategory().getName(),
                course.getEnrolledCourses().size(),
                rate,
                course.getLevel(),
                "Certificate",
                isPurchased
        );
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

            Boolean status = (Boolean) row[7];

            Long numberOfStudents = (Long) row[8];
            Long totalLessons = (Long) row[9];

            BigDecimal rate = (BigDecimal) row[10];

            CourseDTO courseSummary = new CourseDTO(id, danhmucID, title, imageUrl, price, cost, numberOfStudents, totalLessons, rate, type, status);
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
                (Integer) row[14],
                (String) row[15],
                (Boolean) row[16],
                (String) row[17]
        ));
    }

    public Page<CourseDTO> getCoursesByCategories(Integer categoryId, Integer categoryIds, Pageable pageable) {
        Page<Object[]> results = courseRepository.findByCourseCategoryIdsNoWithCap2(categoryId, categoryIds, pageable);
        if (categoryIds != 0) {
            results = courseRepository.findByCourseCategoryIdsWithCap2(categoryId, categoryIds, pageable);
        }


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
                (Integer) row[14], // duration
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
                (Integer) row[14],
                (String) row[15],
                (Boolean) row[16],
                (String) row[17]
        ));
    }

    public Page<CourseDTO_User_Profile> getCoursesByAccountId(Integer accountId, int page, int size) {
        Page<Object[]> results = courseRepository.findCoursesByAccountId(accountId, PageRequest.of(page, size));

        return results.map(result -> new CourseDTO_User_Profile(
                (Integer) result[0],     // id
                (Integer) result[1],      // duration
                (String) result[2],      // image_url
                (String) result[3],      // title
                ((Timestamp) result[4]).toLocalDateTime(), // enrollment_date
                (Boolean) result[5],
                (Boolean) result[6]
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


    public Course addCourse(AdminAddCourseDTO courseDTO) {
        // Tạo đối tượng Course từ DTO
        Course newCourse = new Course();
        newCourse.setTitle(courseDTO.getCoursesTitle());
        newCourse.setDescription(courseDTO.getDescription());
        newCourse.setImage_url(courseDTO.getImageUrl());
        newCourse.setCourseOutput(courseDTO.getCourseOutput());
        newCourse.setLanguage(courseDTO.getLanguage());
        newCourse.setAuthor(courseDTO.getAuthor());
        newCourse.setDuration(courseDTO.getDuration());
        newCourse.setCost(courseDTO.getCost());
        newCourse.setPrice(courseDTO.getPrice());
        newCourse.setStatus(false);
        newCourse.setLevel(courseDTO.getLevel());
        newCourse.setType(courseDTO.getType());
        newCourse.setCreatedAt(LocalDateTime.now());
        newCourse.setUpdatedAt(LocalDateTime.now());
        newCourse.setDeleted(false);

        // Lấy thông tin CourseCategory và Account từ ID
        Category courseCategory = categoryRepository.findById(courseDTO.getCourseCategoryId())
                .orElseThrow(() -> new RuntimeException("Course Category not found"));
        Account account = accountRepository.findById(courseDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        newCourse.setCategory(courseCategory);
        newCourse.setAccount(account);

        // Lưu khóa học vào database
        return courseRepository.save(newCourse);
    }

    public Double checkDiscountForCourse(Integer courseId) {
        Double discount = courseRepository.getDiscountForCourse(courseId);

        if (discount != null) {
            return discount;
        } else {
            return 0.0;
        }
    }

    public Boolean editCourse(Integer courseId, AdminAddCourseDTO courseDTO) {
        // Tìm khóa học cần chỉnh sửa từ ID
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        double phamtram = 100;
        if (existingCourse.getType().equals("FEE")) {
            if (existingCourse.getCost() != existingCourse.getPrice()) {
                Double value = checkDiscountForCourse(courseId);
                if (value != 0) {
                    phamtram = phamtram - value;
                }
            }
        }
        // Cập nhật thông tin của khóa học từ DTO
        existingCourse.setTitle(courseDTO.getCoursesTitle());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setImage_url(courseDTO.getImageUrl());
        existingCourse.setCourseOutput(courseDTO.getCourseOutput());
        existingCourse.setLanguage(courseDTO.getLanguage());

        existingCourse.setDuration(courseDTO.getDuration());


        if (existingCourse.getType().equals("FEE")) {
            BigDecimal price = courseDTO.getCost().multiply(BigDecimal.valueOf(phamtram / 100));
            existingCourse.setPrice(price);
            existingCourse.setCost(courseDTO.getCost());
        }


        existingCourse.setType(courseDTO.getType());
        existingCourse.setUpdatedAt(LocalDateTime.now());


        if (courseDTO.getCourseCategoryId() != null) {
            Category courseCategory = categoryRepository.findById(courseDTO.getCourseCategoryId())
                    .orElseThrow(() -> new RuntimeException("Course Category not found"));
            existingCourse.setCategory(courseCategory);
        }

        if (courseDTO.getAccountId() != null) {
            Account account = accountRepository.findById(courseDTO.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            existingCourse.setAccount(account);
            existingCourse.setAuthor(account.getFullname());
        }
        courseRepository.save(existingCourse);
        // Lưu khóa học đã chỉnh sửa vào database
        return true;
    }

    public Boolean updateCourse(Integer courseId, String title, String author, String description, Integer duration, String language,
                                String cost, String price, String courseOutput, Integer courseCategoryId,
                                Integer accountId, String type, Boolean status, String imageUrl, Course existingCourse, String level) {


        // Cập nhật thông tin của khóa học từ FormData
        existingCourse.setTitle(title);
        existingCourse.setDescription(description);
        existingCourse.setImage_url(imageUrl); // Lưu URL mới hoặc giữ lại URL cũ
        existingCourse.setCourseOutput(courseOutput);
        existingCourse.setLanguage(language);
        existingCourse.setDuration(duration);
        existingCourse.setLevel(level);
        existingCourse.setType(type);
        existingCourse.setStatus(status);
        existingCourse.setUpdatedAt(LocalDateTime.now());
        existingCourse.setAuthor(author);

        if (type.equals("FEE")) {
            BigDecimal priceBig = new BigDecimal(price);
            BigDecimal costBig = new BigDecimal(cost);
            existingCourse.setPrice(priceBig);
            existingCourse.setCost(costBig);
        } else {
            existingCourse.setPrice(new BigDecimal(0));
            existingCourse.setCost(new BigDecimal(0));
        }
        Optional<Course_Discount> courseDiscount = courseDiscountRepository.findByCourseId(existingCourse.getId());
        if (courseDiscount.isPresent() && courseDiscount.get().getDiscount() != null && courseDiscount.get().getDiscount().getStatus() == DiscountStatus.ACTIVE && type.equals("FEE")) {
            UpdatePriceCourse(courseDiscount.get().getDiscount().getDiscountValue(), existingCourse);
        }

        if (courseCategoryId != null) {
            Category courseCategory = categoryRepository.findById(courseCategoryId)
                    .orElseThrow(() -> new RuntimeException("Course Category not found"));
            existingCourse.setCategory(courseCategory);
        }

        if (accountId != null) {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            existingCourse.setAccount(account);
            existingCourse.setAuthor(account.getFullname());
        }

        courseRepository.save(existingCourse);

        return true;
    }

    public boolean UpdatePriceCourse(BigDecimal percentDiscount, Course course) {
        BigDecimal percentDecimal = percentDiscount.divide(BigDecimal.valueOf(100));
        BigDecimal finalPrice = course.getCost().multiply(BigDecimal.ONE.subtract(percentDecimal));
        course.setPrice(finalPrice);
        return true;
    }

    public Page<AdminCourseGetDTO> getCoursesWithCategoryAdmin(int page, int size) {
        // Tạo đối tượng Pageable từ page và size
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để lấy dữ liệu phân trang
        Page<Object[]> resultPage = courseRepository.findCourseWithCategory(pageable);

        // Chuyển đổi từ Object[] sang DTO
        List<AdminCourseGetDTO> dtoList = resultPage.getContent().stream()
                .map(row -> {
                    AdminCourseGetDTO dto = new AdminCourseGetDTO();
                    dto.setId((Integer) row[0]);  // id
                    dto.setCourseTitle((String) row[1]);  // courseTitle
                    dto.setDuration((String) row[2]);  // duration
                    dto.setLanguage((String) row[3]);  // language
                    dto.setCategoryName((String) row[4]);  // categoryName
                    dto.setDeleted((Boolean) row[5]);  // deleted
                    return dto;
                })
                .collect(Collectors.toList());

        // Trả về Page<AdminCourseGetDTO>
        return new PageImpl<>(dtoList, pageable, resultPage.getTotalElements());
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

    public Page<AdminCourseResultDTO_V2> getCoursesByAccountIdAdmin(int accountId1, int page, int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);
        // Gọi repository để lấy dữ liệu dạng Page<Object[]>
        Page<Object[]> dataPage = courseRepository.findCoursesByAccountIdAdmin(accountId1, pageable);

        // Map dữ liệu từ Object[] sang AdminCourseResultDTO_V2
        return dataPage.map(row -> {
            // Map các giá trị từ Object[] sang AdminCourseResultDTO_V2
            Integer id = (Integer) row[0];                         // id
            String courseTitle = (String) row[1];                  // title
            String description = (String) row[2];                 // description
            String imageUrl = (String) row[3];                    // imageUrl
            String courseOutput = (String) row[4];                // courseOutput
            String language = (String) row[5];                    // language
            String author = (String) row[6];                      // author
            Integer duration = (Integer) row[7];                    // duration
            BigDecimal cost = (BigDecimal) row[8];                // cost
            BigDecimal price = (BigDecimal) row[9];               // price
            LocalDateTime createdAt = convertTimestampToLocalDateTime(row[10]);    // createdAt
            LocalDateTime updatedAt = convertTimestampToLocalDateTime(row[11]);    // updatedAt
            Boolean status = (Boolean) row[12];                   // status
            String type = (String) row[13];                       // type
            LocalDateTime deletedDate = convertTimestampToLocalDateTime(row[14]);  // deletedDate
            Boolean isDeleted = (Boolean) row[15];                // isDeleted
            Integer accountId = (Integer) row[16];                // accountId

            String categoryName3 = (String) row[17];               // categoryName
            Integer categoryId3 = (Integer) row[18];               // categoryId

            String categoryName2 = (String) row[19];               // categoryName
            Integer categoryId2 = (Integer) row[20];               // categoryId

            String categoryName1 = (String) row[21];               // categoryName
            Integer categoryId1 = (Integer) row[22];               // categoryId
            // Tạo và trả về DTO
            return new AdminCourseResultDTO_V2(
                    id, courseTitle, description, imageUrl, courseOutput, language,
                    author, duration, cost, price, createdAt, updatedAt, status,
                    type, deletedDate, isDeleted, accountId, categoryName3, categoryId3, categoryName2, categoryId2, categoryName1, categoryId1
            );
        });
    }


    public Page<AdminCourseResultDTO_V2> getAllCoursesAdmin(int page, int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để lấy dữ liệu dạng Page<Object[]>
        Page<Object[]> dataPage = courseRepository.findAllCoursesResult(pageable);

        // Map dữ liệu từ Object[] sang AdminCourseResultDTO_V2
        return dataPage.map(row -> {
            // Map các giá trị từ Object[] sang AdminCourseResultDTO_V2
            Integer id = (Integer) row[0];                         // id
            String courseTitle = (String) row[1];                  // title
            String description = (String) row[2];                 // description
            String imageUrl = (String) row[3];                    // imageUrl
            String courseOutput = (String) row[4];                // courseOutput
            String language = (String) row[5];                    // language
            String author = (String) row[6];                      // author
            Integer duration = (Integer) row[7];                    // duration
            BigDecimal cost = (BigDecimal) row[8];                // cost
            BigDecimal price = (BigDecimal) row[9];               // price
            LocalDateTime createdAt = convertTimestampToLocalDateTime(row[10]);    // createdAt
            LocalDateTime updatedAt = convertTimestampToLocalDateTime(row[11]);    // updatedAt
            Boolean status = (Boolean) row[12];                   // status
            String type = (String) row[13];                       // type
            LocalDateTime deletedDate = convertTimestampToLocalDateTime(row[14]);  // deletedDate
            Boolean isDeleted = (Boolean) row[15];                // isDeleted
            Integer accountId = (Integer) row[16];                // accountId

            String categoryName3 = (String) row[17];               // categoryName
            Integer categoryId3 = (Integer) row[18];               // categoryId

            String categoryName2 = (String) row[19];               // categoryName
            Integer categoryId2 = (Integer) row[20];               // categoryId

            String categoryName1 = (String) row[21];               // categoryName
            Integer categoryId1 = (Integer) row[22];               // categoryId

            return new AdminCourseResultDTO_V2(
                    id, courseTitle, description, imageUrl, courseOutput, language,
                    author, duration, cost, price, createdAt, updatedAt, status,
                    type, deletedDate, isDeleted, accountId, categoryName3, categoryId3, categoryName2, categoryId2, categoryName1, categoryId1
            );
        });
    }

//    public Page<AdminCourseDTOList> getAllCoursesAdminSearch(Integer categoryId1, Integer categoryId2, Integer categoryId3, String searchTerm, int page, int size) {
//        // Tạo đối tượng Pageable để phân trang
//        Pageable pageable = PageRequest.of(page, size);
//
//        // Gọi repository để lấy dữ liệu dạng Page<Object[]>
//        Page<Object[]> dataPage = courseRepository.findAllCoursesResultSearch(categoryId1, categoryId2, categoryId3, searchTerm, pageable);
//
//        // Map dữ liệu từ Object[] sang AdminCourseResultDTO_V2
//        return dataPage.map(row -> {
//            // Map các giá trị từ Object[] sang AdminCourseResultDTO_V2
//            Integer id = (Integer) row[0];                         // id
//            String courseTitle1 = (String) row[1];                  // title
//            String description = (String) row[2];                 // description
//            String imageUrl = (String) row[3];                    // imageUrl
//            String courseOutput = (String) row[4];                // courseOutput
//            String language = (String) row[5];                    // language
//            String author1 = (String) row[6];                      // author
//            Integer duration = (Integer) row[7];                    // duration
//            BigDecimal cost = (BigDecimal) row[8];                // cost
//            BigDecimal price = (BigDecimal) row[9];               // price
//            LocalDateTime createdAt = convertTimestampToLocalDateTime(row[10]);    // createdAt
//            LocalDateTime updatedAt = convertTimestampToLocalDateTime(row[11]);    // updatedAt
//            Boolean status = (Boolean) row[12];                   // status
//            String type = (String) row[13];                       // type
//            LocalDateTime deletedDate = convertTimestampToLocalDateTime(row[14]);  // deletedDate
//            Boolean isDeleted = (Boolean) row[15];                // isDeleted
//            Integer accountIdData = (Integer) row[16];                // accountId
//
//            String categoryName3 = (String) row[17];               // categoryName
//            Integer categoryId3Data = (Integer) row[18];               // categoryId
//
//            String categoryName2 = (String) row[19];               // categoryName
//            Integer categoryId2Data = (Integer) row[20];               // categoryId
//
//            String categoryName1 = (String) row[21];               // categoryName
//            Integer categoryId1Data = (Integer) row[22];               // categoryId
//
//            Long countStudent = (Long) row[23];
//            return new AdminCourseDTOList(
//                    id, courseTitle1, description, imageUrl, courseOutput, language,
//                    author1, duration, cost, price, createdAt, updatedAt, status,
//                    type, deletedDate, isDeleted, accountIdData, categoryName3, categoryId3Data, categoryName2, categoryId2Data, categoryName1, categoryId1Data, countStudent
//            );
//        });
//    }

    public Page<AdminCourseDTOList> getAllCoursesAdmin(Integer categoryId, String searchTerm, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> dataPage = courseRepository.findCoursesAdmin(categoryId, searchTerm, pageable);
        return convertToDTO(dataPage);
    }

    public Page<AdminCourseDTOList> convertToDTO(Page<Course> coursePage) {
        List<AdminCourseDTOList> dtoList = coursePage.getContent().stream()
                .map(course -> {
                    AdminCourseDTOList dto = new AdminCourseDTOList(
                            course.getId(),
                            course.getTitle(),
                            course.getDescription(),
                            course.getImage_url(),
                            course.getCourseOutput(),
                            course.getLanguage(),
                            course.getAuthor(),
                            course.getDuration(),
                            course.getCost(),
                            course.getPrice(),
                            course.getCreatedAt(),
                            course.getUpdatedAt(),
                            course.getStatus(),
                            course.getType(),
                            course.getDeletedDate(),
                            course.isDeleted(),
                            course.getAccount().getId(),
                            course.getCategory().getName(),
                            course.getCategory().getId(),
                            course.getCategory().getParentCategory().getName(),
                            course.getCategory().getParentCategory().getId(),
                            course.getCategory().getParentCategory().getParentCategory().getName(),
                            course.getCategory().getParentCategory().getParentCategory().getId(),
                            Long.valueOf(course.getEnrolledCourses().size()),
                            course.getLevel()
                    );

                    // Tính toán discountStatus
                    Optional<Course_Discount> courseDiscountOpt = courseDiscountRepository.findByCourseId(course.getId());
                    if (courseDiscountOpt.isPresent() && courseDiscountOpt.get().getDiscount() != null &&
                            courseDiscountOpt.get().getDiscount().getStatus() == DiscountStatus.ACTIVE) {
                        dto.setDiscountStatus(true);
                    } else {
                        dto.setDiscountStatus(false);
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, coursePage.getPageable(), coursePage.getTotalElements());
    }

    public List<AdminCourseResultDTO_V2> getAllCoursesList() {
        // Gọi repository để lấy dữ liệu
        List<Object[]> dataPage = courseRepository.findAllCoursesResult();

        // Chuyển đổi dữ liệu từ Object[] sang AdminCourseResultDTO_V2
        return dataPage.stream().map(row -> {
            Integer id = (Integer) row[0];
            String courseTitle = (String) row[1];
            String description = (String) row[2];
            String imageUrl = (String) row[3];
            String courseOutput = (String) row[4];
            String language = (String) row[5];
            String author = (String) row[6];
            Integer duration = (Integer) row[7];
            BigDecimal cost = (BigDecimal) row[8];
            BigDecimal price = (BigDecimal) row[9];
            LocalDateTime createdAt = convertTimestampToLocalDateTime(row[10]);
            LocalDateTime updatedAt = convertTimestampToLocalDateTime(row[11]);
            Boolean status = (Boolean) row[12];
            String type = (String) row[13];
            LocalDateTime deletedDate = convertTimestampToLocalDateTime(row[14]);
            Boolean isDeleted = (Boolean) row[15];
            Integer accountId = (Integer) row[16];

            // Danh mục cấp 3
            String categoryName3 = (String) row[17];
            Integer categoryId3 = (Integer) row[18];

            // Danh mục cấp 2
            String categoryName2 = (String) row[19];
            Integer categoryId2 = (Integer) row[20];

            // Danh mục cấp 1
            String categoryName1 = (String) row[21];
            Integer categoryId1 = (Integer) row[22];

            // Trả về đối tượng DTO
            return new AdminCourseResultDTO_V2(
                    id, courseTitle, description, imageUrl, courseOutput, language,
                    author, duration, cost, price, createdAt, updatedAt, status,
                    type, deletedDate, isDeleted, accountId,
                    categoryName3, categoryId3, categoryName2, categoryId2, categoryName1, categoryId1
            );
        }).collect(Collectors.toList());
    }

    //    Dùng để làm danh mục
    public List<CourseForListAdminDTO> getAllCoursesListSimple() {

        List<Course> data = courseRepository.findCourses();
        List<CourseForListAdminDTO> courseForListAdminDTOList = new ArrayList<>();
        for (Course item : data) {
            CourseForListAdminDTO course = new CourseForListAdminDTO();
            course = CourseMapper.toDTOForListAdmin(item);
            courseForListAdminDTOList.add(course);
        }
        return courseForListAdminDTOList;
    }

    public Page<AdminCourseOfDiscount> getCoursesWithDiscounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Fetch raw data
        Page<Object[]> rawData = courseRepository.getCourseofDiscount(pageable);

        // Map raw data to DTO
        Page<AdminCourseOfDiscount> result = rawData.map(data -> {
            Integer id = (Integer) data[0];
            String coursesTitle = (String) data[1];
            String duration = (String) data[2];
            Double price = (data[3] != null) ? ((Number) data[3]).doubleValue() : null;
            Double cost = (data[4] != null) ? ((Number) data[4]).doubleValue() : null;

            return new AdminCourseOfDiscount(id, coursesTitle, duration, price, cost);
        });

        return result;
    }

    public Course statusCourseAdmin(int courseId) {
        Optional<Course> accountOpt = courseRepository.findById(courseId);

        if (accountOpt.isPresent()) {
            Course account = accountOpt.get();
            account.setStatus(true);
            return courseRepository.save(account);
        } else {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }

    public Course unstatusCourseAdmin(int courseId) {
        Optional<Course> lessonOpt = courseRepository.findById(courseId);

        if (lessonOpt.isPresent()) {
            Course lesson = lessonOpt.get();
            lesson.setStatus(false);
            return courseRepository.save(lesson);
        } else {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }

    public Map<String, Integer> getFirstChapterAndLesson(Integer courseId) {
        // Lấy chapter đầu tiên
        Optional<Integer> chapterId = chapterRepository.findFirstChapterIdByCourseId(courseId);
        if (chapterId.isPresent()) {
            // Lấy lesson đầu tiên của chương đó
            Optional<Integer> lessonId = lessonRepository.findFirstLessonIdByChapterId(chapterId.get());
            if (lessonId.isPresent()) {
                Map<String, Integer> result = new HashMap<>();
                result.put("chapterId", chapterId.get());
                result.put("lessonId", lessonId.get());
                return result;
            } else {
                throw new ResourceNotFoundException("No lessons found for the first chapter.");
            }
        } else {
            throw new ResourceNotFoundException("No chapters found for the course.");
        }
    }

    public List<AdminCourseFilterDTO_V2> getAllCoursesListAdmin() {
        // Gọi repository để lấy dữ liệu dưới dạng List<Object[]>
        List<Object[]> data = courseRepository.findAllCoursesResultList();

        // Map dữ liệu từ Object[] sang AdminCourseResultDTO
        return data.stream().map(row -> {
            Integer id = (Integer) row[0];                     // id
            String courseTitle = (String) row[1];              // courseTitle
            String duration = (String) row[2];                 // duration
            BigDecimal price = (BigDecimal) row[3];            // price
            BigDecimal cost = (BigDecimal) row[4];              // price
            Boolean status = (Boolean) row[5];                    // status
            Boolean isDeleted = (Boolean) row[6];                 // isDeleted
            String categoryName = (String) row[7];                // categoryName (từ SELECT)
            Integer accountId = (Integer) row[8];
            Integer categoryId = (Integer) row[9];

            // Tạo và trả về AdminCourseResultDTO
            return new AdminCourseFilterDTO_V2(id, courseTitle, duration, price, cost, status, isDeleted, categoryName, accountId, categoryId);
        }).collect(Collectors.toList());  // Chuyển đổi sang List<AdminCourseResultDTO>
    }


    public List<AdminCourseResultDTO> getCoursesByAccountListIdAdmin(int accountId) {
        // Gọi repository để lấy dữ liệu dạng List<Object[]>
        List<Object[]> dataList = courseRepository.findCoursesByAccountIdListAdmin(accountId);

        // Map dữ liệu từ Object[] sang AdminCourseResultDTO
        List<AdminCourseResultDTO> resultList = new ArrayList<>();
        for (Object[] row : dataList) {
            Integer id = row[0] instanceof Integer ? (Integer) row[0] : null; // id
            String courseTitle = row[1] instanceof String ? (String) row[1] : ""; // courseTitle
            String duration = row[2] instanceof String ? (String) row[2] : ""; // duration
            BigDecimal price = row[3] instanceof BigDecimal ? (BigDecimal) row[3] : BigDecimal.ZERO; // price
            BigDecimal cost = row[4] instanceof BigDecimal ? (BigDecimal) row[4] : BigDecimal.ZERO; // price
            Boolean status = row[5] instanceof Boolean ? (Boolean) row[5] : false; // status
            Boolean isDeleted = row[6] instanceof Boolean ? (Boolean) row[6] : false; // isDeleted
            String categoryName = row[7] instanceof String ? (String) row[7] : ""; // categoryName

            // Tạo đối tượng AdminCourseResultDTO và thêm vào danh sách
            AdminCourseResultDTO dto = new AdminCourseResultDTO(id, courseTitle, duration, price, cost, status, isDeleted, categoryName);
            resultList.add(dto);
        }
        return resultList;
    }

    public List<ChapterDTOUserView> getChaptersByCourseIdView(Long courseId) {
        List<Object[]> results = courseRepository.findChaptersByCourseId(courseId);
        List<ChapterDTOUserView> chapterDTOUserViews = new ArrayList<>();
        for (Object[] item : results) {
            Integer chapterId = (Integer) item[0];
            ChapterDTOUserView chapterDTOUserView = new ChapterDTOUserView(chapterId, (String) item[1], (Long) item[2]);

            List<Object[]> listvideo = courseRepository.findVideosByChapterId(chapterId);
            List<VideoDTOUserView> videoDTOUserViewList = new ArrayList<>();
            for (Object[] video : listvideo) {
                VideoDTOUserView viewVideo = new VideoDTOUserView((Integer) video[0], (String) video[1], (Integer) video[2], (Boolean) video[3], (String) video[4]);
                videoDTOUserViewList.add(viewVideo);
            }
            chapterDTOUserView.setVideoDTOUserViewList(videoDTOUserViewList);
            chapterDTOUserViews.add(chapterDTOUserView);
        }
        return chapterDTOUserViews;
    }


    //    public List<LessonDTOUserView> getLessonsWithVideos(Long courseId) {
//        List<Object[]> results = courseRepository.findLessonsAndVideosByCourseId(courseId);
//        Map<Long, LessonDTOUserView> lessonMap = new HashMap<>();
//
//        for (Object[] row : results) {
//            Long lessonId = ((Number) row[0]).longValue();
//            String lessonTitle = (String) row[1];
//            Integer lessonDuration = ((Number) row[2]).intValue();
//            Long videoId = row[3] != null ? ((Number) row[3]).longValue() : null;
//            String videoTitle = (String) row[4];
//            Integer videoDuration = row[5] != null ? ((Number) row[5]).intValue() : null;
//
//            // Nếu bài học chưa tồn tại trong Map, tạo mới
//            LessonDTOUserView lesson = lessonMap.computeIfAbsent(lessonId, id ->
//                    new LessonDTOUserView(lessonId, lessonTitle, lessonDuration)
//            );
//
//            // Thêm video vào bài học
//            if (videoId != null) {
//                lesson.getVideos().add(new VideoDTOUserView(videoId, videoTitle, videoDuration));
//            }
//        }
//
//        // Tính tổng số video và tổng thời lượng video cho từng bài học
//        for (LessonDTOUserView lesson : lessonMap.values()) {
//            lesson.setVideoCount(lesson.getVideos().size());
//            lesson.setTotalVideoDuration(lesson.getVideos().stream()
//                    .mapToInt(VideoDTOUserView::getVideoDuration)
//                    .sum());
//        }
//
//        return new ArrayList<>(lessonMap.values());
//    }
    public List<CourseReportDTO> getCourseReport() {
        return courseRepository.getCourseReport().stream().map(row ->
                new CourseReportDTO(
                        (String) row[0],
                        (Long) row[1],
                        (BigDecimal) row[2],
                        (Boolean) row[3],
                        (String) row[4]
                )
        ).collect(Collectors.toList());
    }

    public boolean checkCourseCompleteness(Integer courseId) {
        List<Object[]> result = courseRepository.checkCourseCompleteness(courseId);

        // Kiểm tra nếu có kết quả và nó thoả mãn điều kiện
        return !result.isEmpty() && result.get(0)[1].equals(result.get(0)[2]) && result.get(0)[1].equals(result.get(0)[3]);
    }

    public Optional<Boolean> getCourseStatusById(Long courseId) {
        return courseRepository.findStatusByCourseId(courseId);
    }

    public CourseCategoryDTO_ADMIN getCourseCategoryById(Integer courseId) {
        List<Object[]> result = courseRepository.findCategoryByCourseId(courseId);
        CourseCategoryDTO_ADMIN categoryDTO = new CourseCategoryDTO_ADMIN();
        for (Object[] item : result) {
            Integer categoryId = (Integer) item[0];  // Ép kiểu sang Long cho ID
            String categoryName = (String) item[1];
            categoryDTO.setCategoryId(categoryId);
            categoryDTO.setCategoryName(categoryName);
            break;
        }
        return categoryDTO;
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminCourseDTORestoreList> getDeletedCourses(Pageable pageable) {
        Page<Object[]> resultPage = courseRepository.findDeletedCourses(pageable);
        List<AdminCourseDTORestoreList> courseDTOList = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminCourseDTORestoreList dto = new AdminCourseDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setAuthor((String) result[1]);
            dto.setCost((BigDecimal) result[2]);
            dto.setCourseOutput((String) result[3]);
            LocalDateTime createdAt = convertTimestampToLocalDateTime(result[4]);
            dto.setCreatedAt(createdAt);
            LocalDateTime deleteDate = convertTimestampToLocalDateTime(result[5]);
            dto.setDeletedDate(deleteDate);
            dto.setDescription((String) result[6]);
            dto.setDuration((Integer) result[7]);
            dto.setImageUrl((String) result[8]);
            dto.setIsDeleted((Boolean) result[9]);
            dto.setLanguage((String) result[10]);
            dto.setPrice((BigDecimal) result[11]);
            dto.setStatus((Boolean) result[12]);
            dto.setCoursesTitle((String) result[13]);
            dto.setType((String) result[14]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[15]);
            dto.setUpdatedAt(updateAt);
            dto.setCourseCategoryId((Integer) result[16]);
            dto.setAccountId((Integer) result[17]);
            courseDTOList.add(dto);
        }

        return new PageImpl<>(courseDTOList, pageable, resultPage.getTotalElements());
    }

    public List<AdminCourseDTORestoreList> getNoDeletedCoursesList() {
        List<Object[]> resultPage = courseRepository.findNoDeletedCoursesList();
        List<AdminCourseDTORestoreList> courseDTOList = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminCourseDTORestoreList dto = new AdminCourseDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setAuthor((String) result[1]);
            dto.setCost((BigDecimal) result[2]);
            dto.setCourseOutput((String) result[3]);
            LocalDateTime createdAt = convertTimestampToLocalDateTime(result[4]);
            dto.setCreatedAt(createdAt);
            LocalDateTime deleteDate = convertTimestampToLocalDateTime(result[5]);
            dto.setDeletedDate(deleteDate);
            dto.setDescription((String) result[6]);
            dto.setDuration((Integer) result[7]);
            dto.setImageUrl((String) result[8]);
            dto.setIsDeleted((Boolean) result[9]);
            dto.setLanguage((String) result[10]);
            dto.setPrice((BigDecimal) result[11]);
            dto.setStatus((Boolean) result[12]);
            dto.setCoursesTitle((String) result[13]);
            dto.setType((String) result[14]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[15]);
            dto.setUpdatedAt(updateAt);
            dto.setCourseCategoryId((Integer) result[16]);
            dto.setAccountId((Integer) result[17]);
            courseDTOList.add(dto);
        }

        return courseDTOList;
    }

    //LAy thong tin khoa học theo yeu cau
    public List<AdminCourseDTORestoreList> getCoursesListPublicQuery(String author, String title, String language, String type, String price) {
        List<Object[]> resultPage = courseRepository.findCoursesListQuery(author, title, language, price, type);
        List<AdminCourseDTORestoreList> courseDTOList = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminCourseDTORestoreList dto = new AdminCourseDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setAuthor((String) result[1]);
            dto.setCost((BigDecimal) result[2]);
            dto.setCourseOutput((String) result[3]);
            LocalDateTime createdAt = convertTimestampToLocalDateTime(result[4]);
            dto.setCreatedAt(createdAt);
            LocalDateTime deleteDate = convertTimestampToLocalDateTime(result[5]);
            dto.setDeletedDate(deleteDate);
            dto.setDescription((String) result[6]);
            dto.setDuration((Integer) result[7]);

            dto.setIsDeleted((Boolean) result[8]);
            dto.setLanguage((String) result[9]);
            dto.setPrice((BigDecimal) result[10]);
            dto.setStatus((Boolean) result[11]);
            dto.setCoursesTitle((String) result[12]);
            dto.setType((String) result[13]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[14]);
            dto.setUpdatedAt(updateAt);
            dto.setCourseCategoryId((Integer) result[15]);
            dto.setAccountId((Integer) result[16]);
            courseDTOList.add(dto);
        }

        return courseDTOList;
    }


    public Page<AdminCourseDTORestoreList> getDeletedCoursesSearch(String courseTitle, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<Object[]> results = courseRepository.searchCoursesByCourseTitleAndDeleteDate(courseTitle, deletedDate, pageable);
        if (courseTitle != null && !courseTitle.isEmpty() && deletedDate != null && !deletedDate.isEmpty()) {
            results = courseRepository.searchCoursesByCourseTitleAndDeleteDate(courseTitle, deletedDate, pageable);
        } else if (courseTitle != null && !courseTitle.isEmpty()) {
            results = courseRepository.searchCoursesByTitle(courseTitle, pageable);
        } else if (deletedDate != null && !deletedDate.isEmpty()) {
            results = courseRepository.searchCoursesByDeletedDate(deletedDate, pageable);
        }
        List<AdminCourseDTORestoreList> courseDTOList = new ArrayList<>();
        for (Object[] result : results) {
            AdminCourseDTORestoreList dto = new AdminCourseDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setAuthor((String) result[1]);
            dto.setCost((BigDecimal) result[2]);
            dto.setCourseOutput((String) result[3]);
            LocalDateTime createdAt = convertTimestampToLocalDateTime(result[4]);
            dto.setCreatedAt(createdAt);
            LocalDateTime deleteDate = convertTimestampToLocalDateTime(result[5]);
            dto.setDeletedDate(deleteDate);
            dto.setDescription((String) result[6]);
            dto.setDuration((Integer) result[7]);
            dto.setImageUrl((String) result[8]);
            dto.setIsDeleted((Boolean) result[9]);
            dto.setLanguage((String) result[10]);
            dto.setPrice((BigDecimal) result[11]);
            dto.setStatus((Boolean) result[12]);
            dto.setCoursesTitle((String) result[13]);
            dto.setType((String) result[14]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[15]);
            dto.setUpdatedAt(updateAt);
            dto.setCourseCategoryId((Integer) result[16]);
            dto.setAccountId((Integer) result[17]);
            courseDTOList.add(dto);
        }

        return new PageImpl<>(courseDTOList, pageable, results.getTotalElements());
    }

    public Course updateRestoreCourse(AdminCourseDTORestoreList adminCourseDTORestoreList) {
        Optional<Course> accountOptional = courseRepository.findById(adminCourseDTORestoreList.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + adminCourseDTORestoreList.getId());
        } else {
            Course account = accountOptional.get();
            account.setDeleted(false);
            return courseRepository.save(account);
        }
    }

    public void deleteRestoreCourse(AdminCourseDTORestoreList accountDetailsDTOV2) {
        Optional<Course> accountOptional = courseRepository.findById(accountDetailsDTOV2.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + accountDetailsDTOV2.getId());
        } else {
            courseRepository.delete(accountOptional.get());
        }
    }


    public Page<CourseDTOUserPublic> getCoursesPublic(String type, String keyword,
                                                      List<Integer> categoryIds, Integer accountId,
                                                      Pageable pageable) {
        Page<Course> courses = null;

        switch (type.toLowerCase()) {
            case "popular":
                courses = courseRepository.findPopularCourses(keyword, pageable); // Lọc phổ biến với ít nhất 100 học viên
                break;
            case "discount":
                courses = courseRepository.findDiscountCourses(keyword, pageable); // Giảm giá (price < cost)
                break;
            case "category":
                courses = courseRepository.findByTitleAndCategory(
                        categoryIds, keyword, pageable); // Lọc theo nhiều danh mục
                break;
            default:
                courses = courseRepository.findByTitleAndCategory(
                        categoryIds, keyword, pageable); // Lọc theo nhiều danh mục
        }

        return courses.map(course -> convertToDTO(course, accountId));
    }

    // Chuyển đổi Course thành CourseDTOUserPublic
    private CourseDTOUserPublic convertToDTO(Course course, Integer accountId) {
        CourseDTOUserPublic dto = new CourseDTOUserPublic();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setImageUrl(course.getImage_url());
        dto.setDuration(course.getDuration());
        dto.setCourseOutput(course.getCourseOutput());
        dto.setLanguage(course.getLanguage());
        dto.setType(course.getType());
        dto.setStatus(course.getStatus());
        dto.setCreatedAt(course.getCreatedAt().toString());
        dto.setUpdatedAt(course.getUpdatedAt().toString());
        dto.setDeletedDate(course.getDeletedDate() != null ? course.getDeletedDate().toString() : null);
        dto.setDeleted(course.isDeleted());
        dto.setAuthor(course.getAuthor());
        dto.setCategoryName(course.getCategory().getName());
        dto.setAccountId(course.getAccount().getId() + "");
        dto.setCourseCategoryId(course.getCategory().getId() + "");
        dto.setLessonCount(course.getLessons().size());
        dto.setStudentCount(course.getEnrolledCourses().size());
        dto.setItemCountReview(course.getReviews().size());

        Double averageRating = courseRepository.findAverageRatingByCourseId(course.getId());

        double roundedRating = averageRating != null
                ? BigDecimal.valueOf(averageRating).setScale(1, RoundingMode.UP).doubleValue()
                : 0.0;

        dto.setRating(roundedRating);


        dto.setCost(course.getCost());
        dto.setPrice(course.getPrice());

        if (accountId != null) {
            boolean isPurchased = course.getEnrolledCourses().stream()
                    .anyMatch(te -> te.getAccount().getId() == accountId);
            dto.setPurchased(isPurchased);
        } else {
            dto.setPurchased(false);
        }


        Optional<Course_Discount> courseDiscountOpt = courseDiscountRepository.findByCourseId(course.getId());

        if (courseDiscountOpt.isPresent() && courseDiscountOpt.get().getDiscount() != null && courseDiscountOpt.get().getDiscount().getStatus() == DiscountStatus.ACTIVE && course.getType().equals("FEE")) {
            int intValue = courseDiscountOpt.get().getDiscount().getDiscountValue().intValue();
            dto.setPercentDiscount(intValue);
        } else {
            dto.setPercentDiscount(0);
        }

//        dto.setPercentDiscount(10);
        dto.setLevel(course.getLevel());
        return dto;
    }
}
