package com.example.bakelink.bakers.models;

import java.util.List;

public class RecommendationCake {
    private String id;
    private String imageUrl;
    private List<List<Integer>> rgbColors;

    private String customCakeRequestId;
    private String bakerTitle;

    private String cakeType;

    private Cake cake;

    public RecommendationCake() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<List<Integer>> getRgbColors() {
        return rgbColors;
    }

    public void setRgbColors(List<List<Integer>> rgbColors) {
        this.rgbColors = rgbColors;
    }

    public String getCustomCakeRequestId() {
        return customCakeRequestId;
    }

    public void setCustomCakeRequestId(String customCakeRequestId) {
        this.customCakeRequestId = customCakeRequestId;
    }

    public String getBakerTitle() {
        return bakerTitle;
    }

    public void setBakerTitle(String bakerTitle) {
        this.bakerTitle = bakerTitle;
    }

    public Cake getCake() {
        return cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }
}
