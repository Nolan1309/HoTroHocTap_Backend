package com.example.hotrohoctapbackend.util;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MultipartFileInputStream implements MultipartFile {

    private final InputStream inputStream;
    private final String originalFilename;
    private final String contentType;

    public MultipartFileInputStream(InputStream inputStream, String originalFilename, String contentType) {
        this.inputStream = inputStream;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        try {
            return inputStream.available() == 0;
        } catch (IOException e) {
            throw new RuntimeException("Error checking if InputStream is empty", e);
        }
    }

    @Override
    public long getSize() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            throw new RuntimeException("Error getting size of InputStream", e);
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        return inputStream.readAllBytes();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Transfer to file is not supported");
    }
}
