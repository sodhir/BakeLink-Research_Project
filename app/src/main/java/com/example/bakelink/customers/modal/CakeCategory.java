package com.example.bakelink.customers.modal;

public class CakeCategory {
    private String name;
    private String imageUrl;

    public CakeCategory(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
