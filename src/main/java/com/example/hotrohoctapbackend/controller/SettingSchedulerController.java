package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.SettingDTO;
import com.example.hotrohoctapbackend.entity.SettingScheduler;
import com.example.hotrohoctapbackend.scheduler.NotificationScheduler;
import com.example.hotrohoctapbackend.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/settings")
public class SettingSchedulerController {
    @Autowired
    private SettingService service;
    @Autowired
    private NotificationScheduler notificationScheduler;


    @GetMapping
    public ResponseEntity<List<SettingScheduler>> getAllSettings() {
        return ResponseEntity.ok(service.getAllSettings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettingScheduler> getSettingById(@PathVariable int id) {
        Optional<SettingScheduler> setting = service.getSettingById(id);
        return setting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SettingScheduler>> getSettingsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getSettingsByType(type));
    }

    @PostMapping
    public ResponseEntity<SettingScheduler> createSetting(@RequestBody SettingDTO setting) {
        return ResponseEntity.ok(service.saveSetting(setting));
    }

    @PutMapping("/{id}/activate/{type}")
    public ResponseEntity<Void> activateSetting(@PathVariable int id, @PathVariable String type) {
        service.activateSetting(id, type);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSettingName(@PathVariable int id, @RequestBody Map<String, String> updateData) {
        String name = updateData.get("name");
        service.updateSettingName(id, name);
        notificationScheduler.scheduleDynamicCronJob();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable int id) {
        service.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/update-cron/{id}")
//    public ResponseEntity<Void> updateCronExpression(@PathVariable Integer id,@RequestBody Map<String, String> request) {
//        String cronExpression = request.get("cronExpression");
//
//        SettingScheduler scheduler = service.ge("scheduler")
//                .orElseThrow(() -> new RuntimeException("Scheduler not found"));
//
//        scheduler.setCronExpression(cronExpression);
//        settingSchedulerRepository.save(scheduler);
//
//        return ResponseEntity.ok().build();
//    }
}
