package com.example.hotrohoctapbackend.DTO;

import java.util.Objects;

public class GeneralDocumentDTO {
    private Integer documentId;
    private String documentTitle;
    private String documentDescription;
    private String documentUrl;
    private boolean deleted;
    private String categoryLevel1;
    private String categoryLevel2;
    private String categoryLevel3;

    public GeneralDocumentDTO(Integer documentId, String documentTitle, String documentDescription, String documentUrl, boolean deleted, String categoryLevel1, String categoryLevel2, String categoryLevel3) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.documentDescription = documentDescription;
        this.documentUrl = documentUrl;
        this.deleted = deleted;
        this.categoryLevel1 = categoryLevel1;
        this.categoryLevel2 = categoryLevel2;
        this.categoryLevel3 = categoryLevel3;
    }

    public GeneralDocumentDTO() {
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCategoryLevel1() {
        return categoryLevel1;
    }

    public void setCategoryLevel1(String categoryLevel1) {
        this.categoryLevel1 = categoryLevel1;
    }

    public String getCategoryLevel2() {
        return categoryLevel2;
    }

    public void setCategoryLevel2(String categoryLevel2) {
        this.categoryLevel2 = categoryLevel2;
    }

    public String getCategoryLevel3() {
        return categoryLevel3;
    }

    public void setCategoryLevel3(String categoryLevel3) {
        this.categoryLevel3 = categoryLevel3;
    }

    @Override
    public String toString() {
        return "GeneralDocumentDTO{" +
                "documentId=" + documentId +
                ", documentTitle='" + documentTitle + '\'' +
                ", documentDescription='" + documentDescription + '\'' +
                ", documentUrl='" + documentUrl + '\'' +
                ", deleted=" + deleted +
                ", categoryLevel1='" + categoryLevel1 + '\'' +
                ", categoryLevel2='" + categoryLevel2 + '\'' +
                ", categoryLevel3='" + categoryLevel3 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralDocumentDTO that = (GeneralDocumentDTO) o;
        return deleted == that.deleted &&
                Objects.equals(documentId, that.documentId) &&
                Objects.equals(documentTitle, that.documentTitle) &&
                Objects.equals(documentDescription, that.documentDescription) &&
                Objects.equals(documentUrl, that.documentUrl) &&
                Objects.equals(categoryLevel1, that.categoryLevel1) &&
                Objects.equals(categoryLevel2, that.categoryLevel2) &&
                Objects.equals(categoryLevel3, that.categoryLevel3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, documentTitle, documentDescription, documentUrl, deleted, categoryLevel1, categoryLevel2, categoryLevel3);
    }
}
