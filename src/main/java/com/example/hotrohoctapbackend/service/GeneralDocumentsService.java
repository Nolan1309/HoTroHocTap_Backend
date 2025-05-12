package com.example.hotrohoctapbackend.service;

import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.Admin.GeneralDocumentDTO_Version2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCourseDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminDocumentDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLesssonDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.GeneralDocument.GeneralDocumentDTOAdmin;
import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentDTO_User;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.dao.GeneralDocumentRepository;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.util.ByteArrayMultipartFile;
import com.example.hotrohoctapbackend.util.FirebaseStorageService;
import jakarta.persistence.EntityNotFoundException;
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

    public GeneralDocument getDocumentById(Integer id) {
        return generalDocumentRepository.findById(id).get();
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
    }


    // Phương thức lấy tài liệu phổ biến, mới nhất hoặc đề xuất
    public Page<GeneralDocumentDTOAdmin> getDocuments(String type, String title, Integer categoryId, String format, int minView, int minDownload, int page, int size) {
        // Tạo đối tượng PageRequest với thông tin trang và kích thước trang
        PageRequest pageable = PageRequest.of(page, size);

        // Chuyển đổi format về chữ thường nếu nó không phải null, nếu null thì không làm gì cả
        String formatUPCASE = format != null ? format.toLowerCase() : null;

        // Tùy thuộc vào loại tài liệu yêu cầu (popular, new, recommended)
        Page<GeneralDocument> generalDocumentDTOAdmins = null;

        switch (type != null ? type.toLowerCase() : "") {
            case "popular":
                generalDocumentDTOAdmins = generalDocumentRepository.findMostPopularDocuments(title, categoryId, formatUPCASE, pageable);
                break;
            case "new":
                generalDocumentDTOAdmins = generalDocumentRepository.findLatestDocuments(title, categoryId, formatUPCASE, pageable);
                break;
            case "recommended":
                generalDocumentDTOAdmins = generalDocumentRepository.findRecommendedDocuments(title, categoryId, formatUPCASE, minView, minDownload, pageable);
                break;
            default:
                // Nếu không có loại nào khớp, trả về popular theo mặc định
                generalDocumentDTOAdmins = generalDocumentRepository.findMostPopularDocuments(title, categoryId, formatUPCASE, pageable);
                break;
        }

        // Chuyển đổi từ Entity sang DTO và trả về kết quả
        return generalDocumentDTOAdmins.map(this::convertToDTO);
    }


    public Page<GeneralDocumentDTOAdmin> getGeneralDocuments(String title, String status, Pageable pageable) {
        Page<GeneralDocument> generalDocumentPage = generalDocumentRepository.findGeneralDocumentsByFilters(title, status, pageable);

        // Convert from entity to DTO
        return generalDocumentPage.map(this::convertToDTO);
    }

    public GeneralDocumentDTOAdmin getDocumentByIdConvert(int id) {
        GeneralDocument generalDocument = generalDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GeneralDocument not found with id: " + id));
        return convertToDTO(generalDocument);
    }


    private GeneralDocumentDTOAdmin convertToDTO(GeneralDocument generalDocument) {
        GeneralDocumentDTOAdmin dto = new GeneralDocumentDTOAdmin();
        dto.setId(generalDocument.getId());
        dto.setTitle(generalDocument.getTitle());
        dto.setCategoryId(generalDocument.getCategory().getId());
        dto.setCategoryName(generalDocument.getCategory().getName());
        dto.setFileUrl(generalDocument.getUrl());
        dto.setUpdatedAt(generalDocument.getUpdatedAt()); // Assuming this is stored in entity
        dto.setCreatedAt(generalDocument.getCreatedAt()); // Format to string if needed
        dto.setFormat(generalDocument.getFormat());
        dto.setSize(generalDocument.getSize()); // Size if relevant
        dto.setView(generalDocument.getView());
        dto.setDescription(generalDocument.getDescription());
        dto.setDownloads(generalDocument.getGeneralDocumentAcounts().size());
        dto.setStatus(generalDocument.getStatus());
        return dto;
    }


    public List<DocumentRelateUserDTO> getDocumentsByCategoryId(Long categoryId) {
        List<Object[]> list = generalDocumentRepository.findDocumentSummariesByCategoryId(categoryId);
        List<DocumentRelateUserDTO> listDocument = new ArrayList<>();
        for (Object[] item : list) {
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
                        (Integer) row[8],
                        (Integer) row[9],    // categoryLevel2
                        convertTimestampToLocalDateTime(row[10]),     // categoryLevel3
                        (Boolean) row[11]
                ))
                .collect(Collectors.toList());

        // Return the mapped result as a Page
        return new PageImpl<>(documentWithCategoriesList, pageable, resultsPage.getTotalElements());
    }

    public Page<GeneralDocumentDTO_Version2> getDocumentsWithCategoriesSearch(Integer categoryId1, Integer categoryId2, Integer categoryId3, String searchTerm, Pageable pageable) {
        Page<Object[]> resultsPage = generalDocumentRepository.findDocumentsWithCategoriesSearch(categoryId1, categoryId2, categoryId3, searchTerm, pageable);

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
                        (Integer) row[8],
                        (Integer) row[9],    // categoryLevel2
                        convertTimestampToLocalDateTime(row[10]),     // categoryLevel3
                        (Boolean) row[11]
                ))
                .collect(Collectors.toList());

        // Return the mapped result as a Page
        return new PageImpl<>(documentWithCategoriesList, pageable, resultsPage.getTotalElements());
    }


    public GeneralDocument saveDocument(MultipartFile file, String title, String description, int idCategory, MultipartFile thumbnail, String status) throws Exception {
        GeneralDocument generalDocument = firebaseStorageService.uploadFile(file, title, description, idCategory);
        generalDocument.setImage_url(firebaseStorageService.uploadFileImage(thumbnail));
        generalDocument.setStatus(status);
        return generalDocumentRepository.save(generalDocument);
    }

    public Boolean updateGeneralDocument(GeneralDocument updateRequest) {
        GeneralDocument update = generalDocumentRepository.saveAndFlush(updateRequest);
        if (update == null) {
            return false;
        }
        return true;
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

    public GeneralDocument updateStatusActive(int documentID) {
        Optional<GeneralDocument> accountOpt = generalDocumentRepository.findById(documentID);
        if (accountOpt.isPresent()) {
            GeneralDocument account = accountOpt.get();
            account.setStatus("ACTIVE");
            return generalDocumentRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + documentID);
        }
    }

    public GeneralDocument updateStatusInActive(int documentID) {
        Optional<GeneralDocument> accountOpt = generalDocumentRepository.findById(documentID);
        if (accountOpt.isPresent()) {
            GeneralDocument account = accountOpt.get();
            account.setStatus("INACTIVE");
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

    public GeneralDocumentDTOAdmin incrementViewCount(int documentId) {
        GeneralDocument document = generalDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));
        document.setView(document.getView() + 1);
        return convertToDTO(generalDocumentRepository.save(document));
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminDocumentDTORestoreList> getGeneralDocument(Integer categoryId1, Integer categoryId2, Integer categoryId3, String title, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = generalDocumentRepository.findGeneralDocumentsBy(categoryId1, categoryId2, categoryId3, title, deletedDate, pageable);
        List<AdminDocumentDTORestoreList> adminDocumentDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminDocumentDTORestoreList dto = new AdminDocumentDTORestoreList();
            dto.setId((Integer) result[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[1]);
            dto.setCreatedAt(createAt);
            dto.setDescription((String) result[2]);

            dto.setImage((String) result[3]);
            dto.setTitle((String) result[4]);

            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[5]);
            dto.setUpdatedAt(updateAt);


            dto.setUrl((String) result[6]);

            dto.setView((Integer) result[7]);
            dto.setIdCategory((Integer) result[8]);

            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[9]);
            dto.setDeletedDate(deleteAt);
            dto.setIsDeleted((Boolean) result[10]);
            dto.setStatus((Boolean) result[11]);
            adminDocumentDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminDocumentDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public GeneralDocument updateRestoreDocument(AdminDocumentDTORestoreList adminDocumentDTORestoreList) {
        Optional<GeneralDocument> accountOptional = generalDocumentRepository.findById(adminDocumentDTORestoreList.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Document not found with id: " + adminDocumentDTORestoreList.getId());
        } else {
            GeneralDocument generalDocument = accountOptional.get();
            generalDocument.setDeleted(false);
            return generalDocumentRepository.save(generalDocument);
        }
    }

    public void deleteRestoreDocument(AdminDocumentDTORestoreList adminDocumentDTORestoreList) {
        Optional<GeneralDocument> generalDocument = generalDocumentRepository.findById(adminDocumentDTORestoreList.getId());
        if (generalDocument.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + adminDocumentDTORestoreList.getId());
        } else {
            generalDocumentRepository.delete(generalDocument.get());
        }
    }

}
