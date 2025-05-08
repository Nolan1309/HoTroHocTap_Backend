package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminQuestionGetDTO {
    private int id;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String result;
    private String instruction;
    private String resultCheck;


    // Constructor
    public AdminQuestionGetDTO(int id, String content, String optionA, String optionB, String optionC, String optionD, String result, String instruction, String resultCheck) {
        this.id = id;
        this.content = content;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.result = result;
        this.instruction = instruction;
        this.resultCheck = resultCheck;
    }



    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getResultCheck() {
        return resultCheck;
    }

    public void setResultCheck(String resultCheck) {
        this.resultCheck = resultCheck;
    }
}
