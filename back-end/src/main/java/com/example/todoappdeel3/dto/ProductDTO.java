package com.example.todoappdeel3.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProductDTO {
    public String name;
    public String description;
    public Number price;
    public String imgURL;

    @JsonAlias("category_id")
    public long categoryId;

    public ProductDTO(String name, String description, Number price, long categoryId, String imgURL) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.imgURL = imgURL;
    }
}
