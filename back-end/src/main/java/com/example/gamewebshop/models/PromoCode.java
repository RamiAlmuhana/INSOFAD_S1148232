package com.example.gamewebshop.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PromoCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private double discount;
    private LocalDateTime expiryDate;
    private int maxUsageCount;
    private PromoCodeType type;
    // Add other properties as needed


    public PromoCode(String code, double discount, LocalDateTime expiryDate, int maxUsageCount, PromoCodeType type) {
        this.code = code;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.maxUsageCount = maxUsageCount;
        this.type = type;
    }

    public PromoCode() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMaxUsageCount() {
        return maxUsageCount;
    }

    public void setMaxUsageCount(int maxUsageCount) {
        this.maxUsageCount = maxUsageCount;
    }

    public PromoCodeType getType() {
        return type;
    }

    public void setType(PromoCodeType type) {
        this.type = type;
    }

    public enum PromoCodeType {
        FIXED_AMOUNT,
        PERCENTAGE
    }

}
