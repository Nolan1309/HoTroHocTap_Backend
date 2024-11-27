package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.SettingScheduler;
import com.example.hotrohoctapbackend.entity.Test_Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "setting")
public interface SettingRepository extends JpaRepository<SettingScheduler,Integer> {

    List<SettingScheduler> findByType(String type);
}
