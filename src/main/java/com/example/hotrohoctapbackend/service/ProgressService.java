package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.ProgressDTO_User;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
                    result[6] != null ? (Integer) result[6] : null,
                    (Boolean) result[7]
            );
            progressList.add(dto);
        }

        return progressList;
    }

    //    public Progress addOrUpdateProgress(ProgressDTO_User progressDTO) {
//        // Lấy thông tin Account, Course, Chapter và Lesson từ các repository
//        Account account = accountRepository.findById(progressDTO.getAccountId())
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//        Course course = courseRepository.findById(progressDTO.getCourseId())
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        Chapter currentChapter = chapterRepository.findById(progressDTO.getChapterId())
//                .orElseThrow(() -> new RuntimeException("Chapter not found"));
//
//
//
//
//        if(!progressDTO.isChapterTest()){
//
//        }
//        Lesson currentLesson = lessonRepository.findById(progressDTO.getLessonId())
//                .orElseThrow(() -> new RuntimeException("Lesson not found"));
//
//        // Lấy danh sách các bài học trong chương hiện tại
//        List<Lesson> lessonsInCurrentChapter = lessonRepository.findByChapter_IdAndCourse_Id(currentChapter.getId(), course.getId());
//
//        lessonsInCurrentChapter.sort(Comparator.comparingInt(Lesson::getId));
//
//        // Tìm bài học tiếp theo trong chương hiện tại
//        Lesson nextLesson = null;
//        for (int i = 0; i < lessonsInCurrentChapter.size(); i++) {
//            if (lessonsInCurrentChapter.get(i).getId() == currentLesson.getId() && i < lessonsInCurrentChapter.size() - 1) {
//                // Nếu tìm thấy bài học hiện tại và có bài tiếp theo
//                nextLesson = lessonsInCurrentChapter.get(i + 1);
//                break;
//            }
//        }
//
//        Progress progress = new Progress();
//
//        if (nextLesson != null) {
//            // Nếu có bài học tiếp theo trong chương hiện tại
//            progress.setAccount(account);
//            progress.setCourse(course);
//            progress.setChapter(currentChapter);
//            progress.setLesson(nextLesson);
//            progress.setVideoCompleted(progressDTO.isVideoStatus());
//            progress.setTestCompleted(progressDTO.isTestStatus());
//            progress.setTestScore(progressDTO.getTestScore());
//        } else {
//            // Nếu không còn bài học nào trong chương hiện tại, kiểm tra xem bài test cuối chương đã có trong Progress chưa
//            Progress chapterTestProgress = progressRepository.findByAccountAndCourseAndChapterAndLessonIsNull(
//                    account, course, currentChapter
//            );
//
//            if (chapterTestProgress == null) {
//                // Nếu chưa có, mở khóa bài test cuối chương (lesson_id = null)
//                progress.setAccount(account);
//                progress.setCourse(course);
//                progress.setChapter(currentChapter);
//                progress.setLesson(null); // lesson_id là null cho bài test cuối chương
//                progress.setVideoCompleted(false); // Không có video cho bài test chương
//                progress.setTestCompleted(false); // Chưa hoàn thành bài test chương
//                progress.setChapterTested(true);
//                progress.setTestScore(null);
//            } else if (!chapterTestProgress.isTestCompleted()) {
//                // Nếu bài test cuối chương chưa hoàn thành, yêu cầu người dùng hoàn thành bài test chương
//                throw new RuntimeException("Chapter test not completed. Please complete the chapter test before proceeding.");
//            } else {
//                // Nếu bài test cuối chương đã hoàn thành, tìm chương tiếp theo
//                List<Chapter> chaptersInCourse = chapterRepository.findChaptersByCourseId(course.getId());
//                chaptersInCourse.sort(Comparator.comparingInt(Chapter::getId));
//
//                Chapter nextChapter = null;
//                for (int i = 0; i < chaptersInCourse.size(); i++) {
//                    if (chaptersInCourse.get(i).getId() == currentChapter.getId() && i < chaptersInCourse.size() - 1) {
//                        // Nếu tìm thấy chương hiện tại và có chương tiếp theo
//                        nextChapter = chaptersInCourse.get(i + 1);
//                        break;
//                    }
//                }
//
//                if (nextChapter != null) {
//                    // Nếu có chương tiếp theo, mở khóa bài học đầu tiên trong chương tiếp theo
//                    List<Lesson> lessonsInNextChapter = lessonRepository.findLessonsByChapterId(nextChapter.getId());
//                    if (!lessonsInNextChapter.isEmpty()) {
//                        Lesson firstLessonInNextChapter = lessonsInNextChapter.get(0);
//
//                        progress.setAccount(account);
//                        progress.setCourse(course);
//                        progress.setChapter(nextChapter);
//                        progress.setLesson(firstLessonInNextChapter);
//                        progress.setVideoCompleted(progressDTO.isVideoStatus());
//                        progress.setTestCompleted(progressDTO.isTestStatus());
//                        progress.setTestScore(progressDTO.getTestScore());
//                    } else {
//                        throw new RuntimeException("Next chapter has no lessons available");
//                    }
//                } else {
//                    throw new RuntimeException("No more chapters available");
//                }
//            }
//        }
//
//        // Lưu progress vào database
//        return progressRepository.save(progress);
//    }
    public Progress addOrUpdateProgress(ProgressDTO_User progressDTO) {
        // Lấy thông tin Account, Course, Chapter và Lesson từ các repository
        Account account = accountRepository.findById(progressDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Course course = courseRepository.findById(progressDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Chapter currentChapter = chapterRepository.findById(progressDTO.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Khởi tạo một Progress mới
        Progress progress = new Progress();
        progress.setAccount(account);
        progress.setCourse(course);
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
                    progress.setVideoCompleted(progressDTO.isVideoStatus());
                    progress.setTestCompleted(progressDTO.isTestStatus());
                    progress.setTestScore(progressDTO.getTestScore());
                } else {
                    throw new RuntimeException("Next chapter has no lessons available");
                }
            } else {
                throw new RuntimeException("No more chapters available");
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
            } else {
                Progress chapterTestProgress = progressRepository.findByAccountAndCourseAndChapterAndLessonIsNull(
                        account, course, currentChapter
                );
                if (chapterTestProgress == null) {
                    progress.setLesson(null);
                    progress.setVideoCompleted(false);
                    progress.setTestCompleted(false);
                    progress.setChapterTested(true);
                    progress.setTestScore(null);
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
            }
        }

        return progressRepository.save(progress);
    }


}
