package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_View_User;
import com.example.hotrohoctapbackend.exception.ErrorResponse;
import com.example.hotrohoctapbackend.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/test-results")
public class TestResultController {
    @Autowired
    private TestResultService testResultService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrUpdateTestResult(@RequestBody TestResultDTO_User testResultDTOUser) {
        try {
            TestResultDTO_User testResult = testResultService.addTestResult(testResultDTOUser);
            return new ResponseEntity<>(testResult, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    System.currentTimeMillis()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    System.currentTimeMillis()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Lỗi :" + e.getMessage(),
                    System.currentTimeMillis()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/view-user")
    public ResponseEntity<Page<TestResultDTO_View_User>> getTestResultsByAccountIdUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Integer accountId,
            @RequestParam(required = false) String search) { // Nhận tham số `search`
        Page<TestResultDTO_View_User> results = testResultService.getTestResultsByAccountId(page, size, accountId, search);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/average-score")
    public ResponseEntity<Double> getAverageScore(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        Double averageScore = testResultService.getAverageScoreUser(accountId, courseId);
        return ResponseEntity.ok(averageScore);
    }

    @GetMapping("/pass-rate")
    public ResponseEntity<Double> getPassRate(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        Double passRate = testResultService.getPassRateUser(accountId, courseId);
        return ResponseEntity.ok(passRate);
    }

    @GetMapping("/result/detail")
    public ResponseEntity<List<Object>> getTestResults(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        List<Object> results = testResultService.getTestResultsUser(accountId, courseId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/result-count")
    public ResponseEntity<List<Object[]>> countResultsGroupedByResult(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        List<Object[]> results = testResultService.countResultsGroupedByResultUser(accountId, courseId);
        return ResponseEntity.ok(results);
    }

    //ADMIN
    @GetMapping("/average-scoreADMIN")
    public ResponseEntity<Double> getAverageScoreAdmin(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        Double averageScore = testResultService.getAverageScoreUser(accountId, courseId);
        return ResponseEntity.ok(averageScore);
    }

    @GetMapping("/pass-rateADMIN")
    public ResponseEntity<Double> getPassRateADMIN(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        Double passRate = testResultService.getPassRateUser(accountId, courseId);
        return ResponseEntity.ok(passRate);
    }

    @GetMapping("/result/detailADMIN")
    public ResponseEntity<List<Object>> getTestResultsADMIN(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        List<Object> results = testResultService.getTestResultsUser(accountId, courseId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/result-countADMIN")
    public ResponseEntity<List<Object[]>> countResultsGroupedByResultADMIN(
            @RequestParam Long accountId,
            @RequestParam Long courseId) {
        List<Object[]> results = testResultService.countResultsGroupedByResultUser(accountId, courseId);
        return ResponseEntity.ok(results);
    }
}
