package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.ActivityLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "activitylogs")
public interface ActivityLogsRepository extends JpaRepository<ActivityLogs,Integer> {
}
