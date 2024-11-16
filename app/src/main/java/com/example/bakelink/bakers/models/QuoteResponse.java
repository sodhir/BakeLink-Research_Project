package com.example.bakelink.bakers.models;

public class QuoteResponse {
    private String quoteResponseId;
    private String userID;
    String CustomCakeRequestId;
    private Double quotedPrice;
    private String responseMessage;

    private String status;

    private String customerId;
    private String imageUrl;

    public QuoteResponse(String userID, Double quotedPrice, String responseMessage) {
        this.userID = userID;
        this.quotedPrice = quotedPrice;
        this.responseMessage = responseMessage;
    }

    public QuoteResponse() {
    }

    public QuoteResponse(String userID, Double quotedPrice, String responseMessage, String customCakeRequestId) {
        this.userID = userID;
        this.quotedPrice = quotedPrice;
        this.responseMessage = responseMessage;
        this.CustomCakeRequestId = customCakeRequestId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomCakeRequestId() {
        return CustomCakeRequestId;
    }

    public void setCustomCakeRequestId(String customCakeRequestId) {
        CustomCakeRequestId = customCakeRequestId;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Double getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(Double quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getQuoteResponseId() {
        return quoteResponseId;
    }

    public void setQuoteResponseId(String quoteResponseId) {
        this.quoteResponseId = quoteResponseId;
    }
}
