package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

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

    public GeneralDocumentDTO_Version2(Integer documentId, String documentTitle, String documentDescription, String documentUrl, boolean deleted, String categoryLevel1, String categoryLevel2, String categoryLevel3, Integer category_id) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.documentDescription = documentDescription;
        this.documentUrl = documentUrl;
        this.deleted = deleted;
        this.categoryLevel1 = categoryLevel1;
        this.categoryLevel2 = categoryLevel2;
        this.categoryLevel3 = categoryLevel3;
        this.category_id = category_id;
    }
}
