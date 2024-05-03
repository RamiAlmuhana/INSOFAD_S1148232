package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @Column(length = 500)
    private String description;
    private Double price;
    private String imgURL;
    @Column(length = 500)
    private String specifications;
    private String releaseDate;
    private String publisher;

     /*
    Maps the many-to-one relationship between product and category, jsonbackreference so that we do not get an
    infinite dependency loop in the request. Cascasdetype merge so the product is able to create a category if we
    seed the data to the database. Without the merge you get a persistence race condition.
    */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Category category;





    //needed by JPA to create the entity must be present no arg constructor
    public Product() {
    }

    public Product(String name, String description, Double price, String imgURL, Category category, String specifications, String releaseDate, String publisher) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
        this.specifications = specifications;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
        this.category = category;
    }

    //getters and setters are needed to map all the properties to the database by JPA, could
    //also be solved by making the properties public but gives less control over the properties.


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
