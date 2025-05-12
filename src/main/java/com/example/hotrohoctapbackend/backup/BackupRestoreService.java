package com.example.hotrohoctapbackend.backup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BackupRestoreService {

//    public ResponseEntity<byte[]> backupDatabase() {
//        try {
//            // Đường dẫn đầy đủ đến mysqldump
//            String mysqldumpPath = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";
//            String backupFilePath = "C:\\data\\backup.sql"; // Đường dẫn tệp sao lưu
////            String backupFilePath = "C:/data/backup.sql"; // Đường dẫn tệp sao lưu
//
//            String username = "root"; // Tên người dùng MySQL
//            String password = "Thao.10072002@"; // Mật khẩu MySQL
//            String databaseName = "hotrohoctap2"; // Tên cơ sở dữ liệu
//            String host = "localhost"; // Địa chỉ máy chủ
//
//            // Tạo thư mục sao lưu nếu chưa tồn tại
//            new File("C:/data").mkdirs();
//
//            // Đặt mật khẩu trong biến môi trường
//            ProcessBuilder processBuilder = new ProcessBuilder();
//            processBuilder.environment().put("MYSQL_PWD", "Thao.10072002@");
//
//// Cấu hình lệnh mysqldump
////            List<String> command = Arrays.asList(
////                    "mysqldump",
////                    "-h", "localhost",
////                    "-u", "root",
////                    "hotrohoctap2"
////            );
//
//
//
//            List<String> command = Arrays.asList(
//                    mysqldumpPath,
//                    "-h", host,
//                    "-u", username,
//                    databaseName
//            );
////            String commandString = String.format(
////                    mysqldumpPath+" -h %s -u %s -p%s %s > %s",
////                    host, username, password, databaseName, backupFilePath
////            );
//            processBuilder.command(command);
//            // Tạo ProcessBuilder và chuyển hướng đầu ra
////            ProcessBuilder processBuilder = new ProcessBuilder(command);
//            processBuilder.redirectOutput(new File(backupFilePath));  // Lưu vào file
//            processBuilder.redirectErrorStream(true);  // Gộp lỗi vào đầu ra
//
//            // Thực thi lệnh
//            Process process = processBuilder.start();
//            int exitCode = process.waitFor(); // Đợi lệnh hoàn thành
//            if (exitCode != 0) {
//                try (InputStream errorStream = process.getErrorStream();
//                     BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
//                    StringBuilder errorMessage = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        errorMessage.append(line).append("\n");
//                    }
//                    throw new IOException("mysqldump failed: " + errorMessage);
//                }
//            }
//
//            // Đọc file sao lưu và trả về dưới dạng byte[]
//            File backupFile = new File(backupFilePath);
//            byte[] fileContent = new byte[(int) backupFile.length()];
//            try (FileInputStream fileInputStream = new FileInputStream(backupFile)) {
//                fileInputStream.read(fileContent);
//            }
//
//            // Thiết lập header để tải file
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=backup.sql");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(fileContent.length)
//                    .body(fileContent);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace(); // Log lỗi
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(("Backup failed: " + e.getMessage()).getBytes());
//        }
//    }
//    public BackupResponseDTO restoreDatabase(MultipartFile file) {
//        File tempFile = null;
//
//        try {
//            // Tạo file tạm thời
//            tempFile = new File("C:\\data\\temp\\backup.sql");
//
//            File backupDirectory = new File("C:\\data\\temp");
//            if (!backupDirectory.exists()) {
//                backupDirectory.mkdirs();
//            }
//
//            // Lưu tệp từ MultipartFile vào file tạm
//            try (OutputStream os = new FileOutputStream(tempFile)) {
//                os.write(file.getBytes());
//            }
//            String mysqlPath = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe\"";  // Đặt đường dẫn trong dấu nháy kép
//            String dbName = "hotrohoctap3";
//            String dbUser = "root";
////            String dbPassword = "Thao.10072002@";
//            String dbPassword = "";
//
//
////            String cm = String.format("%s -u %s -p%s %s <    \ \"%s\"",
////                    mysqlPath, dbUser, dbPassword, dbName, tempFile.getAbsolutePath());
//
//            String cm = mysqlPath + " -u root " + dbName + " -p"+dbPassword + " < \"" + tempFile.getAbsolutePath();
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

    public ResponseEntity<byte[]> backupDatabase() {
        try {
            // Đường dẫn đầy đủ đến mysqldump
            String mysqldumpPath = "C:\\xampp\\mysql\\bin\\mysqldump.exe";
            String backupFilePath = "C:\\data\\backup.sql"; // Đường dẫn tệp sao lưu
//            String backupFilePath = "C:/data/backup.sql"; // Đường dẫn tệp sao lưu

            String username = "root"; // Tên người dùng MySQL

            String databaseName = "hotrohoctap3"; // Tên cơ sở dữ liệu
            String host = "localhost"; // Địa chỉ máy chủ

            // Tạo thư mục sao lưu nếu chưa tồn tại
            new File("C:/data").mkdirs();

            // Đặt mật khẩu trong biến môi trường
            ProcessBuilder processBuilder = new ProcessBuilder();
//            processBuilder.environment().put("MYSQL_PWD", "Thao.10072002@");
            processBuilder.environment().put("MYSQL_PWD", "");

            List<String> command = Arrays.asList(
                    mysqldumpPath,
                    "-h", host,
                    "-u", username,
                    databaseName
            );
//            String commandString = String.format(
//                    mysqldumpPath+" -h %s -u %s -p%s %s > %s",
//                    host, username, password, databaseName, backupFilePath
//            );
            processBuilder.command(command);
            // Tạo ProcessBuilder và chuyển hướng đầu ra
//            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectOutput(new File(backupFilePath));  // Lưu vào file
            processBuilder.redirectErrorStream(true);  // Gộp lỗi vào đầu ra

            // Thực thi lệnh
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // Đợi lệnh hoàn thành
            if (exitCode != 0) {
                try (InputStream errorStream = process.getErrorStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                    StringBuilder errorMessage = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorMessage.append(line).append("\n");
                    }
                    throw new IOException("mysqldump failed: " + errorMessage);
                }
            }

            // Đọc file sao lưu và trả về dưới dạng byte[]
            File backupFile = new File(backupFilePath);
            byte[] fileContent = new byte[(int) backupFile.length()];
            try (FileInputStream fileInputStream = new FileInputStream(backupFile)) {
                fileInputStream.read(fileContent);
            }

            // Thiết lập header để tải file
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=backup.sql");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileContent.length)
                    .body(fileContent);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Log lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Backup failed: " + e.getMessage()).getBytes());
        }
    }
    public BackupResponseDTO restoreDatabase(MultipartFile file) {
        File tempFile = null;

        try {
            // Tạo file tạm thời
            tempFile = new File("C:\\data\\temp\\backup.sql");

            File backupDirectory = new File("C:\\data\\temp");
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs();
            }

            // Lưu tệp từ MultipartFile vào file tạm
            try (OutputStream os = new FileOutputStream(tempFile)) {
                os.write(file.getBytes());
            }
            String mysqlPath = "C:\\xampp\\mysql\\bin\\mysql.exe";  // Đặt đường dẫn trong dấu nháy kép
            String dbName = "hotrohoctap3";
            String dbUser = "root";
            String dbPassword = "";
//            String cm = mysqlPath + " -u root " + dbName + " < \"" + tempFile.getAbsolutePath();

            String cm = mysqlPath + " -u root " + dbName + " -pThao.10072002@" + "< \"" + tempFile.getAbsolutePath();

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
