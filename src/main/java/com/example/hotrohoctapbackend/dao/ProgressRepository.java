package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "progress")
public interface ProgressRepository extends JpaRepository<Progress, Integer> {

    @Query(value = "SELECT " +
            "up.account_id, " +
            "up.course_id, " +
            "up.chapter_id, " +
            "up.lesson_id, " +
            "up.video_completed AS video_status, " +
            "up.test_completed AS test_status, " +
            "up.test_score, " +
            "up.is_chapter_test "+
            "FROM progress up " +
            "WHERE up.course_id = :courseId " +
            "AND up.account_id = :accountId",
            nativeQuery = true)
    List<Object[]> findProgressByCourseAndAccount(@Param("courseId") Integer courseId, @Param("accountId") Integer accountId);

    @Query("SELECT p FROM Progress p WHERE p.account = :account AND p.course = :course AND p.chapter = :chapter AND p.lesson IS NULL")
    Progress findByAccountAndCourseAndChapterAndLessonIsNull(
            @Param("account") Account account,
            @Param("course") Course course,
            @Param("chapter") Chapter chapter
    );
}
