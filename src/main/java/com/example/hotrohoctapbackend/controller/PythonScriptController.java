package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.LearningPathSuggestionAPI;
import com.example.hotrohoctapbackend.service.services.PythonScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/python")
public class PythonScriptController {

    @Autowired
    private PythonScriptService studentDataService;

    @PostMapping("/send-student-data")
    public List<LearningPathSuggestionAPI> sendStudentDataToFlask(@RequestBody Object studentData) {
        return studentDataService.sendStudentData(studentData);
    }
}
