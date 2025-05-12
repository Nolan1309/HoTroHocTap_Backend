package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.*;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseDTOAdminV3;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseDTOUserPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseForListAdminDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.DTO.User.ChapterDTOUserView;
import com.example.hotrohoctapbackend.DTO.User.CourseDTO_User_Profile;
import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseInfoDetailDTO_User;
import com.example.hotrohoctapbackend.DTO.User.LessonDTOUserView;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CourseService;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrolledCourseService enrolledCoursesService;
    @Autowired
    private ImageKitService imageKitService;

    @GetMapping()
    public ResponseEntity<Page<CourseDTO>> getAllCourses(Pageable pageable) {
        Page<CourseDTO> coursesPage = courseService.getAllCourse(pageable);
        if (coursesPage.hasContent()) {
            return ResponseEntity.ok(coursesPage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/public/filter")
    public ApiResponse<Page<CourseDTOUserPublic>> getCourses(
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<CourseDTOUserPublic> courses = courseService.getCoursesPublic(type, keyword, categoryIds, accountId, pageable);
        return new ApiResponse<>(200, "Success", courses);
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseDTOAdminV3> getCourseById(@PathVariable int id,
                                                       @RequestParam(required = false) Integer accountId) {
        CourseDTOAdminV3 courseDTO = courseService.getCourseById(id, accountId);
        return new ApiResponse<>(200, "Success", courseDTO);
    }

    @GetMapping("/statistics/{courseId}")
    public ResponseEntity<Map<String, Integer>> getCourseStatistics(@PathVariable("courseId") Integer courseId) {
        Map<String, Integer> statistics = courseService.getCourseStatistics(courseId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/top6")
    public List<CourseDTO> getLevel1Categories() {
        return courseService.getTop_6_CoursesWithDetails();
    }


    @GetMapping("/category/{courseCategoryId}")
    public ResponseEntity<Page<CourseDTO>> getCoursesByCategory(@PathVariable Integer courseCategoryId, Pageable pageable) {
        Page<CourseDTO> coursesPage = courseService.getCoursesByCategory(courseCategoryId, pageable);
        if (coursesPage.hasContent()) {
            return ResponseEntity.ok(coursesPage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Page<CourseDTO>> getCoursesByCategories(
            @RequestParam Integer categoryId,
            @RequestParam Integer categoryIds,
            Pageable pageable) {
        Page<CourseDTO> coursesPage = courseService.getCoursesByCategories(categoryId, categoryIds, pageable);

        // Trả về danh sách rỗng và mã 200 OK thay vì 404 Not Found
        return ResponseEntity.ok(coursesPage);
    }

    @GetMapping("/check-type/{id}")
    public ResponseEntity<String> getCourseTypeById(@PathVariable("id") int id) {
        String courseType = courseService.getCourseTypeById(id);
        return ResponseEntity.ok(courseType); // Trả về type của khóa học
    }

    @GetMapping("/account/enrolled/{accountId}")
    public Page<CourseDTO_User_Profile> getEnrolledCourses(
            @PathVariable("accountId") Integer accountId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return courseService.getCoursesByAccountId(accountId, page, size);
    }

    @GetMapping("/accountADMIN/enrolled/{accountId}")
    public Page<CourseDTO_User_Profile> getEnrolledCoursesADMIN(
            @PathVariable("accountId") Integer accountId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return courseService.getCoursesByAccountId(accountId, page, size);
    }

    //Section vao hoc
    @GetMapping("/take-course/{courseId}")
    public ResponseEntity<CourseInfoDetailDTO_User> getCourseDetails(@PathVariable Integer courseId) {
        CourseInfoDetailDTO_User courseDetails = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok(courseDetails);
    }

    @PostMapping("/add-course")
    public ResponseEntity<ApiResponse<Course>> addCourse(
            @RequestParam("coursesTitle") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("duration") Integer duration,
            @RequestParam("language") String language,
            @RequestParam("cost") String cost,
            @RequestParam("price") String price,
            @RequestParam("level") String level,
            @RequestParam("courseOutput") String courseOutput,
            @RequestParam("image") MultipartFile image,  // Nhận file hình ảnh
            @RequestParam("courseCategoryId") Integer courseCategoryId,
            @RequestParam("accountId") Integer accountId,
            @RequestParam("type") String type,
            @RequestParam("status") Boolean status) {

        try {
            // Tiến hành lưu file nếu có
            String imageUrl = null;
            if (!image.isEmpty()) {
                imageUrl = imageKitService.uploadFromBytes(image).getUrl(); // Giả sử sử dụng dịch vụ upload hình ảnh
            }

            // Tạo DTO cho khóa học
            AdminAddCourseDTO adminAddCourseDTO = new AdminAddCourseDTO();
            adminAddCourseDTO.setCoursesTitle(title);
            adminAddCourseDTO.setAuthor(author);
            adminAddCourseDTO.setDescription(description);
            adminAddCourseDTO.setDuration(duration);
            adminAddCourseDTO.setLevel(level);
            adminAddCourseDTO.setLanguage(language);
            adminAddCourseDTO.setCost(new BigDecimal(cost));
            adminAddCourseDTO.setPrice(new BigDecimal(price));
            adminAddCourseDTO.setCourseOutput(courseOutput);
            adminAddCourseDTO.setImageUrl(imageUrl);
            adminAddCourseDTO.setType(type);
            adminAddCourseDTO.setStatus(status);
            adminAddCourseDTO.setAccountId(accountId);
            adminAddCourseDTO.setCourseCategoryId(courseCategoryId);
            // Gọi service để thêm khóa học
            Course createdCourse = courseService.addCourse(adminAddCourseDTO);

            // Trả về response với status 201 và khóa học vừa tạo
            ApiResponse<Course> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm khóa học thành công", createdCourse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // Trả về lỗi nếu có vấn đề trong quá trình thêm khóa học
            ApiResponse<Course> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi khi thêm khóa học", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/update-course/{courseId}")
    public ResponseEntity<ApiResponse<Boolean>> updateCourse(
            @PathVariable Integer courseId,
            @RequestParam("coursesTitle") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("duration") Integer duration,
            @RequestParam("language") String language,
            @RequestParam("cost") String cost,
            @RequestParam("level") String level,
            @RequestParam("price") String price,
            @RequestParam("courseOutput") String courseOutput,
            @RequestParam(value = "image", required = false) MultipartFile image, // Nhận file hình ảnh, có thể null
            @RequestParam("courseCategoryId") Integer courseCategoryId,
            @RequestParam("accountId") Integer accountId,
            @RequestParam("type") String type,
            @RequestParam("status") Boolean status) {

        try {
            // Tìm khóa học cần chỉnh sửa từ ID
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Nếu có file hình ảnh mới, tiến hành lưu và lấy URL
            String imageUrl = existingCourse.getImage_url(); // Nếu không có ảnh mới, giữ nguyên URL cũ
            if (image != null && !image.isEmpty()) {
                // Giả sử bạn sử dụng dịch vụ để upload ảnh và lấy URL
                imageUrl = imageKitService.uploadFromBytes(image).getUrl();
            }
            // Gọi service để xử lý logic cập nhật
            Boolean updatedCourse = courseService.updateCourse(courseId, title, author, description, duration, language, cost, price, courseOutput, courseCategoryId, accountId, type, status, imageUrl, existingCourse, level);

            // Trả về phản hồi thành công
            ApiResponse<Boolean> response = new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật khóa học thành công", updatedCourse);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Lỗi nếu không tìm thấy khóa học
            ApiResponse<Boolean> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Khóa học không tồn tại", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Lỗi khi có vấn đề khác
            ApiResponse<Boolean> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi khi cập nhật khóa học", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/getall")
    public ResponseEntity<Page<AdminCourseGetDTO>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminCourseGetDTO> courses = courseService.getCoursesWithCategoryAdmin(page, size);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/getall-list")
    public ResponseEntity<List<AdminCourseFilterDTO_V2>> getAllCoursesListAdmin() {
        List<AdminCourseFilterDTO_V2> courses = courseService.getAllCoursesListAdmin();

        // Kiểm tra nếu không có dữ liệu
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();  // Trả về 204 No Content nếu danh sách trống
        }

        return ResponseEntity.ok(courses);  // Trả về danh sách khóa học với mã trạng thái 200 OK
    }

    @GetMapping("/courses/ofaccount/list/{accountId}")
    public ResponseEntity<List<AdminCourseResultDTO>> getCoursesByAccountIdList(
            @PathVariable("accountId") int accountId
    ) {
        // Gọi service để lấy dữ liệu khóa học mà không phân trang
        List<AdminCourseResultDTO> courses = courseService.getCoursesByAccountListIdAdmin(accountId);

        // Nếu không có khóa học nào, trả về mã lỗi 204 (No Content)
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Trả về danh sách khóa học với mã trạng thái 200 (OK)
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountAdmin(@PathVariable int id) {
        try {
            Course deletedCourse = courseService.deleteCourseAdmin(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeCourseAdmin(@PathVariable int id) {
        try {
            Course deletedCourse = courseService.activeCourseAdmin(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @GetMapping("/courses/ofaccount/{accountId}")
    public Page<AdminCourseResultDTO_V2> getCoursesByAccountId(
            @PathVariable("accountId") int accountId,
            @RequestParam(value = "page", defaultValue = "0") int page, // Giá trị mặc định là 0
            @RequestParam(value = "size", defaultValue = "10") int size // Giá trị mặc định là 10
    ) {
        return courseService.getCoursesByAccountIdAdmin(accountId, page, size);
    }

    @GetMapping("/getallresult")
    public Page<AdminCourseResultDTO_V2> getAllCourses(
            @RequestParam(value = "page", defaultValue = "0") int page, // Giá trị mặc định là 0
            @RequestParam(value = "size", defaultValue = "10") int size // Giá trị mặc định là 10
    ) {
        return courseService.getAllCoursesAdmin(page, size);
    }

    @GetMapping("/all-get-result-search")
    public Page<AdminCourseDTOList> getAllCoursesSearch(
            @RequestParam(required = false) Integer categoryId1,
            @RequestParam(required = false) Integer categoryId2,
            @RequestParam(required = false) Integer categoryId3,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page, // Giá trị mặc định là 0
            @RequestParam(value = "size", defaultValue = "10") int size // Giá trị mặc định là 10
    ) {
//        return courseService.getAllCoursesAdminSearch(categoryId1, categoryId2, categoryId3, searchTerm, page, size);
        return courseService.getAllCoursesAdmin(categoryId3, searchTerm, page, size);
    }

    @GetMapping("/get-all-result-list")
    public List<AdminCourseResultDTO_V2> getAllCoursesList() {
        return courseService.getAllCoursesList();
    }

    @GetMapping("/get-all-result-list-course")
    public ApiResponse<List<CourseForListAdminDTO>> getAllCoursesListForCategory() {
        ApiResponse<List<CourseForListAdminDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách khóa học thành công", courseService.getAllCoursesListSimple());
        return response;
    }

    @GetMapping("/courses/discounts")
    public Page<AdminCourseOfDiscount> getCoursesWithDiscounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return courseService.getCoursesWithDiscounts(page, size);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> statusAccountAdmin(@PathVariable int id) {
        try {
            Course deletedCourse = courseService.statusCourseAdmin(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @PutMapping("/unstatus/{id}")
    public ResponseEntity<?> unstatusCourseAdmin(@PathVariable int id) {
        try {
            Course deletedCourse = courseService.unstatusCourseAdmin(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @GetMapping("/{courseId}/first-chapter-lesson")
    public ResponseEntity<Map<String, Integer>> getFirstChapterAndLesson(@PathVariable Integer courseId) {
        Map<String, Integer> result = courseService.getFirstChapterAndLesson(courseId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{courseId}/lessons-view")
    public List<ChapterDTOUserView> getChaptersWithVideos(@PathVariable Long courseId) {
        return courseService.getChaptersByCourseIdView(courseId);
    }

    @GetMapping("/report-admin")
    public List<CourseReportDTO> getCourseReport() {
        return courseService.getCourseReport();
    }

    @GetMapping("/courses/{courseId}/check-completion")
    public boolean checkCourseCompletion(@PathVariable Integer courseId) {
        return courseService.checkCourseCompleteness(courseId);
    }

    @GetMapping("/courses/status")
    public ResponseEntity<Boolean> getCourseStatus(@RequestParam Long courseId) {
        Optional<Boolean> status = courseService.getCourseStatusById(courseId);

        if (status.isPresent()) {
            return ResponseEntity.ok(status.get()); // Trả về status của khóa học
        } else {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy khóa học, trả về lỗi 404
        }
    }

    @GetMapping("/courses/status-user")
    public ResponseEntity<Boolean> getCourseStatusUser(@RequestParam Long courseId) {
        Optional<Boolean> status = courseService.getCourseStatusById(courseId);

        if (status.isPresent()) {
            return ResponseEntity.ok(status.get()); // Trả về status của khóa học
        } else {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy khóa học, trả về lỗi 404
        }
    }

    @GetMapping("/courses/{courseId}/category")
    public ResponseEntity<CourseCategoryDTO_ADMIN> getCourseCategory(@PathVariable Integer courseId) {
        CourseCategoryDTO_ADMIN categoryOpt = courseService.getCourseCategoryById(courseId);

        if (categoryOpt != null) {

            return ResponseEntity.ok(categoryOpt);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/restore/list-all-courses")
    public Page<AdminCourseDTORestoreList> getDeletedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return courseService.getDeletedCourses(pageRequest);
    }

    @GetMapping("/restore-no-delete/list-all-no-courses")
    public List<AdminCourseDTORestoreList> getNoDeletedCourses() {
        return courseService.getNoDeletedCoursesList();
    }

    //COze - Lay all khoa hoc - Ko check
    @GetMapping("/public/course-all")
    public DataExportPublic getCoursesListPublicQuery(@RequestParam(required = false) String author,
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) String language,
                                                      @RequestParam(required = false) String price,
                                                      @RequestParam(required = false) String type

    ) {
        DataExportPublic dataExportPublic = new DataExportPublic();
        dataExportPublic.setData(courseService.getCoursesListPublicQuery(author, title, language, type, price));
        return dataExportPublic;
    }

    @GetMapping("/restore/list-all/search-courses")
    public ResponseEntity<Page<AdminCourseDTORestoreList>> searchAccounts(
            @RequestParam(required = false) String courseTitle,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AdminCourseDTORestoreList> result = courseService.getDeletedCoursesSearch(courseTitle, deletedDate, page, size);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/restore/{courseId}")
    public ResponseEntity<Course> restoreCourse(@PathVariable Integer courseId) {
        AdminCourseDTORestoreList accountDetails = new AdminCourseDTORestoreList();
        accountDetails.setId(courseId);
        Course restoredAccount = courseService.updateRestoreCourse(accountDetails);
        return ResponseEntity.ok(restoredAccount);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer courseId) {
        AdminCourseDTORestoreList accountDetails = new AdminCourseDTORestoreList();
        accountDetails.setId(courseId);
        courseService.deleteRestoreCourse(accountDetails);
        return ResponseEntity.ok("Course permanently deleted.");
    }


}
