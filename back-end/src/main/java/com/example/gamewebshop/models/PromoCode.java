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
    private LocalDateTime startDate;
    private int maxUsageCount;
    private PromoCodeType type;
    private double minSpendAmount;
    private int usageCount; // New field to track usage count
    private double totalDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructors, getters, and setters

    public PromoCode(String code, double discount, LocalDateTime expiryDate, LocalDateTime startDate, int maxUsageCount, PromoCodeType type, Category category, double minSpendAmount) {
        this.code = code;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.startDate = startDate;
        this.maxUsageCount = maxUsageCount;
        this.type = type;
        this.category = category;
        this.minSpendAmount = minSpendAmount;
        this.usageCount = 0; // Initialize usage count
        this.totalDiscountAmount = 0;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
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

    public double getMinSpendAmount() {
        return minSpendAmount;
    }

    public void setMinSpendAmount(double minSpendAmount) {
        this.minSpendAmount = minSpendAmount;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public enum PromoCodeType {
        FIXED_AMOUNT,
        PERCENTAGE
    }
}
