package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.StudentCourseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "student_course_data")
public interface StudentCourseDataRepository extends JpaRepository<StudentCourseData, String> {
    // Các truy vấn tùy chỉnh nếu cần
    Optional<StudentCourseData> findByAccount_IdAndStudentId(Integer accountId, String studentId);

    Optional<StudentCourseData> findByStudentId(String studentId);


    Optional<StudentCourseData> findByAccountIdAndCourseId(Integer accountId, Integer courseId);

    Optional<StudentCourseData> findStudentCourseDataByEmail(String email);

    //    List<StudentCourseData> findStudentCourseDataByIdIn(List<>);
    Optional<StudentCourseData> findByEmail(String email);


    @Query("SELECT s FROM StudentCourseData s WHERE s.course.id = :courseId AND s.account.id IS NOT NULL")
    Page<StudentCourseData> findStudentCourseDataByCourse_IdAndAccount_Id(Integer courseId, Pageable pageable);

    //    Page<StudentCourseData> findStudentCourseDataByCourse_Id(Integer courseId, Pageable pageable);
    Page<StudentCourseData> findByCourse_IdAndClassRoomContaining(Integer courseId, String classRoom, Pageable pageable);

    Page<StudentCourseData> findStudentCourseDataByCourse_IdAndClassRoom(Integer courseId, Pageable pageable, String classRoom);

    @Query("SELECT s FROM StudentCourseData s WHERE s.course.id = :courseId AND s.classRoom = :classRoom AND s.account.id IS NOT NULL")
    Page<StudentCourseData> findStudentCourseDataByCourse_IdAndClassRoomAndAccount_Id(Integer courseId, String classRoom, Pageable pageable);

    List<StudentCourseData> findStudentCourseDataByCourse_Id(Integer courseId);

    @Query("SELECT s FROM StudentCourseData s WHERE s.course.id = :courseId AND s.account.id IS NOT NULL")
    List<StudentCourseData> findStudentCourseDataByCourse_IdAccNull(Integer courseId);

    Optional<StudentCourseData> findByAccountId(Integer accountId);

    List<StudentCourseData> findStudentCourseDataByCourse_IdAndClassRoom(Integer courseId, String classRoom);

    @Query("SELECT COUNT(s) FROM StudentCourseData s WHERE s.course.id = :courseId AND (:classRoom IS NULL OR s.classRoom = :classRoom)")
    long countTotalStudentsByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);

    @Query("SELECT COUNT(s) FROM StudentCourseData s WHERE s.examScore >= 5 AND s.course.id = :courseId AND (:classRoom IS NULL OR s.classRoom = :classRoom)")
    long countPassedStudentsByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);

    @Query("SELECT COUNT(s) FROM StudentCourseData s WHERE s.examScore < 5 AND s.course.id = :courseId AND (:classRoom IS NULL OR s.classRoom = :classRoom)")
    long countFailedStudentsByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);

    @Query("SELECT s FROM StudentCourseData s WHERE s.course.id = :courseId")
    Page<StudentCourseData> findByCourseId(@Param("courseId") Integer courseId, Pageable pageable);

    @Query("SELECT s FROM StudentCourseData s")
    Page<StudentCourseData> findAllWithPagination(Pageable pageable);

}
