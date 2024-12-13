package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.SettingDTO;
import com.example.hotrohoctapbackend.dao.SettingRepository;
import com.example.hotrohoctapbackend.entity.SettingScheduler;
import com.example.hotrohoctapbackend.scheduler.NotificationScheduler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private NotificationScheduler notificationScheduler;


    public List<SettingScheduler> getAllSettings() {
        return settingRepository.findAll();
    }

    public Optional<SettingScheduler> getSettingById(int id) {
        return settingRepository.findById(id);
    }

    //get all type
    public List<SettingScheduler> getSettingsByType(String type) {
        return settingRepository.findByType(type);
    }

    //save
    public SettingScheduler saveSetting(SettingDTO setting) {

        SettingScheduler settingScheduler = new SettingScheduler();
        settingScheduler.setCheck(setting.isCheck());
        settingScheduler.setType(setting.getType());
        settingScheduler.setName(setting.getName());
        return settingRepository.save(settingScheduler);
    }

    public void updateSettingName(int id, String name) {
        Optional<SettingScheduler> setting = settingRepository.findById(id);
        if (setting.isPresent()) {


            SettingScheduler updatedSetting = setting.get();
            updatedSetting.setName(name);
            settingRepository.save(updatedSetting);

        } else {
            throw new EntityNotFoundException("Setting not found");
        }
    }

    //xoa
    public void deleteSetting(int id) {
        settingRepository.deleteById(id);
    }

    public String getScore(String type) {
        List<SettingScheduler> schedulerList = settingRepository.findByType(type);
        for (SettingScheduler item : schedulerList)
        {
            if(item.isCheck()){
                return item.getName();
            }
        }
        return "8.0";
    }


    public void activateSetting(int id, String type) {
        // Lấy tất cả các mục và đặt `check` = false
        List<SettingScheduler> allSettings = settingRepository.findByType(type);
        for (SettingScheduler setting : allSettings) {
            if (setting.isCheck()) {
                setting.setCheck(false);
                settingRepository.save(setting);
            }
        }

        // Cập nhật mục được chọn thành `check` = true
        Optional<SettingScheduler> settingToActivate = settingRepository.findById(id);
        if (settingToActivate.isPresent()) {
            SettingScheduler setting = settingToActivate.get();
            setting.setCheck(true);
            settingRepository.save(setting);

            notificationScheduler.scheduleDynamicCronJob();
        } else {
            throw new EntityNotFoundException("Setting not found");
        }
    }


}
