package com.example.hotrohoctapbackend.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class CourseDTO {
    private Integer id;
    private Integer id_danhmuc;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal cost;
    private Long numberOfStudents;
    private Long totalLessons;
    private BigDecimal averageRating;

    public CourseDTO(Integer id, Integer id_danhmuc, String title, String imageUrl, BigDecimal price,BigDecimal cost, Long numberOfStudents, Long totalLessons, BigDecimal averageRating) {
        this.id = id;
        this.id_danhmuc = id_danhmuc;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.cost = cost;
        this.numberOfStudents = numberOfStudents;
        this.totalLessons = totalLessons;
        this.averageRating = averageRating;
    }
    public CourseDTO() {
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_danhmuc() {
        return id_danhmuc;
    }

    public void setId_danhmuc(Integer id_danhmuc) {
        this.id_danhmuc = id_danhmuc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Long getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Long totalLessons) {
        this.totalLessons = totalLessons;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", id_danhmuc=" + id_danhmuc +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", numberOfStudents=" + numberOfStudents +
                ", totalLessons=" + totalLessons +
                ", averageRating=" + averageRating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return id.equals(courseDTO.id) &&
                id_danhmuc.equals(courseDTO.id_danhmuc) &&
                title.equals(courseDTO.title) &&
                imageUrl.equals(courseDTO.imageUrl) &&
                price.equals(courseDTO.price) &&
                numberOfStudents.equals(courseDTO.numberOfStudents) &&
                totalLessons.equals(courseDTO.totalLessons) &&
                averageRating.equals(courseDTO.averageRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id_danhmuc, title, imageUrl, price, numberOfStudents, totalLessons, averageRating);
    }
}
