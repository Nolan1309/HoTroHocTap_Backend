package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.service.services.ActivityHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/activity")
public class ActivityHistoryController {
    @Autowired
    private ActivityHistoryService activityHistoryService;

    
}
