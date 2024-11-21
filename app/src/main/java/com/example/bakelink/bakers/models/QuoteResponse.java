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
    private Double cakeTypePrice;
    private Double cakeSizePrice;
    private Double cakeLayersPrice;
    private Double cakeWeightPrice;
    private Double cakeFlavorPrice;
    private Double cakeFillingPrice;
    private Double additionalNotesPrice;
    private Double deliveryChargesPrice;
    private Double discountsPrice;



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

    public QuoteResponse(Double discountsPrice, Double deliveryChargesPrice, Double additionalNotesPrice, Double cakeFillingPrice, Double cakeFlavorPrice, Double cakeWeightPrice, Double cakeLayersPrice, Double cakeSizePrice, Double cakeTypePrice, String imageUrl, String customerId, String status, String responseMessage, Double quotedPrice, String customCakeRequestId, String cakeType, String bakerId, String quoteResponseId) {
        this.discountsPrice = discountsPrice;
        this.deliveryChargesPrice = deliveryChargesPrice;
        this.additionalNotesPrice = additionalNotesPrice;
        this.cakeFillingPrice = cakeFillingPrice;
        this.cakeFlavorPrice = cakeFlavorPrice;
        this.cakeWeightPrice = cakeWeightPrice;
        this.cakeLayersPrice = cakeLayersPrice;
        this.cakeSizePrice = cakeSizePrice;
        this.cakeTypePrice = cakeTypePrice;
        this.imageUrl = imageUrl;
        this.customerId = customerId;
        this.status = status;
        this.responseMessage = responseMessage;
        this.quotedPrice = quotedPrice;
        this.customCakeRequestId = customCakeRequestId;
        this.cakeType = cakeType;
        this.bakerId = bakerId;
        this.quoteResponseId = quoteResponseId;
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

    public Double getCakeTypePrice() {
        return cakeTypePrice;
    }

    public void setCakeTypePrice(Double cakeTypePrice) {
        this.cakeTypePrice = cakeTypePrice;
    }

    public Double getCakeSizePrice() {
        return cakeSizePrice;
    }

    public void setCakeSizePrice(Double cakeSizePrice) {
        this.cakeSizePrice = cakeSizePrice;
    }

    public Double getCakeLayersPrice() {
        return cakeLayersPrice;
    }

    public void setCakeLayersPrice(Double cakeLayersPrice) {
        this.cakeLayersPrice = cakeLayersPrice;
    }

    public Double getCakeWeightPrice() {
        return cakeWeightPrice;
    }

    public void setCakeWeightPrice(Double cakeWeightPrice) {
        this.cakeWeightPrice = cakeWeightPrice;
    }

    public Double getCakeFlavorPrice() {
        return cakeFlavorPrice;
    }

    public void setCakeFlavorPrice(Double cakeFlavorPrice) {
        this.cakeFlavorPrice = cakeFlavorPrice;
    }

    public Double getCakeFillingPrice() {
        return cakeFillingPrice;
    }

    public void setCakeFillingPrice(Double cakeFillingPrice) {
        this.cakeFillingPrice = cakeFillingPrice;
    }

    public Double getAdditionalNotesPrice() {
        return additionalNotesPrice;
    }

    public void setAdditionalNotesPrice(Double additionalNotesPrice) {
        this.additionalNotesPrice = additionalNotesPrice;
    }

    public Double getDeliveryChargesPrice() {
        return deliveryChargesPrice;
    }

    public void setDeliveryChargesPrice(Double deliveryChargesPrice) {
        this.deliveryChargesPrice = deliveryChargesPrice;
    }

    public Double getDiscountsPrice() {
        return discountsPrice;
    }

    public void setDiscountsPrice(Double discountsPrice) {
        this.discountsPrice = discountsPrice;
    }
}
