package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.ExistProgressPassDTO_USER;
import com.example.hotrohoctapbackend.DTO.User.ProgressDTO_User;
import com.example.hotrohoctapbackend.DTO.User.UserGetCheckProgress;
import com.example.hotrohoctapbackend.entity.Progress;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import com.example.hotrohoctapbackend.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @GetMapping("/{courseId}/progress/{accountId}")
    public ResponseEntity<List<ProgressDTO_User>> getProgress(@PathVariable Integer courseId, @PathVariable Integer accountId) {
        List<ProgressDTO_User> progressList = progressService.getProgressByCourseAndAccount(courseId, accountId);
        return ResponseEntity.ok(progressList);
    }
//    @PostMapping("/add")
//    public ResponseEntity<Progress> addProgress(@RequestBody ProgressDTO_User progressDTO) {
//        Progress progress = progressService.addOrUpdateProgress(progressDTO);
//        return ResponseEntity.ok(progress);
//    }


    @PostMapping("/check-pass")
    public ResponseEntity<Boolean> checkPassResult(@RequestBody ExistProgressPassDTO_USER dto) {
        boolean result = progressService.checkPassResult_User(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculateProgress(
            @RequestParam Integer accountId,
            @RequestParam Integer courseId) {
        Double progress = progressService.calculateProgress(accountId, courseId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/calculateADMIN")
    public ResponseEntity<Double> calculateProgressAdmin(
            @RequestParam Integer accountId,
            @RequestParam Integer courseId) {
        Double progress = progressService.calculateProgress(accountId, courseId);
        return ResponseEntity.ok(progress);
    }


    @PostMapping("/add")
    public ResponseEntity<UserGetCheckProgress> addProgress(
            @RequestParam int accountId,
            @RequestParam int courseId,
            @RequestParam int chapterId,
            @RequestParam int lessonId) {
        UserGetCheckProgress progress = progressService.createProgress(accountId, courseId, chapterId, lessonId);
        if (progress != null) {
            String message = enrolledCourseService.updateStatus(accountId, courseId);
            return new ResponseEntity<>(progress, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
