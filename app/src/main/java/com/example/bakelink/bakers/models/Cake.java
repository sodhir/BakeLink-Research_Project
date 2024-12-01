package com.example.bakelink.bakers.models;

import java.util.List;

public class Cake {

    String cakeId;

    String cakeName;
    Double price;

    String description;
    private List<String> weights;

    String cakeImgUrl;

    public Cake(String cakeImgUrl, Double price, String description, String cakeName) {
        this.cakeImgUrl = cakeImgUrl;
        this.price = price;
        this.description = description;
        this.cakeName = cakeName;
    }

    public Cake(Double price, String cakeName, String cakeImgUrl) {
        this.price = price;
        this.cakeName = cakeName;
        this.cakeImgUrl = cakeImgUrl;
    }

    public Cake() {
    }

    public List<String> getWeights() {
        return weights;
    }

    public void setWeights(List<String> weights) {
        this.weights = weights;
    }

    public String getCakeId() {
        return cakeId;
    }

    public void setCakeId(String cakeId) {
        this.cakeId = cakeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
