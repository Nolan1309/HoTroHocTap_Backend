package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class OptionDTO {
    private String text; // Nội dung đáp án
    private boolean isCorrect; // Đáp án đúng hay không

    public OptionDTO() {
    }

    public OptionDTO(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

}
