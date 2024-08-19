package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.service.CategoryService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseStorageService {

    private static final String BUCKET_NAME = "learn-with-tms-4cc08.appspot.com";
    @Autowired
    private CategoryService categoryService;
    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setProjectId("learn-with-tms-4cc08").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to generate unique file names
    private String generateFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    // Method to get download URL
    private String getDownloadUrl(BlobInfo blobInfo) {
        try {
            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                    blobInfo.getBucket(),
                    URLEncoder.encode(blobInfo.getName(), StandardCharsets.UTF_8.toString()),
                    blobInfo.getMetadata().get("firebaseStorageDownloadTokens"));
        } catch (Exception e) {
            throw new RuntimeException("Error generating download URL", e);
        }
    }

    public GeneralDocument uploadFile(MultipartFile file, String title, String description, int idCategory) throws IOException {
        String imageName = generateFileName(file.getOriginalFilename());
        String folderName = "document/";
        String fullFileName = folderName + imageName;

        // Set metadata and create blob info
        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getInputStream());
        String fileUrl = getDownloadUrl(blobInfo);
        Category category = categoryService.getCategoryById(idCategory);
        GeneralDocument document = new GeneralDocument();
        document.setTitle(title);
        document.setDescription(description);
        document.setUrl(fileUrl);
        document.setCategory(category);
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        return document;
    }
}
