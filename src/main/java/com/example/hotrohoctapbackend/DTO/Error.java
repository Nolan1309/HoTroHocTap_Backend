package com.example.hotrohoctapbackend.DTO;

public class Error {
    private String noidung;

    public Error(String noidung) {
        this.noidung = noidung;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    @Override
    public String toString() {
        return "Error{" +
                "noidung='" + noidung + '\'' +
                '}';
    }
}
