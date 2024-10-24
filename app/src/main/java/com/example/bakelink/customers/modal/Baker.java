package com.example.bakelink.customers.modal;

public class Baker {

    private String name;
    private String imageUrl; // URL for the bakery image
    private float rating; // Bakery rating

    // Constructor
    public Baker(String name, String imageUrl, float rating) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getRating() {
        return rating;
    }
}
