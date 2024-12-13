package com.example.hotrohoctapbackend.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/backup")
public class BackupDatabaseController {
    @Autowired
    private BackupRestoreService backupRestoreService;

    // API để backup dữ liệu
    @GetMapping("/export")
    public ResponseEntity<byte[]> backupDatabase() {
        return backupRestoreService.backupDatabase();
    }

    @PostMapping("/restore")
    public BackupResponseDTO restoreDatabase(@RequestParam("file") MultipartFile file) {
        return backupRestoreService.restoreDatabase(file);
    }


}
