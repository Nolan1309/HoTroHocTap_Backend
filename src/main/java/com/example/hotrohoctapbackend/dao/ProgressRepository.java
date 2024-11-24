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
import java.util.Optional;

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
            "up.is_chapter_test " +
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

    Optional<Progress> findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonId(
            Integer accountId, Integer courseId, Integer chapterId, Boolean chapterTested, Integer lessonId
    );

    Optional<Progress> findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonIdIsNull(
            Integer accountId, Integer courseId, Integer chapterId, Boolean chapterTested
    );

    @Query(value = "SELECT c.id FROM chapters c WHERE c.course_id = :courseId ORDER BY c.id ASC", nativeQuery = true)
    List<Integer> findAllChaptersByCourseId(@Param("courseId") Integer courseId);

    @Query(value = "SELECT MIN(l.id) FROM lessons l WHERE l.course_id = :courseId AND l.chapter_id = :chapterId", nativeQuery = true)
    Integer findFirstLessonInChapter(@Param("courseId") Integer courseId, @Param("chapterId") Integer chapterId);


    @Query(value = "SELECT MAX(l.id) FROM lessons l WHERE l.course_id = :courseId AND l.chapter_id = :chapterId", nativeQuery = true)
    Integer findLastLessonInChapter(@Param("courseId") Integer courseId, @Param("chapterId") Integer chapterId);

    // Kiểm tra nếu tiến trình tồn tại
    boolean existsByAccountIdAndCourseIdAndChapterId(Integer accountId, Integer courseId, Integer chapterId);

    // Kiểm tra nếu tiến trình của bài học tồn tại
    boolean existsByAccountIdAndCourseIdAndChapterIdAndLessonId(Integer accountId, Integer courseId, Integer chapterId, Integer lessonId);

    boolean existsByAccountIdAndCourseIdAndChapterIdAndLessonIdAndChapterTested(
            Integer accountId, Integer courseId, Integer chapterId, Integer lessonId, boolean chapterTested);

    @Query(value = "SELECT COUNT(DISTINCT CASE WHEN p.lesson_id IS NOT NULL THEN p.lesson_id END) as countLesson " +
            "FROM progress p WHERE p.account_id = :accountId AND p.course_id = :courseId AND p.test_score > 0",nativeQuery = true)
    Long countCompletedLessonsUser(@Param("accountId") Integer accountId, @Param("courseId") Integer courseId);

    @Query(value = "SELECT COUNT(DISTINCT CASE WHEN p.is_chapter_test = true THEN p.chapter_id END) as countChapter " +
            "FROM progress p WHERE p.account_id = :accountId AND p.course_id = :courseId AND p.test_score > 0", nativeQuery = true)
    Long countCompletedChaptersUser(@Param("accountId") Integer accountId, @Param("courseId") Integer courseId);

    @Query(value = "SELECT * FROM progress WHERE course_id = :courseId AND account_id = :accountId AND lesson_id = :lessonId AND is_chapter_test = 0 LIMIT 1", nativeQuery = true)
    Optional<Progress> findProgressByLesson(@Param("courseId") int courseId,
                                            @Param("accountId") int accountId,
                                            @Param("lessonId") int lessonId);

    // Lấy một dòng duy nhất cho bài kiểm tra chương
    @Query(value = "SELECT * FROM progress WHERE course_id = :courseId AND account_id = :accountId AND lesson_id IS NULL AND chapter_id = :chapterId AND is_chapter_test = 1 LIMIT 1", nativeQuery = true)
    Optional<Progress> findProgressByChapterTest(@Param("courseId") int courseId,
                                                 @Param("accountId") int accountId,
                                                 @Param("chapterId") int chapterId);
}
