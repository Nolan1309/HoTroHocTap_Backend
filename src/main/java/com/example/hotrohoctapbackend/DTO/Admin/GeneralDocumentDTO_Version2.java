package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GeneralDocumentDTO_Version2 {
    private Integer documentId;
    private String documentTitle;
    private String documentDescription;
    private String documentUrl;
    private boolean deleted;
    private String categoryLevel1;
    private String categoryLevel2;
    private String categoryLevel3;
    private Integer category_id;
    private Integer view;
    private LocalDateTime createAt;
    private Boolean status;

//    public GeneralDocumentDTO_Version2(Integer documentId, String documentTitle, String documentDescription, String documentUrl, boolean deleted, String categoryLevel1, String categoryLevel2, String categoryLevel3, Integer category_id) {
//        this.documentId = documentId;
//        this.documentTitle = documentTitle;
//        this.documentDescription = documentDescription;
//        this.documentUrl = documentUrl;
//        this.deleted = deleted;
//        this.categoryLevel1 = categoryLevel1;
//        this.categoryLevel2 = categoryLevel2;
//        this.categoryLevel3 = categoryLevel3;
//        this.category_id = category_id;
//    }

    public GeneralDocumentDTO_Version2(Integer documentId, String documentTitle, String documentDescription, String documentUrl, boolean deleted, String categoryLevel1, String categoryLevel2, String categoryLevel3, Integer category_id, Integer view, LocalDateTime createAt, Boolean status) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.documentDescription = documentDescription;
        this.documentUrl = documentUrl;
        this.deleted = deleted;
        this.categoryLevel1 = categoryLevel1;
        this.categoryLevel2 = categoryLevel2;
        this.categoryLevel3 = categoryLevel3;
        this.category_id = category_id;
        this.view = view;
        this.createAt = createAt;
        this.status = status;
    }
}
