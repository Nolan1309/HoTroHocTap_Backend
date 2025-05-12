package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.ActivityHistoryRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Activity_History;
import com.example.hotrohoctapbackend.enums.ActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class ActivityHistoryService {
    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void saveActivity(int accountId, String activityType, String description) {
        ActivityType activityType1 = ActivityType.valueOf(activityType);
        List<Activity_History> activityHistoryList = activityHistoryRepository
                .findByAccountIdAndActivityType(accountId, activityType1);

        // Kiểm tra nếu danh sách không trống
        if (activityHistoryList.isEmpty()) {
            // Nếu danh sách trống, không có hoạt động nào trước đó
            Activity_History activityHistory = new Activity_History();
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account không tồn tại"));

            activityHistory.setAccount(account);
            ActivityType type = ActivityType.valueOf(activityType);
            activityHistory.setActivityType(type);
            activityHistory.setDescription(description);
            activityHistory.setTimestamp(LocalDateTime.now());

            activityHistoryRepository.save(activityHistory);
            return;  // Kết thúc nếu không có hoạt động trước đó
        }

        // Nếu danh sách không trống, sắp xếp và lấy hoạt động cuối cùng
        activityHistoryList.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        Activity_History lastActivity = activityHistoryList.get(0);
        if (lastActivity != null) {
            LocalDateTime lastActivityTime = lastActivity.getTimestamp();
            LocalDateTime currentTime = LocalDateTime.now();
            long secondsDifference = ChronoUnit.SECONDS.between(lastActivityTime, currentTime);
            if (secondsDifference < 10) {
                return;  // Nếu hoạt động gần đây diễn ra dưới 30 giây, không lưu mới
            }
        }

        // Lưu hoạt động mới nếu điều kiện đã được đáp ứng
        Activity_History activityHistory = new Activity_History();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account không tồn tại"));

        activityHistory.setAccount(account);
        ActivityType type = ActivityType.valueOf(activityType);
        activityHistory.setActivityType(type);
        activityHistory.setDescription(description);
        activityHistory.setTimestamp(LocalDateTime.now());

        activityHistoryRepository.save(activityHistory);
    }

//    public List<Activity_History> getLoginHistory(int accountId) {
//        return activityHistoryRepository.findByAccountIdAndActivityType(accountId, ActivityType.LOGIN);
//    }

    public List<Activity_History> getLoginHistoryInPeriod(int accountId, LocalDateTime startDate) {
        return activityHistoryRepository.findByAccountIdAndActivityTypeAndTimestampBefore(accountId, ActivityType.LOGIN, startDate);
    }

    public int calculateLoginStreak(int accountId) {
        LocalDateTime now = LocalDateTime.now();  // Thời gian hiện tại (ngày đăng nhập đầu tiên)
        List<Activity_History> loginHistory = getLoginHistoryInPeriod(accountId, now);

        // Sắp xếp lịch sử đăng nhập theo thứ tự giảm dần (ngày gần nhất trước)
        Collections.sort(loginHistory, (a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        int streak = 0;
        LocalDateTime lastLogin = now; // Khởi tạo ngày đăng nhập cuối cùng là ngày hiện tại

        // Duyệt qua lịch sử đăng nhập và tính chuỗi
        for (Activity_History activity : loginHistory) {
            // Nếu đây là lần đầu hoặc ngày đăng nhập là ngày tiếp theo liên tiếp
            if (lastLogin.toLocalDate().equals(activity.getTimestamp().toLocalDate().plusDays(1))) {
                streak++;
                lastLogin = activity.getTimestamp();  // Cập nhật lần đăng nhập cuối cùng
            } else if (lastLogin.toLocalDate().equals(activity.getTimestamp().toLocalDate())) {
                streak++;  // Nếu cùng ngày thì cũng tính thêm 1 streak
                
            } else {
                break; // Dừng lại nếu có gián đoạn (ngày không liên tiếp)
            }
        }

        return streak; // Trả về chuỗi đăng nhập liên tiếp
    }

}
