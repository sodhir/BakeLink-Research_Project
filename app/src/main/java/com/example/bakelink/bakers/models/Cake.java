package com.example.bakelink.bakers.models;

public class Cake {
    String cakeName;
    Double price;

    String cakeImgUrl;

    public Cake(Double price, String cakeName, String cakeImgUrl) {
        this.price = price;
        this.cakeName = cakeName;
        this.cakeImgUrl = cakeImgUrl;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCakeImgUrl() {
        return cakeImgUrl;
    }

    public void setCakeImgUrl(String cakeImgUrl) {
        this.cakeImgUrl = cakeImgUrl;
    }
}
