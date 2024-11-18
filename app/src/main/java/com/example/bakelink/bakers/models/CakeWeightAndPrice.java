package com.example.bakelink.bakers.models;

public class CakeWeightAndPrice {
    private String weight;
    private int price;

    public CakeWeightAndPrice() {
        // Required for Firebase
    }
    // Constructor, getters, and setters
    public CakeWeightAndPrice(String weight, int price) {
        this.weight = weight;
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
