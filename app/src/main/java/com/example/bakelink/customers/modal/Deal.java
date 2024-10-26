package com.example.bakelink.customers.modal;

public class Deal {
    private String name;       // For the name of the deal
    private String dealCode;   // For the deal code
    private String validTill;   // For the validity date of the deal
    private String imageUrl;
    private String dealTag;    // For the image URL

    // Constructor
    public Deal(String name, String dealCode, String validTill, String imageUrl, String dealTag) {
        this.name = name;
        this.dealCode = dealCode;
        this.validTill = validTill;
        this.imageUrl = imageUrl;
        this.dealTag = dealTag;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDealCode() {
        return dealCode;
    }

    public String getValidTill() {
        return validTill;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDealTag() {
        return dealTag;
    }
}
