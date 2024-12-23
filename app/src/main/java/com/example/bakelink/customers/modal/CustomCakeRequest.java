package com.example.bakelink.customers.modal;

import java.util.ArrayList;
import java.util.List;

public class CustomCakeRequest {

    private String userId;
    private String customCakeRequestId;
    private String cakeType;
    private String cakeSize;
    private String flavor;
    private String filling;
    private String deliveryDate;
    private String deliveryTime;

    private String noOfLayers;
    private String cakeWeight;

    private String deliveryAddress;

    private String userEmail;

    private String cakeRequestStatus;

    private List<List<Integer>> rgbColors;


    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCustomCakeRequestId() {
        return customCakeRequestId;
    }

    public void setCustomCakeRequestId(String customCakeRequestId) {
        this.customCakeRequestId = customCakeRequestId;
    }

    private String notes;
    private String imageUrl;

    public CustomCakeRequest() {
    }


    public CustomCakeRequest(String cakeSize, String cakeType, String deliveryDate, String deliveryTime, String filling, String flavor, String imageUrl, String notes, String userId) {
        this.cakeSize = cakeSize;
        this.cakeType = cakeType;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.filling = filling;
        this.flavor = flavor;
        this.imageUrl = imageUrl;
        this.notes = notes;
        this.userId = userId;
    }

    // Getter and setter methods for the fields



    public String getCakeSize() {
        return cakeSize;
    }

    public void setCakeSize(String cakeSize) {
        this.cakeSize = cakeSize;
    }

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getFilling() {
        return filling;
    }

    public void setFilling(String filling) {
        this.filling = filling;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCakeWeight() {
        return cakeWeight;
    }

    public void setCakeWeight(String cakeWeight) {
        this.cakeWeight = cakeWeight;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNoOfLayers() {
        return noOfLayers;
    }

    public void setNoOfLayers(String noOfLayers) {
        this.noOfLayers = noOfLayers;
    }

    public String getCakeRequestStatus() {
        return cakeRequestStatus;
    }

    public void setCakeRequestStatus(String cakeRequestStatus) {
        this.cakeRequestStatus = cakeRequestStatus;
    }

    public List<List<Integer>> getRgbColors() {
        return rgbColors;
    }

    public void setRgbColors(List<List<Integer>> rgbColors) {
        this.rgbColors = rgbColors;
    }
}
