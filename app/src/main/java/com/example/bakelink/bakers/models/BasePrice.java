package com.example.bakelink.bakers.models;

public class BasePrice {
    private String cakeType;
    private int price;

    // Constructor, getters, and setters
    public BasePrice(String cakeType, int price) {
        this.cakeType = cakeType;
        this.price = price;
    }

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

