package com.example.bakelink.customers.modal;

public class Deal {
    private String name;       // For the name of the deal
    private String dealCode;   // For the deal code
    private String validTill;   // For the validity date of the deal
    private String imageUrl;    // For the image URL

    // Constructor
    public Deal(String name, String dealCode, String validTill, String imageUrl) {
        this.name = name;
        this.dealCode = dealCode;
        this.validTill = validTill;
        this.imageUrl = imageUrl;
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
}
