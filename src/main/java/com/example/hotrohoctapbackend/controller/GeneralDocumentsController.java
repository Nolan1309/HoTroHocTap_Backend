package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.Admin.GeneralDocumentDTO_Version2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCourseDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminDocumentDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLesssonDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Account.AccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Blog.BlogDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.GeneralDocument.GeneralDocumentDTOAdmin;
import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentDTO_User;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CategoryService;
import com.example.hotrohoctapbackend.service.GeneralDocumentsService;
import com.example.hotrohoctapbackend.util.FirebaseStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/general_documents")
public class GeneralDocumentsController {

    @Autowired
    private GeneralDocumentsService generalDocumentsService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @GetMapping
    public ApiResponse<Page<GeneralDocumentDTOAdmin>> getGeneralDocuments(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GeneralDocumentDTOAdmin> generalDocuments = generalDocumentsService.getGeneralDocuments(title, status, pageable);

        return new ApiResponse<>(200, "General documents fetched successfully", generalDocuments);
    }

    @GetMapping("/public")
    public ApiResponse<Page<GeneralDocumentDTOAdmin>> getDocuments(
            @RequestParam(required = false) String type, // type = "popular", "latest", "recommended"
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String format,
            @RequestParam(defaultValue = "0") int minView,
            @RequestParam(defaultValue = "0") int minDownload,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Gọi Service để lấy tài liệu theo loại
        Page<GeneralDocumentDTOAdmin> documents = generalDocumentsService.getDocuments(type, title, categoryId, format, minView, minDownload, page, size);

        // Trả về dữ liệu dưới dạng ApiResponse
        return new ApiResponse<>(200, "Success", documents);
    }


    @GetMapping("/create_desc")
    public List<DocumentDTO> getAllDocumentsCreateDesc() {
        return generalDocumentsService.getAllDocumentsWithDownloadCountOrderedByDateDesc();
    }

    @GetMapping("/view_desc")
    public List<DocumentDTO> getAllDocumentsViewDesc() {
        return generalDocumentsService.getAllDocumentsWithDownloadCountOrderedByViewDesc();
    }

    @GetMapping("/download_desc")
    public List<DocumentDTO> getAllDocuments() {
        return generalDocumentsService.getAllDocumentsWithDownloadCountOrderedByDownloadCountDesc();
    }

    @GetMapping("/view_desc/top6")
    public List<DocumentDTO> getAllDocumentTop6_View_Desc() {
        return generalDocumentsService.getAllDocumentWithViewDescTop6();
    }


    @GetMapping("/all-general-document")
    public Page<Object[]> getDocuments(Pageable pageable) {
        return generalDocumentsService.getDocuments(pageable);
    }

    @GetMapping("/{id}")
    public ApiResponse<GeneralDocumentDTOAdmin> getDocumentsByID(@PathVariable("id") int id) {
        try {
            GeneralDocumentDTOAdmin documentDTO = generalDocumentsService.getDocumentByIdConvert(id);
            return new ApiResponse<>(200, "Success", documentDTO); // Trả về mã trạng thái 200 OK cùng dữ liệu
        } catch (EntityNotFoundException e) {
            return new ApiResponse<>(404, "Document not found", null); // Trả về mã trạng thái 404 khi không tìm thấy tài liệu
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal Server Error: " + e.getMessage(), null); // Xử lý lỗi 500
        }
    }


    @GetMapping("/category")
    public Page<Object[]> getDocumentsByCategory(@RequestParam Long id, Pageable pageable) {
        return generalDocumentsService.getDocumentsByCategory(id, pageable);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<DocumentRelateUserDTO>> getDocumentsByCategoryId(@PathVariable Long categoryId) {
        List<DocumentRelateUserDTO> documents = generalDocumentsService.getDocumentsByCategoryId(categoryId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/data")
    public List<Object[]> getDocumentsDataRange_100() {
        return generalDocumentsService.getDocumentsData_100();
    }

    @GetMapping("/search")
    public Page<Object[]> getDocumentsSearch(@RequestParam String title, Pageable pageable) {
        return generalDocumentsService.getDocumentsWithTitle(title, pageable);
    }

    // API tìm kiếm với từ khóa và phân trang
    @GetMapping("/search-query")
    public Page<GeneralDocumentSearch> searchDocuments(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "16") int size) {

        return generalDocumentsService.searchDocuments(keyword, PageRequest.of(page, size));
    }

    @GetMapping("/all")
    public Page<Object> getAll(Pageable pageable) {
        return generalDocumentsService.getAll(pageable);
    }


    ///MAN
    @GetMapping("/documents-with-categories")
    public Page<GeneralDocumentDTO_Version2> getDocumentsWithCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return generalDocumentsService.getDocumentsWithCategories(pageable);
    }

    @GetMapping("/documents-with-categories-search")
    public Page<GeneralDocumentDTO_Version2> getDocumentsWithCategoriesSearch(
            @RequestParam(required = false) Integer categoryId1,
            @RequestParam(required = false) Integer categoryId2,
            @RequestParam(required = false) Integer categoryId3,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return generalDocumentsService.getDocumentsWithCategoriesSearch(categoryId1, categoryId2, categoryId3, searchTerm, pageable);
    }

    @PostMapping("/upload")
    public ResponseEntity<GeneralDocument> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("status") String status,
            @RequestParam("thumbnail") MultipartFile thumbnail) {
        try {
            GeneralDocument document = generalDocumentsService.saveDocument(file, title, description, categoryId, thumbnail, status);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/generaldocuments-update/{id}")
    public ResponseEntity<ApiResponse<?>> updateGeneralDocument(
            @PathVariable("id") int id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("status") String status,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) {

        try {


            GeneralDocument generalDocument = generalDocumentsService.getDocumentById(id);
            if (generalDocument == null) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "No found document", null), HttpStatus.BAD_REQUEST);
            }
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "No found category", null), HttpStatus.BAD_REQUEST);
            }
            GeneralDocument generalDocument1 = new GeneralDocument();
            if (file != null && !file.isEmpty()) {
                generalDocument1 = firebaseStorageService.uploadFileURL(generalDocument, file);
                generalDocument1.setImage_url(firebaseStorageService.uploadFileImage(thumbnail));
            }
            generalDocument1.setId(generalDocument.getId());
            generalDocument1.setTitle(title);
            generalDocument1.setDescription(description);
            generalDocument1.setCategory(category);
            generalDocument1.setStatus(status);
            generalDocument1.setUpdatedAt(LocalDateTime.now());
            generalDocument1.setGeneralDocumentAcounts(generalDocument.getGeneralDocumentAcounts());

            // Cập nhật tài liệu
            Boolean updatedDoc = generalDocumentsService.updateGeneralDocument(generalDocument1);
            if (updatedDoc) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Successful", null), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Update fail", null), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generaldocuments-details/{id}")
    public ResponseEntity<GeneralDocumentDetails> getDocumentDetails(@PathVariable int id) {
        Optional<GeneralDocumentDetails> documentDetails = generalDocumentsService.getDocumentDetailsById(id);

        if (documentDetails.isPresent()) {
            return ResponseEntity.ok(documentDetails.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/account/view-list")
    public ResponseEntity<Page<GeneralDocumentDTO_User>> getDocuments(
            @RequestParam Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GeneralDocumentDTO_User> documents = generalDocumentsService.getDocumentsByAccountIdUser(accountId, page, size);
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideGeneralDocumentAdmin(@PathVariable int id) {
        try {
            GeneralDocument hidedGeneralDocument = generalDocumentsService.hideGeneralDocumentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showGeneralDocumentAdmin(@PathVariable int id) {
        try {
            GeneralDocument showGeneralDocument = generalDocumentsService.showGeneralDocumentAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/{id}/increment-view")
    public ApiResponse<GeneralDocumentDTOAdmin> incrementViewCount(@PathVariable int id) {
        try {
            GeneralDocumentDTOAdmin updatedDocument = generalDocumentsService.incrementViewCount(id);
            return new ApiResponse<>(200, "Lượt xem của tài liệu đã được tăng!", updatedDocument);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi tăng lượt xem", null);
        }
    }


    @PutMapping("/status/{id}")
    public ResponseEntity<?> statusGeneralDocumentAdmin(@PathVariable int id) {
        try {
            GeneralDocument deletedCourse = generalDocumentsService.updateStatusActive(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @PutMapping("/unstatus/{id}")
    public ResponseEntity<?> instatusGeneralDocumentAdmin(@PathVariable int id) {
        try {
            GeneralDocument deletedCourse = generalDocumentsService.updateStatusInActive(id);
            return ResponseEntity.ok().body("Course with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + id);
        }
    }

    @GetMapping("/restore/list-all-documents")
    public Page<AdminDocumentDTORestoreList> getGeneralDocument(
            @RequestParam(required = false) Integer categoryId1,
            @RequestParam(required = false) Integer categoryId2,
            @RequestParam(required = false) Integer categoryId3,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (title.equals("")) {
            title = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }

        return generalDocumentsService.getGeneralDocument(categoryId1, categoryId2, categoryId3, title, deletedDate, page, size);
    }

    @PutMapping("/restore/{documentId}")
    public ResponseEntity<GeneralDocument> restoreDocument(@PathVariable Integer documentId) {
        AdminDocumentDTORestoreList adminDocumentDTORestoreList = new AdminDocumentDTORestoreList();
        adminDocumentDTORestoreList.setId(documentId);
        GeneralDocument restoredAccount = generalDocumentsService.updateRestoreDocument(adminDocumentDTORestoreList);
        return ResponseEntity.ok(restoredAccount);
    }

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Integer documentId) {
        AdminDocumentDTORestoreList adminDocumentDTORestoreList = new AdminDocumentDTORestoreList();
        adminDocumentDTORestoreList.setId(documentId);
        generalDocumentsService.deleteRestoreDocument(adminDocumentDTORestoreList);
        return ResponseEntity.ok("Document permanently deleted.");
    }
}
