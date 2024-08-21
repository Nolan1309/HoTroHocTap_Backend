package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.DocumentDTO;
import com.example.hotrohoctapbackend.dao.GeneralDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeneralDocumentsService {
    @Autowired
    private GeneralDocumentRepository generalDocumentRepository;

//    @Cacheable(value = "documentList", key = "#list")
    public Page<Object[]> getDocuments(Pageable pageable) {
        return generalDocumentRepository.findDocumentsAll(pageable);
    }

    public Object[] getDocumentsByIDDanhMuc(int id) {
        return generalDocumentRepository.getDocumentByID(id);
    }

//    @Cacheable(value = "documentss", key = "#categoryId") Fillter tai lieu theo danh muc
    public Page<Object[]> getDocumentsByCategory(Long categoryId, Pageable pageable) {
        return generalDocumentRepository.findDocumentsByCategory(categoryId, pageable);
    }
    public Page<Object[]> getDocumentsWithTitle(String title,  Pageable pageable) {
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


}
