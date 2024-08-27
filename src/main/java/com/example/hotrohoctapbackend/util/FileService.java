package com.example.hotrohoctapbackend.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Service
public class FileService {
    public String saveFileToStaticFolder(MultipartFile file) throws IOException {
        // Đường dẫn thư mục static
        String staticFolderPath = "src/main/resources/static/data";

        // Xác định tên file và đường dẫn lưu
        String fileName = file.getOriginalFilename();
        File staticFolder = new File(staticFolderPath);

        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        if (!staticFolder.exists()) {
            staticFolder.mkdirs();
        }

        // Lưu file vào thư mục static
        File savedFile = new File(staticFolder, fileName);
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(savedFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Xây dựng URL cho file đã lưu
        // URL này phụ thuộc vào cấu hình của ứng dụng web
        // Ví dụ, nếu ứng dụng của bạn phục vụ tài nguyên tĩnh từ gốc "/static/"
        return  fileName;
    }
}
