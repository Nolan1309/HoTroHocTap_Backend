package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.ChapterDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminChapterDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCourseDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Chapter.ChapterDTOAdminV3;
import com.example.hotrohoctapbackend.DTO.ChapterDTO;

import com.example.hotrohoctapbackend.DTO.LessonDTO;
import com.example.hotrohoctapbackend.DTO.TestDTO;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TestRepository testRepository;

    // Phương thức tìm Chapter bằng courseId và trả về danh sách ChapterDTO
    public List<ChapterDTO> findChaptersByCourseId(Integer courseId) {
        // Tìm chapters bằng courseId
        List<Chapter> chapters = chapterRepository.findChaptersByCourseId(courseId);
        List<ChapterDTO> chapterDTOList = new ArrayList<>();

        for (Chapter item : chapters) {
            ChapterDTO chapterDTO = new ChapterDTO();

            List<LessonDTO> lessonDTOS = new ArrayList<>();
            List<TestDTO> testDTOS = new ArrayList<>();

            List<Lesson> lessonList = lessonRepository.findLessonsByChapterId(item.getId());
            List<Test> testList = testRepository.findTestsByChapterId(item.getId());

            for (Lesson lessonDTO : lessonList) {
                LessonDTO lessonDTO1 = new LessonDTO();
                lessonDTO1.setId(lessonDTO.getId());
                lessonDTO1.setTitle(lessonDTO.getTitle());
                lessonDTO1.setDeleted(lessonDTO.isDeleted());
                lessonDTO1.setStatus(lessonDTO.isStatus());
                lessonDTO1.setTopic(lessonDTO.getTopic());
                lessonDTO1.setIsTestExcluded(lessonDTO.getIsTestExcluded());
                lessonDTOS.add(lessonDTO1);
            }

            for (Test test : testList) {
                TestDTO testDTO = new TestDTO();
                testDTO.setId(test.getId());
                testDTO.setTitle(test.getTitle());
                testDTO.setDescription(test.getDescription());
                testDTO.setSummary(test.isSummary());
                testDTO.setTotalQuestion(test.getTotalQuestion());
                testDTO.setCreatedAt(test.getCreatedAt());
                testDTO.setUpdatedAt(test.getUpdatedAt());
                testDTO.setLessonId(test.getLesson() != null ? test.getLesson().getId() : null); // Kiểm tra lesson null
                testDTO.setChapterId(test.getChapter() != null ? test.getChapter().getId() : null);
                testDTOS.add(testDTO);
            }

            chapterDTO.setId(item.getId());
            chapterDTO.setTitle(item.getTitle());
            chapterDTO.setStatus(item.isStatus());
            chapterDTO.setDeleted(item.isDeleted());
            chapterDTO.setLessonList(lessonDTOS);
            chapterDTO.setTestList(testDTOS);
            chapterDTO.setId_course(item.getCourse().getId());
            chapterDTOList.add(chapterDTO);
        }

        return chapterDTOList;

    }

    public List<ChapterDTOAdminV3> getChaptersByCourseId(int courseId) {
        List<Chapter> chapters = chapterRepository.findByCourseIdAndIsDeletedFalse(courseId);
        return chapters.stream()
                .map(chapter -> new ChapterDTOAdminV3(String.valueOf(chapter.getId()), chapter.getCourse().getId(), chapter.getTitle(),
                        chapter.isStatus(),
                        chapter.getDeletedDate() != null ? chapter.getDeletedDate().toString() : "",
                        chapter.isDeleted()))
                .collect(Collectors.toList());
    }

    public List<ChapterDTOAdmin> findAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();

        List<ChapterDTOAdmin> chapterDTOAdmins = new ArrayList<>();
        for (Chapter item : chapters) {
            ChapterDTOAdmin chapterDTOAdmin = new ChapterDTOAdmin();
            chapterDTOAdmin.setId(item.getId());
            chapterDTOAdmin.setTitle(item.getTitle());
            chapterDTOAdmin.setCourse_id(item.getCourse().getId());
            chapterDTOAdmin.setDeleted(item.isDeleted());
            chapterDTOAdmins.add(chapterDTOAdmin);
        }
        return chapterDTOAdmins;
    }


    public Boolean addChapter(ChapterDTO chapterDTO) {
        // Tìm course theo id_course trong DTO
        Course course = courseRepository.findById(chapterDTO.getId_course())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + chapterDTO.getId_course()));

        // Tạo mới Chapter từ ChapterDTO
        Chapter chapter = new Chapter();
        chapter.setTitle(chapterDTO.getTitle());
        chapter.setStatus(chapterDTO.getStatus());
        chapter.setCourse(course);

        Chapter checkChapter = chapterRepository.saveAndFlush(chapter);
        if (checkChapter != null) {
            return true;
        } else return false;
    }

    public Chapter editChapter(Integer chapterId, ChapterDTO chapterDTO) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found with id: " + chapterId));
        chapter.setTitle(chapterDTO.getTitle());
        chapter.setStatus(chapterDTO.getStatus());
        return chapterRepository.save(chapter);
    }

    public Chapter hideChapterAdmin(int chapterID) {
        // Tìm tài khoản theo ID
        Optional<Chapter> accountOpt = chapterRepository.findById(chapterID);

        if (accountOpt.isPresent()) {
            Chapter account = accountOpt.get();
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return chapterRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + chapterID);
        }
    }

    public Chapter showChapterAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Chapter> accountOpt = chapterRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Chapter account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return chapterRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }

    public Chapter lockChapterAdmin(int chapterID) {
        // Tìm tài khoản theo ID
        Optional<Chapter> accountOpt = chapterRepository.findById(chapterID);

        if (accountOpt.isPresent()) {
            Chapter chapter = accountOpt.get();
            chapter.setStatus(false);
            return chapterRepository.save(chapter);
        } else {
            throw new RuntimeException("Test not found with id: " + chapterID);
        }
    }

    public Chapter unlockChapterAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Chapter> accountOpt = chapterRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Chapter chapter = accountOpt.get();
            chapter.setStatus(true);
            return chapterRepository.save(chapter);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminChapterDTORestoreList> getDeletedChapter(Pageable pageable) {
        Page<Object[]> resultPage = chapterRepository.findDeletedChapterAll(pageable);
        List<AdminChapterDTORestoreList> chapterDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminChapterDTORestoreList dto = new AdminChapterDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setChapterTitle((String) result[1]);
            dto.setCourseId((Integer) result[2]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[4]);
            chapterDTORestoreLists.add(dto);
        }
        return new PageImpl<>(chapterDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Page<AdminChapterDTORestoreList> getDeletedChapterSearch(String chapterTitle, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = chapterRepository.searchChapterByChapterTitleAndDeleteDate(chapterTitle, deletedDate, pageable);
        if (chapterTitle != null && !chapterTitle.isEmpty() && deletedDate != null && !deletedDate.isEmpty()) {
            resultPage = chapterRepository.searchChapterByChapterTitleAndDeleteDate(chapterTitle, deletedDate, pageable);
        } else if (chapterTitle != null && !chapterTitle.isEmpty()) {
            resultPage = chapterRepository.searchChapterByTitle(chapterTitle, pageable);
        } else if (deletedDate != null && !deletedDate.isEmpty()) {
            resultPage = chapterRepository.searchChapterByDeletedDate(deletedDate, pageable);
        }
        List<AdminChapterDTORestoreList> chapterDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminChapterDTORestoreList dto = new AdminChapterDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setChapterTitle((String) result[1]);
            dto.setCourseId((Integer) result[2]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[4]);
            chapterDTORestoreLists.add(dto);
        }
        return new PageImpl<>(chapterDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Page<AdminChapterDTORestoreList> getDeletedChapterByCourseId(Integer courseId, Pageable pageable) {
        Page<Object[]> resultPage = chapterRepository.findDeletedChapterByCourseId(courseId, pageable);
        List<AdminChapterDTORestoreList> chapterDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminChapterDTORestoreList dto = new AdminChapterDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setChapterTitle((String) result[1]);
            dto.setCourseId((Integer) result[2]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[4]);
            chapterDTORestoreLists.add(dto);
        }
        return new PageImpl<>(chapterDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Page<AdminChapterDTORestoreList> getDeletedChapterSearchByCourseId(Integer courseId, String chapterTittle, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = chapterRepository.searchChapterByChapterTitleAndDeleteDateByCourseId(courseId, chapterTittle, deletedDate, pageable);
        if (chapterTittle != null && !chapterTittle.isEmpty() && deletedDate != null && !deletedDate.isEmpty()) {
            resultPage = chapterRepository.searchChapterByChapterTitleAndDeleteDateByCourseId(courseId, chapterTittle, deletedDate, pageable);
        } else if (chapterTittle != null && !chapterTittle.isEmpty()) {
            resultPage = chapterRepository.searchChapterByTitleByCourseId(courseId, chapterTittle, pageable);
        } else if (deletedDate != null && !deletedDate.isEmpty()) {
            resultPage = chapterRepository.searchChapterByDeletedDateByCourseId(courseId, deletedDate, pageable);
        }
        List<AdminChapterDTORestoreList> chapterDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminChapterDTORestoreList dto = new AdminChapterDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setChapterTitle((String) result[1]);
            dto.setCourseId((Integer) result[2]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[4]);
            chapterDTORestoreLists.add(dto);
        }
        return new PageImpl<>(chapterDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Chapter updateRestoreChapter(AdminChapterDTORestoreList adminCourseDTORestoreList) {
        Optional<Chapter> accountOptional = chapterRepository.findById(adminCourseDTORestoreList.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Chapter not found with id: " + adminCourseDTORestoreList.getId());
        } else {
            Chapter chapter = accountOptional.get();
            chapter.setDeleted(false);
            return chapterRepository.save(chapter);
        }
    }

    public void deleteRestoreChapter(AdminChapterDTORestoreList accountDetailsDTOV2) {
        Optional<Chapter> accountOptional = chapterRepository.findById(accountDetailsDTOV2.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Chapter not found with id: " + accountDetailsDTOV2.getId());
        } else {
            chapterRepository.delete(accountOptional.get());
        }
    }

    public List<AdminChapterDTORestoreList> getNoDeletedChapter(Integer courseId) {
        List<Object[]> resultPage = chapterRepository.findNoDeletedChaptersList(courseId);
        List<AdminChapterDTORestoreList> chapterDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminChapterDTORestoreList dto = new AdminChapterDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setChapterTitle((String) result[1]);
            dto.setCourseId((Integer) result[2]);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[4]);
            chapterDTORestoreLists.add(dto);
        }
        return chapterDTORestoreLists;
    }

}