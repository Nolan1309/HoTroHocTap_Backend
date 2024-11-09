package com.example.hotrohoctapbackend.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class AdminPaymentDTO {
    private int id;
    private String buyerName;
    private BigDecimal totalPayment;
    private Date paymentDate;
    private String paymentMethod;

    // Constructors, Getters, and Setters
    public AdminPaymentDTO(Integer id, String buyerName, BigDecimal totalPayment, Date paymentDate, String paymentMethod) {
        this.id = id;
        this.buyerName = buyerName;
        this.totalPayment = totalPayment;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
