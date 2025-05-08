package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "prediction_result")
public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {
    Optional<PredictionResult> findTopByAccountIdOrderByCreatedAtDesc(Integer accountId);

    Optional<PredictionResult> findTopByStudentId(String studentId);

    @Query("SELECT COUNT(DISTINCT p.studentId) FROM PredictionResult p WHERE p.account.id IN " +
            "(SELECT s.account.id FROM StudentCourseData s WHERE s.course.id = :courseId " +
            "AND (:classRoom IS NULL OR s.classRoom = :classRoom))")
    long countPredictedStudentsByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);


    @Query("SELECT COUNT(DISTINCT p.studentId) FROM PredictionResult p WHERE p.prediction = 0 AND p.account.id IN " +
            "(SELECT s.account.id FROM StudentCourseData s WHERE s.course.id = :courseId " +
            "AND (:classRoom IS NULL OR s.classRoom = :classRoom))")
    long countPredictedPassByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);


    @Query("SELECT COUNT(DISTINCT p.studentId) FROM PredictionResult p WHERE p.prediction = 1 AND p.account.id IN " +
            "(SELECT s.account.id FROM StudentCourseData s WHERE s.course.id = :courseId " +
            "AND (:classRoom IS NULL OR s.classRoom = :classRoom))")
    long countPredictedFailByCourseAndClassRoom(@Param("courseId") Integer courseId, @Param("classRoom") String classRoom);

}
