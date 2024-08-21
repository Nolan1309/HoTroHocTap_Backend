package com.example.hotrohoctapbackend.DTO;
import java.io.Serializable;
import java.time.LocalDateTime;

public class DocumentDTO implements Serializable{

    private Integer documentId;
    private String documentTitle;
    private String image_url;
    private String url;
    private int view;
    private LocalDateTime created_at;
    private int download_count;

    public DocumentDTO(Integer documentId, String documentTitle, String image_url, String url, int view, LocalDateTime created_at, int download_count) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.image_url = image_url;
        this.url = url;
        this.view = view;
        this.created_at = created_at;
        this.download_count = download_count;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }
}
