package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Activity_History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "activity_history")
public interface ActivityHistoryRepository extends JpaRepository<Activity_History, Integer> {
    List<Activity_History> findByAccountIdAndActivityType(int accountId, String activityType);

    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'login'")
    long countLoginCountByAccountId(Integer accountId);

    // Đếm số lần xem video của accountId
    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'video_clicked'")
    long countVideoWatchedCountByAccountId(Integer accountId);

    // Đếm số lần làm bài tập của accountId
    @Query("SELECT COUNT(a) FROM Activity_History a WHERE a.account.id = :accountId AND a.activityType = 'test_clicked'")
    long countAssignmentSubmittedCountByAccountId(Integer accountId);
}
