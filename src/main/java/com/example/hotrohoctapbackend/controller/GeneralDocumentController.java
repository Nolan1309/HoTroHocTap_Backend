package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.GeneralDocumentDTO;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.DTO.UpdateDocumentRequest;
import com.example.hotrohoctapbackend.service.services.GeneralDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GeneralDocumentController {

    @Autowired
    private GeneralDocumentService generalDocumentService;

    @GetMapping("/documents-with-categories")
    public List<GeneralDocumentDTO> getDocumentsWithCategories() {
        return generalDocumentService.getDocumentsWithCategories();
    }
    @PostMapping("/upload")
    public ResponseEntity<GeneralDocument> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") int categoryId) {
        try {
            GeneralDocument document = generalDocumentService.saveDocument(file, title, description, categoryId);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/generaldocuments/{id}")
    public ResponseEntity<GeneralDocument> updateGeneralDocument(
            @PathVariable("id") int id,
            @RequestBody UpdateDocumentRequest updateRequest) {
        GeneralDocument updatedDoc = generalDocumentService.updateGeneralDocument(id, updateRequest);
        if (updatedDoc != null) {
            return ResponseEntity.ok(updatedDoc);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
