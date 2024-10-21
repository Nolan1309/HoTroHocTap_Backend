package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.ProgressDTO_User;
import com.example.hotrohoctapbackend.dao.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressService {
    @Autowired
    private ProgressRepository progressRepository;

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
                    result[6] != null ? (Integer) result[6] : null
            );
            progressList.add(dto);
        }

        return progressList;
    }
}
