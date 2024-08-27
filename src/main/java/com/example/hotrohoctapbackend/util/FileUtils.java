package com.example.hotrohoctapbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class FileUtils {
    public static MultipartFile convertToMultipartFile(InputStream inputStream, String originalFilename, String contentType) {
        return new MultipartFileInputStream(inputStream, originalFilename, contentType);
    }
}
