package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import com.example.hotrohoctapbackend.entity.ExamInfo;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamInfoRepository extends JpaRepository<ExamInfo, Integer> {

    @Query("SELECT ex FROM ExamInfo ex WHERE ex.test.id = :testId")
    Optional<ExamInfo> findByTestId(Integer testId);

}
