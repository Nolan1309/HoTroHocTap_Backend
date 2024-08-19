package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.DTO.GeneralDocumentDTO;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.service.CategoryService;
import com.example.hotrohoctapbackend.dao.GeneralDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.hotrohoctapbackend.DTO.UpdateDocumentRequest;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Optional;

@Service
public class GeneralDocumentService {

    @Autowired
    private GeneralDocumentRepository generalDocumentRepository;
    @Autowired
    private FirebaseStorageService firebaseStorageService;

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

    public GeneralDocument saveDocument(MultipartFile file, String title, String description, int idCategory) throws IOException {
        GeneralDocument generalDocument = firebaseStorageService.uploadFile(file,title,description,idCategory);
        return generalDocumentRepository.save(generalDocument);
    }
    @Autowired
    private CategoryRepository categoryRepository;
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

}
