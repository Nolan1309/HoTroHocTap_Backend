package com.example.hotrohoctapbackend.service;

import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.Admin.GeneralDocumentDTO_Version2;
import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentDTO_User;
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
import org.springframework.data.domain.PageImpl;
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

//    @Cacheable(value = "documentlist", key = "#pageable.pageNumber")
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

    public List<DocumentRelateUserDTO> getDocumentsByCategoryId(Long categoryId) {
        List<Object[]> list = generalDocumentRepository.findDocumentSummariesByCategoryId(categoryId);
        List<DocumentRelateUserDTO> listDocument = new ArrayList<>();
        for (Object[] item : list){
            DocumentRelateUserDTO documentRelateUserDTO = new DocumentRelateUserDTO();
            documentRelateUserDTO.setId(((Number) item[0]).intValue());
            documentRelateUserDTO.setTitle((String) item[1]);
            documentRelateUserDTO.setTotalDownload(((Number) item[2]).intValue());
            documentRelateUserDTO.setTotalView(((Number) item[3]).intValue());
            listDocument.add(documentRelateUserDTO);
        }
        return listDocument;
    }

//    @Cacheable(value = "search", key = "'documentsData_100'")
    public List<Object[]> getDocumentsData_100() {
        return generalDocumentRepository.findTop100Documents();
    }

//    @Cacheable(value = "all", key = "#pageable.pageNumber")
    public Page<Object> getAll(Pageable pageable) {
        return generalDocumentRepository.GetAll(pageable);
    }

    public Object[] getDocumentsByIDDanhMuc(int id) {
        return generalDocumentRepository.getDocumentByID(id);
    }

//    @Cacheable(value = "documentbycategory", key = "#categoryId + '-' + #pageable.pageNumber")
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
    public Page<GeneralDocumentDTO_Version2> getDocumentsWithCategories(Pageable pageable) {
        Page<Object[]> resultsPage = generalDocumentRepository.findDocumentsWithCategories(pageable);

        // Map the content of the Page<Object[]> to a list of GeneralDocumentDTO
        List<GeneralDocumentDTO_Version2> documentWithCategoriesList = resultsPage.getContent().stream()
                .map(row -> new GeneralDocumentDTO_Version2(
                        (Integer) row[0],   // documentId
                        (String) row[1],    // documentTitle
                        (String) row[2],    // documentDescription
                        (String) row[3],    // documentUrl
                        (Boolean) row[4],   // deleted
                        (String) row[5],    // categoryLevel1
                        (String) row[6],    // categoryLevel2
                        (String) row[7],     // categoryLevel3
                        (Integer)row[8]
                ))
                .collect(Collectors.toList());

        // Return the mapped result as a Page
        return new PageImpl<>(documentWithCategoriesList, pageable, resultsPage.getTotalElements());
    }

    public GeneralDocument saveDocument(MultipartFile file, String title, String description, int idCategory, MultipartFile thumbnail) throws Exception {
        GeneralDocument generalDocument = firebaseStorageService.uploadFile(file, title, description, idCategory);
        generalDocument.setImage_url(firebaseStorageService.uploadFileImage(thumbnail));
        return generalDocumentRepository.save(generalDocument);
    }

    public GeneralDocument updateGeneralDocument(int id, UpdateDocumentRequest updateRequest) {
        Optional<GeneralDocument> existingDocumentOpt = generalDocumentRepository.findById(id);
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
    public Page<GeneralDocumentDTO_User> getDocumentsByAccountIdUser(Long accountId, int page, int size) {
        int offset = page * size;

        List<Object[]> results = generalDocumentRepository.findDocumentsByAccountIdUser(accountId, offset, size);

        List<GeneralDocumentDTO_User> documents = results.stream().map(record -> {
            GeneralDocumentDTO_User dto = new GeneralDocumentDTO_User();
            dto.setDocumentId(((Integer) record[0]));
            dto.setTitle((String) record[1]);
            dto.setDateDownload(record[2].toString());
            dto.setUrl((String) record[3]);
            return dto;
        }).collect(Collectors.toList());

        long totalElements = generalDocumentRepository.countDocumentsByAccountIdUser(accountId);

        return new PageImpl<>(documents, PageRequest.of(page, size), totalElements);
    }
    public GeneralDocument hideGeneralDocumentAdmin(int documentID) {
        // Tìm tài khoản theo ID
        Optional<GeneralDocument> accountOpt = generalDocumentRepository.findById(documentID);

        if (accountOpt.isPresent()) {
            GeneralDocument account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return generalDocumentRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + documentID);
        }
    }

    public GeneralDocument showGeneralDocumentAdmin(int documentID) {
        // Tìm tài khoản theo ID
        Optional<GeneralDocument> accountOpt = generalDocumentRepository.findById(documentID);

        if (accountOpt.isPresent()) {
            GeneralDocument account = accountOpt.get();
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return generalDocumentRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + documentID);
        }
    }
    public GeneralDocument incrementViewCount(int documentId) {
        GeneralDocument document = generalDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));
        document.setView(document.getView() + 1);
        return generalDocumentRepository.save(document);
    }
}
