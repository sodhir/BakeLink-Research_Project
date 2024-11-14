package com.example.bakelink.customers.modal;

public class Baker {

    String id;
    private String name;
    private String imageUrl;
    private float rating;

    public Baker() {
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
