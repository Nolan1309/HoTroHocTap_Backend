package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.TestUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "testuseranswers")
public interface TestUserAnswerRepository extends JpaRepository<TestUserAnswer,Integer> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TestUserAnswer t " +
            "WHERE t.test.id = :testId AND t.question.id = :questionId AND t.account.id = :accountId")
    boolean existsByTestIdAndQuestionIdAndAccountId(
            @Param("testId") Integer testId,
            @Param("questionId") Integer questionId,
            @Param("accountId") Integer accountId
    );
}
