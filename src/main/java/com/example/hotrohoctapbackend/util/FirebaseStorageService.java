package com.example.hotrohoctapbackend.util;


import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.entity.Video;
import com.example.hotrohoctapbackend.service.CategoryService;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.google.common.io.Files.getFileExtension;

@Service
public class FirebaseStorageService {

    private static final String BUCKET_NAME = "learn-with-tms-4cc08.appspot.com";
    @Autowired
    private CategoryService categoryService;
    private Storage storage;
    @Autowired
    private ConvertDocService convertDocService;
    @Autowired
    private FileService fileService;

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

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type is unknown");
        }

        if (contentType.equals("application/pdf")) {
            System.out.println("Day la PDF nha");
        } else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            System.out.println("Day la DOCX nha");
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }

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

    public String uploadFileURL(MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type is unknown");
        }

        if (contentType.equals("application/pdf")) {
            System.out.println("Day la PDF nha");
        } else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            System.out.println("Day la DOCX nha");
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }

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
        return fileUrl;
    }

    public String uploadFileImage(MultipartFile thumbnail) throws IOException {
        String imageName = generateFileName(thumbnail.getOriginalFilename());
        String folderName = "image/imageDocument/";
        String fullFileName = folderName + imageName;

        // Set metadata and create blob info
        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(thumbnail.getContentType())
                .build();
        storage.create(blobInfo, thumbnail.getInputStream());
        String fileUrl = getDownloadUrl(blobInfo);


        return fileUrl;
    }


    //VIDEO
    public Video saveTest(Video videoInput, MultipartFile videoFile, MultipartFile documentFile) throws IOException {
        String imageName = generateFileName(videoFile.getOriginalFilename());
        String folderName = "video/";
        String fullFileName = folderName + imageName;

        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(videoFile.getContentType())
                .build();
        storage.create(blobInfo, videoFile.getInputStream());
        String fileUrl = getDownloadUrl(blobInfo);

        videoInput.setUrl(fileUrl);
        String documentUrl = saveTestDocument(documentFile);
        videoInput.setDocumentUrl(documentUrl);
        return videoInput;
    }

    public String saveTestVideo(MultipartFile videoFile) throws IOException {
        String imageName = generateFileName(videoFile.getOriginalFilename());
        String folderName = "video/";
        String fullFileName = folderName + imageName;

        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(videoFile.getContentType())
                .build();
        storage.create(blobInfo, videoFile.getInputStream());
        String fileUrl = getDownloadUrl(blobInfo);


        return fileUrl;
    }

    public String saveTestDocument(MultipartFile documentFile) throws IOException {
        String imageName = generateFileName(documentFile.getOriginalFilename());
        String folderName = "documentCourse/";
        String fullFileName = folderName + imageName;

        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(documentFile.getContentType())
                .build();
        storage.create(blobInfo, documentFile.getInputStream());
        String fileUrl = getDownloadUrl(blobInfo);
        return fileUrl;
    }

    private String generateFileNameVideo(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtensionVideo(originalFileName);
    }

    private String getExtensionVideo(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    private String getDownloadUrlVideo(BlobInfo blobInfo) {
        try {
            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                    blobInfo.getBucket(),
                    URLEncoder.encode(blobInfo.getName(), StandardCharsets.UTF_8.toString()),
                    blobInfo.getMetadata().get("firebaseStorageDownloadTokens"));
        } catch (Exception e) {
            throw new RuntimeException("Error generating download URL", e);
        }
    }

    public void deleteVideoServerVideo(String filePath) {
        try {
            boolean deleted = storage.delete(BUCKET_NAME, filePath);
            if (deleted) {
                System.out.println("Video deleted successfully.");
            } else {
                System.out.println("Video not found or couldn't be deleted.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting video: " + e.getMessage());
        }
    }

    public String extractFilePathVideo(String url) {
        try {
            // Giải mã URL
            String decodedUrl = URLDecoder.decode(url, "UTF-8");

            // Tách phần tên tệp từ URL
            // Ví dụ URL: https://firebasestorage.googleapis.com/v0/b/thanhsonlab11.appspot.com/o/video%2Fa8d9a18c-6ad8-4002-b257-5727144a7f39.mp4?alt=media&token=a8d9a18c-6ad8-4002-b257-5727144a7f39.mp4
            String[] parts = decodedUrl.split("/o/");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid URL format");
            }

            // Phần tên tệp
            String filePath = parts[1].split("\\?")[0];

            return filePath;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error decoding URL", e);
        }
    }
}
