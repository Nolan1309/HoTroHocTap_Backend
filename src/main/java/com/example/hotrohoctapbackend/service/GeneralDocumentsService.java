package com.example.hotrohoctapbackend.service;

import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.dao.GeneralDocumentRepository;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.util.ByteArrayMultipartFile;
import com.example.hotrohoctapbackend.util.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


@Service
public class GeneralDocumentsService {
    @Autowired
    private GeneralDocumentRepository generalDocumentRepository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Cacheable(value = "documentlist", key = "#pageable.pageNumber")
    public Page<Object[]> getDocuments(Pageable pageable) {
        return generalDocumentRepository.findDocumentsAll(pageable);
    }

    // Hàm tìm kiếm với phân trang
    public Page<GeneralDocumentSearch> searchDocuments(String keyword, Pageable pageable) {
        Page<Object[]> documents = generalDocumentRepository.searchDocumentsByTitleOrCategory(keyword, pageable);
        // Mapping dữ liệu từ Object[] sang GeneralDocumentDTO
        return documents.map(doc -> new GeneralDocumentSearch(
                (int) doc[0], // id


                ((Timestamp) doc[1]).toLocalDateTime(), // created_at
                (String) doc[2], // description
                (String) doc[3], // image
                (String) doc[4], // title
                ((Timestamp) doc[5]).toLocalDateTime(), // updated_at
                (String) doc[6], // url
                (int) doc[7], // view
                (int) doc[8],
                (String) doc[9] // category_name
        ));
//        return generalDocumentRepository.searchDocumentsByTitleOrCategory(keyword, pageable);
    }
    @Cacheable(value = "search", key = "'documentsData_100'")
    public List<Object[]> getDocumentsData_100() {
        return generalDocumentRepository.findTop100Documents();
    }

    @Cacheable(value = "all", key = "#pageable.pageNumber")
    public Page<Object> getAll(Pageable pageable) {
        return generalDocumentRepository.GetAll(pageable);

    }

    public Object[] getDocumentsByIDDanhMuc(int id) {
        return generalDocumentRepository.getDocumentByID(id);
    }

    @Cacheable(value = "documentbycategory", key = "#categoryId + '-' + #pageable.pageNumber")
    public Page<Object[]> getDocumentsByCategory(Long categoryId, Pageable pageable) {
        return generalDocumentRepository.findDocumentsByCategory(categoryId, pageable);
    }


    public Page<Object[]> getDocumentsWithTitle(String title, Pageable pageable) {
        return generalDocumentRepository.findDocumentsWithTitle(title, pageable);
    }

    //View giảm dan
    public List<DocumentDTO> getAllDocumentsWithDownloadCountOrderedByViewDesc() {
        List<Object[]> results = generalDocumentRepository.findAllWithDownloadCountOrderedByViewDesc();
        return results.stream().map(this::mapToDocumentDTO).collect(Collectors.toList());
    }

    //Ngày giảm dan
    public List<DocumentDTO> getAllDocumentsWithDownloadCountOrderedByDateDesc() {
        List<Object[]> results = generalDocumentRepository.findAllWithDownloadCountOrderedByDateDesc();
        return results.stream().map(this::mapToDocumentDTO).collect(Collectors.toList());
    }

    //download giam dan
    public List<DocumentDTO> getAllDocumentsWithDownloadCountOrderedByDownloadCountDesc() {
        List<Object[]> results = generalDocumentRepository.findAllWithDownloadCountOrderedByDownloadCountDesc();
        return results.stream().map(this::mapToDocumentDTO).collect(Collectors.toList());
    }

    private DocumentDTO mapToDocumentDTO(Object[] result) {
        Integer documentId = (Integer) result[0];
        String documentTitle = (String) result[1];
        String image_url = (String) result[2];
        String url = (String) result[3];
        int view = ((Number) result[4]).intValue();
        LocalDateTime created_at = ((java.sql.Timestamp) result[5]).toLocalDateTime();
        int download_count = ((Number) result[6]).intValue();
        return new DocumentDTO(documentId, documentTitle, image_url, url, view, created_at, download_count);
    }

    //View giảm dan with 6 document
    public List<DocumentDTO> getAllDocumentWithViewDescTop6() {
        List<Object[]> results = generalDocumentRepository.findAllWithViewDesc();
        return results.stream().map(this::mapToDocumentDTO).collect(Collectors.toList());
    }

    //Man
    public List<GeneralDocumentDTO> getDocumentsWithCategories() {
        List<Object[]> results = generalDocumentRepository.findDocumentsWithCategories();

        List<GeneralDocumentDTO> documentWithCategoriesList = new ArrayList<>();
        for (Object[] row : results) {
            Integer documentId = (Integer) row[0];
            String documentTitle = (String) row[1];
            String documentDescription = (String) row[2];
            String documentUrl = (String) row[3];
            String categoryLevel1 = (String) row[4];
            String categoryLevel2 = (String) row[5];
            String categoryLevel3 = (String) row[6];

            GeneralDocumentDTO dto = new GeneralDocumentDTO(
                    documentId,
                    documentTitle,
                    documentDescription,
                    documentUrl,
                    categoryLevel1,
                    categoryLevel2,
                    categoryLevel3
            );
            documentWithCategoriesList.add(dto);
        }

        return documentWithCategoriesList;
    }

    public GeneralDocument saveDocument(MultipartFile file, String title, String description, int idCategory, MultipartFile thumbnail) throws Exception {
        GeneralDocument generalDocument = firebaseStorageService.uploadFile(file, title, description, idCategory);
        generalDocument.setImage_url(firebaseStorageService.uploadFileImage(thumbnail));
        return generalDocumentRepository.save(generalDocument);
    }


//    public GeneralDocument saveDocument(MultipartFile file, String title, String description, int idCategory) {
//        GeneralDocument generalDocument;
//        try {
//            generalDocument = firebaseStorageService.uploadFile(file, title, description, idCategory);
//        } catch (IOException | ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error occurred while uploading the file", e);
//        }
//        return generalDocumentRepository.save(generalDocument);
//    }


    public GeneralDocument updateGeneralDocument(int id, UpdateDocumentRequest updateRequest) {
        Optional<GeneralDocument> existingDocumentOpt = Optional.ofNullable(generalDocumentRepository.findById(id));
        if (existingDocumentOpt.isPresent()) {
            GeneralDocument existingDocument = existingDocumentOpt.get();

            Optional<Category> categoryOpt = categoryRepository.findById(updateRequest.getIdCategory());
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                existingDocument.setTitle(updateRequest.getTitle());
                existingDocument.setDescription(updateRequest.getDescription());
                existingDocument.setUrl(updateRequest.getUrl());
                existingDocument.setCategory(category);
                existingDocument.setUpdatedAt(LocalDateTime.now());

                return generalDocumentRepository.save(existingDocument);
            } else {
                System.out.println("Category not found with id: " + updateRequest.getIdCategory());
                return null;
            }
        } else {
            System.out.println("Document not found with id: " + id);
            return null;
        }
    }

    public Optional<GeneralDocumentDetails> getDocumentDetailsById(int id) {
        List<Object[]> results = generalDocumentRepository.findDocumentDetailsById(id);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        Object[] row = results.get(0);

        GeneralDocumentDetails documentDetails = new GeneralDocumentDetails(
                (Integer) row[0],                      // id
                (Integer) row[1],                      // idCategory
                (String) row[2],                       // url
                (String) row[3],                       // title
                (String) row[4],                // description
                (Integer) row[5],
                (Integer) row[6],
                (String) row[7]
        );

        return Optional.of(documentDetails);
    }


}
