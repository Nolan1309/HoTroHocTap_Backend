package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.*;
import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentDTO_User;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.service.GeneralDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/general_documents")
public class GeneralDocumentsController {

    @Autowired
    private GeneralDocumentsService generalDocumentsService;

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
    public Object[] getDocumentsByID(@PathVariable("id") int id) {
        return generalDocumentsService.getDocumentsByIDDanhMuc(id);
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
    public List<GeneralDocumentDTO> getDocumentsWithCategories() {
        return generalDocumentsService.getDocumentsWithCategories();
    }

    //    @PostMapping("/upload")
//    public String uploadDocument(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("categoryId") int categoryId) {
//        try {
//            generalDocumentsService.saveDocument(file, title, description, categoryId);
//            return "Conversion successful!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Conversion failed: " + e.getMessage();
//        }
//    }
    @PostMapping("/upload")
    public ResponseEntity<GeneralDocument> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("thumbnail") MultipartFile thumbnail) {
        try {
            GeneralDocument document = generalDocumentsService.saveDocument(file, title, description, categoryId, thumbnail);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/generaldocuments-update/{id}")
    public ResponseEntity<GeneralDocument> updateGeneralDocument(
            @PathVariable("id") int id,
            @RequestBody UpdateDocumentRequest updateRequest) {
        GeneralDocument updatedDoc = generalDocumentsService.updateGeneralDocument(id, updateRequest);
        if (updatedDoc != null) {
            return ResponseEntity.ok(updatedDoc);
        } else {
            return ResponseEntity.notFound().build();
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

}
