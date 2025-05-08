package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.ActivityHistoryRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Activity_History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ActivityHistoryService {
    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void saveActivity(int accountId, String activityType, String description) {
        List<Activity_History> activityHistoryList = activityHistoryRepository
                .findByAccountIdAndActivityType(accountId, activityType);

        // Kiểm tra nếu danh sách không trống
        if (activityHistoryList.isEmpty()) {
            // Nếu danh sách trống, không có hoạt động nào trước đó
            Activity_History activityHistory = new Activity_History();
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account không tồn tại"));

            activityHistory.setAccount(account);
            activityHistory.setActivityType(activityType);
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
        activityHistory.setActivityType(activityType);
        activityHistory.setDescription(description);
        activityHistory.setTimestamp(LocalDateTime.now());

        activityHistoryRepository.save(activityHistory);
    }


}
