package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.ActivityData;
import com.example.hotrohoctapbackend.service.services.ActivityHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ActivityWebSocketController {

    @Autowired
    private ActivityHistoryService activityHistoryService;

    // Xử lý khi người dùng đăng nhập
    @MessageMapping("/login")
    @SendTo("/topic/activities")
    public void handleLogin(ActivityData activityData) {
        activityHistoryService.saveActivity(activityData.getAccountId(), "login", "User logged in");

    }

    // Xử lý khi người dùng click vào video
    @MessageMapping("/video-clicked")
    @SendTo("/topic/activities")
    public void handleVideoClick(ActivityData activityData) {
        activityHistoryService.saveActivity(activityData.getAccountId(), "video_clicked", "User clicked video ID: " + activityData.getVideoId());


    }

    // Xử lý khi người dùng click vào bài kiểm tra
    @MessageMapping("/test-clicked")
    @SendTo("/topic/activities")
    public void handleTestClick(ActivityData activityData) {
        activityHistoryService.saveActivity(activityData.getAccountId(), "test_clicked", "User clicked test ID: " + activityData.getTestId());
    }
}
