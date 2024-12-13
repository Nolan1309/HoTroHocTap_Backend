package com.example.hotrohoctapbackend.DTO;


import java.math.BigDecimal;
import java.util.Date;
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

    //Update
    private String author;
    private String course_output;
    private Date created_at;
    private Date updated_at;
    private String description;
    private String duration;
    private String language;
    private Boolean status;
    private String type;

    public CourseDTO(Integer id, Integer id_danhmuc, String title, String imageUrl, BigDecimal price, BigDecimal cost, Long numberOfStudents, Long totalLessons, BigDecimal averageRating,String type) {
        this.id = id;
        this.id_danhmuc = id_danhmuc;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.cost = cost;
        this.numberOfStudents = numberOfStudents;
        this.totalLessons = totalLessons;
        this.averageRating = averageRating;
        this.type = type;
    }
    public CourseDTO(Integer id, Integer id_danhmuc, String title, String imageUrl, BigDecimal price, BigDecimal cost, Long numberOfStudents, Long totalLessons, BigDecimal averageRating,String type,Boolean status) {
        this.id = id;
        this.id_danhmuc = id_danhmuc;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.cost = cost;
        this.numberOfStudents = numberOfStudents;
        this.totalLessons = totalLessons;
        this.averageRating = averageRating;
        this.type = type;
        this.status = status;
    }
    public CourseDTO(Integer id, Integer id_danhmuc, String imageUrl, BigDecimal price, BigDecimal cost,
                     String title, Long numberOfStudents, Long totalLessons, BigDecimal averageRating,
                     String author, String course_output, Date created_at, Date updated_at, String description,
                     String duration, String language, Boolean status, String type) {
        this.id = id;
        this.id_danhmuc = id_danhmuc;
        this.imageUrl = imageUrl;
        this.price = price;
        this.cost = cost;
        this.title = title;
        this.numberOfStudents = numberOfStudents;
        this.totalLessons = totalLessons;
        this.averageRating = averageRating;
        this.author = author;
        this.course_output = course_output;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.duration = duration;
        this.language = language;
        this.status = status;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCourse_output() {
        return course_output;
    }

    public void setCourse_output(String course_output) {
        this.course_output = course_output;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
