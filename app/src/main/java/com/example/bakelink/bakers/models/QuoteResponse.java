package com.example.bakelink.bakers.models;

public class QuoteResponse {
    private String quoteResponseId;
    private String bakerId;
    private String cakeType;
    private String customCakeRequestId;
    private Double quotedPrice;
    private String responseMessage;
    private String status;
    private String customerId;
    private String imageUrl;



    // Constructor for Firebase
    public QuoteResponse() {
        // Firebase requires an empty constructor
    }

    // Constructor with data
    public QuoteResponse(String quoteResponseId, String bakerId, String customCakeRequestId, Double quotedPrice, String responseMessage, String status, String customerId, String imageUrl,String cakeType) {
        this.quoteResponseId = quoteResponseId;
        this.bakerId = bakerId;
        this.customCakeRequestId = customCakeRequestId;
        this.quotedPrice = quotedPrice;
        this.responseMessage = responseMessage;
        this.status = status;
        this.customerId = customerId;
        this.imageUrl = imageUrl;
        this.cakeType = cakeType;
    }

    //getter and setter

    public String getQuoteResponseId() {
        return quoteResponseId;
    }

    public void setQuoteResponseId(String quoteResponseId) {
        this.quoteResponseId = quoteResponseId;
    }

    public String getBakerId() {
        return bakerId;
    }

    public void setBakerId(String bakerId) {
        this.bakerId = bakerId;
    }

    public String getCustomCakeRequestId() {
        return customCakeRequestId;
    }

    public void setCustomCakeRequestId(String customCakeRequestId) {
        this.customCakeRequestId = customCakeRequestId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }
}
