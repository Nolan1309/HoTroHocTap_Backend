package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.DocumentDTO;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.service.GeneralDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//
//    @GetMapping("/category/{id}")
//    public List<DocumentDTO> getDocumentsByCategory(@PathVariable int id) {
//        return generalDocumentsService.getDocumentsByCategory(id);
//    }

    @GetMapping("/category")
    public Page<Object[]> getDocumentsByCategory(@RequestParam Long id, Pageable pageable) {
        return generalDocumentsService.getDocumentsByCategory(id, pageable);
    }

    @GetMapping("/search")
    public Page<Object[]> getDocumentsSearch(@RequestParam String title, Pageable pageable) {
        return generalDocumentsService.getDocumentsWithTitle(title, pageable);
    }



}
