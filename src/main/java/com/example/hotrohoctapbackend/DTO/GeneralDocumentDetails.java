package com.example.hotrohoctapbackend.DTO;

public class GeneralDocumentDetails {
    private int id;
    private int idCategory;
    private String url;
    private String title;
    private String description;
    private int idLevel1;
    private int idLevel2;
    private String image_url;

    public GeneralDocumentDetails(int id, int idCategory, String url, String title, String description, int idLevel1, int idLevel2, String image_url) {
        this.id = id;
        this.idCategory = idCategory;
        this.url = url;
        this.title = title;
        this.description = description;
        this.idLevel1 = idLevel1;
        this.idLevel2 = idLevel2;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getIdLevel1() {
        return idLevel1;
    }

    public void setIdLevel1(int idLevel1) {
        this.idLevel1 = idLevel1;
    }

    public int getIdLevel2() {
        return idLevel2;
    }

    public void setIdLevel2(int idLevel2) {
        this.idLevel2 = idLevel2;
    }

    // Constructors
    public GeneralDocumentDetails() {}

    public GeneralDocumentDetails(int id, int idCategory, String url, String title, String description) {
        this.id = id;
        this.idCategory = idCategory;
        this.url = url;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
