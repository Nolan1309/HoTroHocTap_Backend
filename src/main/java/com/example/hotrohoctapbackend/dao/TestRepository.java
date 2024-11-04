    package com.example.hotrohoctapbackend.dao;

    import com.example.hotrohoctapbackend.entity.Test;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.rest.core.annotation.RepositoryRestResource;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import com.example.hotrohoctapbackend.DTO.AdminTestUpdateDTO;
    @RepositoryRestResource(path = "tests")
    public interface TestRepository extends JpaRepository<Test,Integer> {
        @Query(value = "SELECT t.id, t.title, t.description, t.lesson_id, t.chapter_id, t.course_id, t.total_question, t.is_summary " +
                "FROM test t WHERE t.id = :id", nativeQuery = true)
        AdminTestUpdateDTO findTestByIdNative(@Param("id") Integer id);
    }
