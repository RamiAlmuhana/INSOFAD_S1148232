package com.example.gamewebshop.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class OrderDTO{
    public String name;
    public String infix;
    public String last_name;
    public String zipcode;
    public int houseNumber;
    public String notes;

    @JsonAlias("user_id")
    public long userID;

    public OrderDTO(String name, String infix, String last_name, String zipcode, int houseNumber, String notes, long userID) {
        this.name = name;
        this.infix = infix;
        this.last_name = last_name;
        this.zipcode = zipcode;
        this.houseNumber = houseNumber;
        this.notes = notes;
        this.userID = userID;
    }
}