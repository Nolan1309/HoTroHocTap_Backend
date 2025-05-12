package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Ranking.RankingDTO;
import com.example.hotrohoctapbackend.enums.PeriodType;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ApiResponse<Page<RankingDTO>> getRankings(
            @RequestParam(value = "periodType", required = false) String periodType,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "ranking", required = false) Integer ranking,
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PeriodType periodType1 = null;
        if (periodType != null) {
            periodType1 = PeriodType.valueOf(periodType);
        }
        Page<RankingDTO> rankings = rankingService.getRankings(periodType1, status, startDate, endDate, ranking, accountName, page, size);
        return new ApiResponse<>(HttpStatus.OK.value(), "Danh sách xếp hạng", rankings);
    }


    @PostMapping("/update-weekly")
    public ApiResponse<String> updateWeekly() {
        try {
            rankingService.updateWeeklyRankings();
            return new ApiResponse<>(HttpStatus.OK.value(), "Điểm tuần đã được cập nhật thành công.", null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/update-weekly-ranking")
    public ApiResponse<String> updateWeeklyRanking() {
        try {
            rankingService.updateWeeklyRanking();
            return new ApiResponse<>(HttpStatus.OK.value(), "Vị thứ tuần đã được cập nhật thành công.", null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    /**
     * API để cập nhật thứ hạng hàng ngày cho tất cả người dùng.
     * Endpoint này sẽ tính lại xếp hạng hàng ngày của người dùng trong ngày hiện tại.
     */
    @PostMapping("/update-daily-ranking")
    public ApiResponse<String> updateDailyRanking() {
        try {
            rankingService.calculateAndUpdateDailyRanking();
            return new ApiResponse<>(HttpStatus.OK.value(), "Vị thứ ngày đã được cập nhật thành công.", null);
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ xảy ra
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/add-daily-ranking")
    public ApiResponse<String> addDailyRanking() {
        try {
            // Gọi service để thêm bản ghi DAILY mới nếu chưa có
            rankingService.addDailyRankingIfNotExists();
            return new ApiResponse<>(HttpStatus.OK.value(), "Bản ghi DAILY đã được thêm cho ngày hôm nay.", null);
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ xảy ra
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi trong quá trình thêm bản ghi DAILY.", null);
        }
    }

    @PostMapping("/add-monthly-ranking")
    public ApiResponse<String> addMonthlyRanking() {
        try {
            // Gọi service để thêm bản ghi MONTHLY mới nếu chưa có
            rankingService.addMonthlyRankingIfNotExists();
            return new ApiResponse<>(HttpStatus.OK.value(), "Bản ghi MONTHLY đã được thêm cho tháng hiện tại.", null);
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ xảy ra
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi trong quá trình thêm bản ghi MONTHLY.", null);
        }
    }

    @PostMapping("/update-monthly-ranking")
    public ApiResponse<String> updateMonthlyRanking() {
        try {
            // Gọi service để tính toán và cập nhật thứ hạng cho tháng hiện tại
            rankingService.calculateAndUpdateMonthlyRanking();
            return new ApiResponse<>(HttpStatus.OK.value(), "Vị thứ tháng đã được cập nhật thành công.", null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi trong quá trình cập nhật xếp hạng tháng.", null);
        }
    }
}
