package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "testresults")
public interface TestResultRepository extends JpaRepository<TestResult,Integer> {
}
