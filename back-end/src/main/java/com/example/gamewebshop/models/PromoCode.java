package com.example.gamewebshop.models;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructors, getters, and setters

    public PromoCode(String code, double discount, LocalDateTime expiryDate, int maxUsageCount, PromoCodeType type, Category category) {
        this.code = code;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.maxUsageCount = maxUsageCount;
        this.type = type;
        this.category = category;
    }

    public PromoCode() {}

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public enum PromoCodeType {
        FIXED_AMOUNT,
        PERCENTAGE
    }
}
