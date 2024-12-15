package com.example.hotrohoctapbackend.backup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class BackupRestoreService {

    public ResponseEntity<byte[]> backupDatabase() {
        try {
            // Đảm bảo đường dẫn tệp sao lưu và lệnh mysqldump chính xác
            String backupFilePath = "C:/data/backup.sql"; // Đường dẫn tệp sao lưu
            String username = "root"; // Tên người dùng MySQL
            String password = "your_password"; // Mật khẩu MySQL
            String databaseName = "hotrohoctap2"; // Tên cơ sở dữ liệu
            String host = "your_host"; // Địa chỉ máy chủ từ xa, ví dụ "localhost" hoặc "ip_address"
            String command = "mysqldump -h " + host + " -u " + username + " -p" + password + " " + databaseName;

            // Sử dụng ProcessBuilder để thực thi lệnh và chuyển hướng đầu ra
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectOutput(new File(backupFilePath));  // Chuyển hướng đầu ra vào file
            processBuilder.redirectErrorStream(true);  // Gộp lỗi vào đầu ra

            Process process = processBuilder.start();
            process.waitFor();  // Đợi lệnh hoàn thành

            // Đọc tệp sao lưu và trả về dưới dạng byte[]
            File backupFile = new File(backupFilePath);
            byte[] fileContent = new byte[(int) backupFile.length()];
            try (FileInputStream fileInputStream = new FileInputStream(backupFile)) {
                fileInputStream.read(fileContent);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=backup.sql");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileContent.length)
                    .body(fileContent);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Log lỗi để kiểm tra chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Backup failed: " + e.getMessage()).getBytes());
        }
    }

//
//    public BackupResponseDTO restoreDatabase(MultipartFile file) {
//        File tempFile = null;
//
//        try {
//            // Tạo file tạm thời
//            tempFile = new File("F:\\Baocao\\backup2.sql");
//
//            File backupDirectory = new File("F:\\Baocao");
//            if (!backupDirectory.exists()) {
//                backupDirectory.mkdirs();
//            }
//
//            // Lưu tệp từ MultipartFile vào file tạm
//            try (OutputStream os = new FileOutputStream(tempFile)) {
//                os.write(file.getBytes());
//            }
//            String mysqlPath = "C:\\xampp\\mysql\\bin\\mysql.exe";
//            String dbName = "hotrohoctap3";
//            String dbUser = "root";
//            String dbPassword = "";
//
//
//            String cm = mysqlPath + " -u root " + dbName + " < \"" + tempFile.getAbsolutePath();
//
//            // Sử dụng ProcessBuilder để gọi lệnh mongorestore
//            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cm);
//            pb.redirectErrorStream(true);
//
//            Process process = pb.start();
//
//            // Đọc output và log
//            StringBuilder outputLog = new StringBuilder();
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    outputLog.append(line).append("\n");
//                    System.out.println(line);  // Log thông tin tiêu chuẩn
//                }
//            }
//
//            // Đọc lỗi nếu có
//            StringBuilder errorLog = new StringBuilder();
//            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                String line;
//                while ((line = errorReader.readLine()) != null) {
//                    errorLog.append(line).append("\n");
//                    System.err.println(line);  // Log thông báo lỗi
//                }
//            }
//
//            // Đợi quá trình hoàn tất
//            int processComplete = process.waitFor();
//            System.out.println("Process exit code: " + processComplete); // In mã thoát của tiến trình
//
//            // Nếu mã thoát khác 0, có thể có lỗi xảy ra
//            if (processComplete != 0) {
//                tempFile.delete();
//                return new BackupResponseDTO("Restore failed: " + errorLog.toString(), false);
//            }
//
//            // Trả về kết quả thành công nếu không có lỗi
//            return new BackupResponseDTO("Restore successful!\n" + outputLog.toString(), true);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//
//            // Xóa tệp sao lưu tạm sau khi gặp lỗi
//            if (tempFile != null && tempFile.exists()) {
//                tempFile.delete();
//            }
//
//            return new BackupResponseDTO("Restore failed: " + e.getMessage(), false);
//        }
//    }

//    public BackupResponseDTO restoreDatabase2(String path) {
//        try {
//            String mysqlPath = "C:\\xampp\\mysql\\bin\\mysql.exe";
//            String dbName = "hotrohoctap5";
//            String dbUser = "root";
//            String dbPassword = "";
//
//
//            String command = String.format("\"%s\" -u %s %s < \"%s\"",
//                    mysqlPath, dbUser, dbName, path);
//
//
//            String cm = mysqlPath + " -u root hotrohoctap5 < \"C:\\Users\\Admin\\Downloads\\backup (9).sql";
//
//            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cm);
//
//            processBuilder.redirectErrorStream(true);
//
//            Process process = processBuilder.start();
//
//            // Đọc output
//            StringBuilder outputLog = new StringBuilder();
//            try (BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    outputLog.append(line).append("\n");
//                    System.out.println(line);
//                }
//            }
//
//            // Đọc lỗi nếu có
//            StringBuilder errorLog = new StringBuilder();
//            try (BufferedReader errorReader = new BufferedReader(
//                    new InputStreamReader(process.getErrorStream()))) {
//                String line;
//                while ((line = errorReader.readLine()) != null) {
//                    errorLog.append(line).append("\n");
//                    System.err.println(line);
//                }
//            }
//
//            // Chờ tiến trình hoàn tất
//            int processComplete = process.waitFor();
//            if (processComplete == 0) {
//                return new BackupResponseDTO("Restore successful!\n" + outputLog.toString(), true);
//            } else {
//                return new BackupResponseDTO("Restore failed: \n" + errorLog.toString(), false);
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            return new BackupResponseDTO("Restore failed: " + e.getMessage(), false);
//        }
//    }




    public BackupResponseDTO restoreDatabase(MultipartFile file) {
        File tempFile = null;

        try {
            // Tạo file tạm thời
            tempFile = new File("F:\\Baocao\\backup2.sql");

            File backupDirectory = new File("F:\\Baocao");
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs();
            }

            // Lưu tệp từ MultipartFile vào file tạm
            try (OutputStream os = new FileOutputStream(tempFile)) {
                os.write(file.getBytes());
            }
            String mysqlPath = "C:\\xampp\\mysql\\bin\\mysql.exe";
            String dbName = "hotrohoctap3";
            String dbUser = "root";
            String dbPassword = "";


            String cm = mysqlPath + " -u root " + dbName + " < \"" + tempFile.getAbsolutePath();

            // Sử dụng ProcessBuilder để gọi lệnh mongorestore
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cm);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Đọc output và log
            StringBuilder outputLog = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputLog.append(line).append("\n");
                    System.out.println(line);  // Log thông tin tiêu chuẩn
                }
            }

            // Đọc lỗi nếu có
            StringBuilder errorLog = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorLog.append(line).append("\n");
                    System.err.println(line);  // Log thông báo lỗi
                }
            }

            // Đợi quá trình hoàn tất
            int processComplete = process.waitFor();
            System.out.println("Process exit code: " + processComplete); // In mã thoát của tiến trình

            // Nếu mã thoát khác 0, có thể có lỗi xảy ra
            if (processComplete != 0) {
                tempFile.delete();
                return new BackupResponseDTO("Restore failed: " + errorLog.toString(), false);
            }

            // Trả về kết quả thành công nếu không có lỗi
            return new BackupResponseDTO("Restore successful!\n" + outputLog.toString(), true);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

            // Xóa tệp sao lưu tạm sau khi gặp lỗi
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }

            return new BackupResponseDTO("Restore failed: " + e.getMessage(), false);
        }
    }

}
