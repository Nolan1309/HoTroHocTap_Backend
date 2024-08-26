package com.example.hotrohoctapbackend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController2 {
    @Autowired
    private  ConvertDocService documentConversionService;

    public DocumentController2(ConvertDocService documentConversionService) {
        this.documentConversionService = documentConversionService;
    }

    @PostMapping("/convert")
    public String convertDocument(@RequestParam String inputFilePath, @RequestParam String outputDirPath) {
        try {
            documentConversionService.convertDocxToPdf(inputFilePath, outputDirPath);
            return "Conversion successful!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Conversion failed: " + e.getMessage();
        }
    }
}
