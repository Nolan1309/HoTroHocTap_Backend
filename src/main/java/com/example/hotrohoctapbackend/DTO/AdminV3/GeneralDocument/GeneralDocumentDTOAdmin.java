package com.example.hotrohoctapbackend.DTO.AdminV3.GeneralDocument;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GeneralDocumentDTOAdmin {
    private int id;
    private String title;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String format;
    private String size;
    private int view;
    private int downloads;
    private String status;
    private String fileUrl;
    private String description;

    public GeneralDocumentDTOAdmin() {
    }

    public GeneralDocumentDTOAdmin(int id, String title, Integer categoryId, String categoryName, LocalDateTime updatedAt, LocalDateTime createdAt, String format, String size, int view, int downloads, String status, String fileUrl, String description) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.format = format;
        this.size = size;
        this.view = view;
        this.downloads = downloads;
        this.status = status;
        this.fileUrl = fileUrl;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

//    public String getSize() {
//        return size;
//    }
//
//    public void setSize(String size) {
//        this.size = size;
//    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
