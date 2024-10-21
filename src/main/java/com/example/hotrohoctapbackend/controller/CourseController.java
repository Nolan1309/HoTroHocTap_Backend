package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO_User_Profile;
import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.DTO.CourseInfoDetailDTO_User;
import com.example.hotrohoctapbackend.entity.Course;
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

    //Section vao hoc
    @GetMapping("/take-course/{courseId}")
    public ResponseEntity<CourseInfoDetailDTO_User> getCourseDetails(@PathVariable Integer courseId) {
        CourseInfoDetailDTO_User courseDetails = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok(courseDetails);
    }


}
