package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminAddCourseDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminCourseGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminCourseOfDiscount;
import com.example.hotrohoctapbackend.DTO.Admin.AdminCourseResultDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseDTO_User_Profile;
import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseInfoDetailDTO_User;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.service.CourseService;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrolledCourseService enrolledCoursesService;

    @GetMapping("/{id}")
    public CourseDetailDTO getCourseById(@PathVariable Integer id) {
        return courseService.getCourseDetailById(id);
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
            @RequestParam List<Integer> courseCategoryIds,
            Pageable pageable) {
        Page<CourseDTO> coursesPage = courseService.getCoursesByCategories(courseCategoryIds, pageable);

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

    //Admin
    @PostMapping("/add-course")
    public ResponseEntity<Course> addCourse(@RequestBody AdminAddCourseDTO courseDTO) {
        Course createdCourse = courseService.addCourse(courseDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }
    @PutMapping("/update-course/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody AdminAddCourseDTO courseDTO) {
        try {
            Course updatedCourse = courseService.editCourse(courseId, courseDTO);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getall")
    public ResponseEntity<Page<AdminCourseGetDTO>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminCourseGetDTO> courses = courseService.getCoursesWithCategoryAdmin(page, size);
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
    public Page<AdminCourseResultDTO> getCoursesByAccountId(
            @PathVariable("accountId") int accountId,
            @RequestParam(value = "page", defaultValue = "0") int page, // Giá trị mặc định là 0
            @RequestParam(value = "size", defaultValue = "10") int size // Giá trị mặc định là 10
    ) {
        return courseService.getCoursesByAccountIdAdmin(accountId, page, size);
    }
    @GetMapping("/getallresult")
    public Page<AdminCourseResultDTO> getAllCourses(
            @RequestParam(value = "page", defaultValue = "0") int page, // Giá trị mặc định là 0
            @RequestParam(value = "size", defaultValue = "10") int size // Giá trị mặc định là 10
    ) {
        return courseService.getAllCoursesAdmin(page, size);
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
}
