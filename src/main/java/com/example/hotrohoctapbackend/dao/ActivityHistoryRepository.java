package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Activity_History;
import com.example.hotrohoctapbackend.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "activity_history")
public interface ActivityHistoryRepository extends JpaRepository<Activity_History, Integer> {
//    List<Activity_History> findByAccountIdAndActivityType(int accountId, String activityType);

    List<Activity_History> findByAccountIdAndActivityType(int accountId, ActivityType activityType);

    List<Activity_History> findByAccountIdAndActivityTypeAndTimestampBefore(int accountId, ActivityType activityType, LocalDateTime timestamp);

    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'LOGIN'")
    long countLoginCountByAccountId(Integer accountId);

    // Đếm số lần xem video của accountId
    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'VIEW_LESSON'")
    long countVideoWatchedCountByAccountId(Integer accountId);

    // Đếm số lần làm bài tập của accountId
    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'START_EXAM'")
    long countAssignmentSubmittedCountByAccountId(Integer accountId);
}
