package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.ExistProgressPassDTO_USER;
import com.example.hotrohoctapbackend.DTO.User.ProgressDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestUserAnswerRequestDTO_User;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProgressService {
    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public List<ProgressDTO_User> getProgressByCourseAndAccount(Integer courseId, Integer accountId) {
        List<Object[]> results = progressRepository.findProgressByCourseAndAccount(courseId, accountId);

        // Ánh xạ kết quả từ Object[] vào DTO
        List<ProgressDTO_User> progressList = new ArrayList<>();
        for (Object[] result : results) {
            ProgressDTO_User dto = new ProgressDTO_User(
                    (Integer) result[0],  // account_id
                    (Integer) result[1],  // course_id
                    (Integer) result[2],  // chapter_id
                    (Integer) result[3],  // lesson_id
                    (Boolean) result[4],  // video_status
                    (Boolean) result[5],  // test_status
                    result[6] != null ? (Double) result[6] : null,
                    (Boolean) result[7]
            );
            progressList.add(dto);
        }

        return progressList;
    }

    public Map<String, Object> addOrUpdateProgress(ProgressDTO_User progressDTO) {
        Map<String, Object> result = new HashMap<>();
        // Lấy thông tin Account, Course, Chapter và Lesson từ các repository
        Account account = accountRepository.findById(progressDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Course course = courseRepository.findById(progressDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Chapter currentChapter = chapterRepository.findById(progressDTO.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

//        Optional<Progress> existingProgress;
//        if (progressDTO.isChapterTest()) {
//            // Nếu là bài kiểm tra chương (lesson_id là NULL)
//            existingProgress = progressRepository.findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonIdIsNull(
//                    account.getId(), course.getId(), currentChapter.getId(), true
//            );
//        } else {
//            // Nếu là bài kiểm tra thông thường (lesson_id không NULL)
//            Lesson currentLesson = lessonRepository.findById(progressDTO.getLessonId())
//                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
//            existingProgress = progressRepository.findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonId(
//                    account.getId(), course.getId(), currentChapter.getId(), false,currentLesson.getId()
//            );
//        }
//        if (existingProgress.isPresent() && existingProgress.get().getTestScore() >= 8.0) {
//            // Nếu đã hoàn thành bài kiểm tra trước đó và bài kiểm tra đã đạt
//            result.put("status", "already_completed");
//            return result;
//        }


        // Khởi tạo một Progress mới
        Progress progress = new Progress();
        progress.setAccount(account);
        progress.setCourse(course);
        progress.setCompletedAt(LocalDateTime.now());
        progress.setChapter(currentChapter);

        // Kiểm tra nếu đây là chapter test
        if (progressDTO.isChapterTest()) {
            // Nếu là chapter test, tìm chương tiếp theo
            List<Chapter> chaptersInCourse = chapterRepository.findChaptersByCourseId(course.getId());
            chaptersInCourse.sort(Comparator.comparingInt(Chapter::getId));

            Chapter nextChapter = null;
            for (int i = 0; i < chaptersInCourse.size(); i++) {
                if (chaptersInCourse.get(i).getId() == currentChapter.getId() && i < chaptersInCourse.size() - 1) {
                    // Nếu tìm thấy chương hiện tại và có chương tiếp theo
                    nextChapter = chaptersInCourse.get(i + 1);
                    break;
                }
            }

            if (nextChapter != null) {
                // Nếu có chương tiếp theo, mở khóa bài học đầu tiên trong chương tiếp theo
                List<Lesson> lessonsInNextChapter = lessonRepository.findLessonsByChapterId(nextChapter.getId());
                if (!lessonsInNextChapter.isEmpty()) {
                    Lesson firstLessonInNextChapter = lessonsInNextChapter.get(0);

                    progress.setChapter(nextChapter);
                    progress.setLesson(firstLessonInNextChapter);
                    progress.setVideoCompleted(true);
                    progress.setTestCompleted(true);
                    progress.setChapterTested(false);
                    progress.setTestScore(progressDTO.getTestScore());
                    result.put("status", "in_progress");
                } else {
                    result.put("status", "in_progress");
                }
            } else {
                result.put("status", "course_completed");
            }
        } else {
            // Nếu không phải chapter test, xử lý logic mở bài học tiếp theo trong chương hiện tại
            Lesson currentLesson = lessonRepository.findById(progressDTO.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));

            List<Lesson> lessonsInCurrentChapter = lessonRepository.findByChapter_IdAndCourse_Id(currentChapter.getId(), course.getId());
            lessonsInCurrentChapter.sort(Comparator.comparingInt(Lesson::getId));

            Lesson nextLesson = null;
            for (int i = 0; i < lessonsInCurrentChapter.size(); i++) {
                if (lessonsInCurrentChapter.get(i).getId() == currentLesson.getId() && i < lessonsInCurrentChapter.size() - 1) {
                    nextLesson = lessonsInCurrentChapter.get(i + 1);
                    break;
                }
            }

            if (nextLesson != null) {
                progress.setLesson(nextLesson);
                progress.setVideoCompleted(progressDTO.isVideoStatus());
                progress.setTestCompleted(progressDTO.isTestStatus());
                progress.setTestScore(progressDTO.getTestScore());
                result.put("status", "in_progress");
            } else {
                Progress chapterTestProgress = progressRepository.findByAccountAndCourseAndChapterAndLessonIsNull(
                        account, course, currentChapter
                );
                if (chapterTestProgress == null) {
                    progress.setLesson(null);
                    progress.setVideoCompleted(false);
                    progress.setTestCompleted(false);
                    progress.setChapterTested(true);
                    progress.setTestScore(0.0);
                } else if (!chapterTestProgress.isTestCompleted()) {
                    throw new RuntimeException("Chapter test not completed. Please complete the chapter test before proceeding.");
                } else {
                    // Nếu bài test cuối chương đã hoàn thành, tìm chương tiếp theo
                    List<Chapter> chaptersInCourse = chapterRepository.findChaptersByCourseId(course.getId());
                    chaptersInCourse.sort(Comparator.comparingInt(Chapter::getId));

                    Chapter nextChapter = null;
                    for (int i = 0; i < chaptersInCourse.size(); i++) {
                        if (chaptersInCourse.get(i).getId() == currentChapter.getId() && i < chaptersInCourse.size() - 1) {
                            // Nếu tìm thấy chương hiện tại và có chương tiếp theo
                            nextChapter = chaptersInCourse.get(i + 1);
                            break;
                        }
                    }

                    if (nextChapter != null) {
                        // Nếu có chương tiếp theo, mở khóa bài học đầu tiên trong chương tiếp theo
                        List<Lesson> lessonsInNextChapter = lessonRepository.findLessonsByChapterId(nextChapter.getId());
                        if (!lessonsInNextChapter.isEmpty()) {
                            Lesson firstLessonInNextChapter = lessonsInNextChapter.get(0);

                            progress.setAccount(account);
                            progress.setCourse(course);
                            progress.setChapter(nextChapter);
                            progress.setLesson(firstLessonInNextChapter);
                            progress.setVideoCompleted(progressDTO.isVideoStatus());
                            progress.setTestCompleted(progressDTO.isTestStatus());
                            progress.setTestScore(progressDTO.getTestScore());
                        } else {
                            throw new RuntimeException("Next chapter has no lessons available");
                        }
                    } else {
                        throw new RuntimeException("No more chapters available");
                    }
                }
                result.put("status", "in_progress");
            }
        }

        if (!"course_completed".equals(result.get("status"))) {
            progressRepository.save(progress);
        }
        return result;

    }

    public boolean isLastChapter(Integer courseId, Integer chapterId) {
        List<Chapter> chaptersInCourse = chapterRepository.findChaptersByCourseId(courseId);
        chaptersInCourse.sort(Comparator.comparingInt(Chapter::getId));
        return !chaptersInCourse.isEmpty() && chaptersInCourse.get(chaptersInCourse.size() - 1).getId() == chapterId;
    }

    public void UpdateScore(TestUserAnswerRequestDTO_User idProgress, TestResultDTO_User score) {


        Optional<Progress> progress;

        // Kiểm tra nếu là chapter test
        if (idProgress.isChapterTest()) {
            progress = progressRepository.findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonIdIsNull(
                    idProgress.getAccountId(), idProgress.getCourseId(), idProgress.getChapterId(), idProgress.isChapterTest()
            );
        } else {
            // Nếu là lesson test
            progress = progressRepository.findByAccountIdAndCourseIdAndChapterIdAndChapterTestedAndLessonId(
                    idProgress.getAccountId(), idProgress.getCourseId(), idProgress.getChapterId(), idProgress.isChapterTest(), idProgress.getLessonId()
            );
        }

        // Cập nhật điểm nếu progress tồn tại
        if (progress.isPresent()) {
            Progress progressEntity = progress.get();
            progressEntity.setTestScore(score.getScore());
            progressRepository.save(progressEntity);
        } else {
            throw new EntityNotFoundException("Progress không tồn tại cho các tham số được cung cấp.");
        }

    }

    public boolean isNextProgressExists(Integer accountId, Integer courseId, Integer chapterId, Integer lessonId, boolean isChapterTest) {
        if (isChapterTest) {
            // Lấy danh sách tất cả các chapterId của khóa học
            List<Integer> chapterIds = progressRepository.findAllChaptersByCourseId(courseId);

            // Kiểm tra nếu chapterId hiện tại là chapterId cuối cùng trong danh sách
            boolean isLastChapter = !chapterIds.isEmpty() && chapterIds.get(chapterIds.size() - 1).equals(chapterId);

            if (!isLastChapter) {
                // Nếu không phải chương cuối, tìm bài đầu tiên của chương tiếp theo
                Integer nextChapterId = chapterIds.get(chapterIds.indexOf(chapterId) + 1); // Tìm chapterId kế tiếp trong danh sách
                Integer firstLessonInNextChapter = progressRepository.findFirstLessonInChapter(courseId, nextChapterId);

                // Kiểm tra nếu bài đầu tiên của chương tiếp theo đã có tiến trình
                return progressRepository.existsByAccountIdAndCourseIdAndChapterIdAndLessonId(accountId, courseId, nextChapterId, firstLessonInNextChapter);
            } else {
                // Đây là chương cuối cùng, không có chương tiếp theo
                return false;
            }
        } else {
            // Kiểm tra có phải là bài cuối trong chương không
            Integer lastLessonInCurrentChapter = progressRepository.findLastLessonInChapter(courseId, chapterId);

            if (!lessonId.equals(lastLessonInCurrentChapter)) {
                // Nếu không phải bài cuối, kiểm tra tiến trình của bài kế tiếp trong chương hiện tại
                Integer nextLessonId = lessonId + 1;
                return progressRepository.existsByAccountIdAndCourseIdAndChapterIdAndLessonId(accountId, courseId, chapterId, nextLessonId);
            } else {
                // Nếu là bài cuối của chương, kiểm tra bài test của chương đó
                return progressRepository.existsByAccountIdAndCourseIdAndChapterIdAndLessonIdAndChapterTested(accountId, courseId, chapterId, null, true);
            }
        }
    }

    public Double calculateProgress(Integer accountId, Integer courseId) {
        // Total lessons and chapters in the course
        Long totalLessons = lessonRepository.countsLessonsByCourseIdUser(courseId);
        Long totalChapters = chapterRepository.countChaptersByCourseIdUser(courseId);

        // Completed lessons and chapters for the user
        Long completedLessons = progressRepository.countCompletedLessonsUser(accountId, courseId);
        Long completedChapters = progressRepository.countCompletedChaptersUser(accountId, courseId);

        // Calculate progress
        Long totalItems = totalLessons + totalChapters;
        Long completedItems = completedLessons + completedChapters;

        return (completedItems.doubleValue() / totalItems.doubleValue()) * 100;
    }

    public Boolean checkPassResult_User(ExistProgressPassDTO_USER existProgressPassDTOUser) {


        Optional<Progress> progressOptional;

        if (existProgressPassDTOUser.isChapterTest()) {
            progressOptional = progressRepository.findProgressByChapterTest(
                    existProgressPassDTOUser.getCourseId(),
                    existProgressPassDTOUser.getAccountId(),
                    existProgressPassDTOUser.getChapterId()
            );
        } else {
            progressOptional = progressRepository.findProgressByLesson(
                    existProgressPassDTOUser.getCourseId(),
                    existProgressPassDTOUser.getAccountId(),
                    existProgressPassDTOUser.getLessonId()
            );
        }
        return progressOptional
                .map(progress -> progress.getTestScore() != null && progress.getTestScore() > 6)
                .orElse(false);
    }
}
