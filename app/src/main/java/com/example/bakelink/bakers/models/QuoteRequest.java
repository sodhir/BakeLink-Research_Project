package com.example.bakelink.bakers.models;

public class QuoteRequest {
    private String customerName;
    private String cakeType;
    private String deliveryDate;
    private String location;
    private int imageResource;

    public QuoteRequest(String customerName, String cakeType, String deliveryDate, String location, int imageResource) {
        this.customerName = customerName;
        this.cakeType = cakeType;
        this.deliveryDate = deliveryDate;
        this.location = location;
        this.imageResource = imageResource;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getCakeType() { return cakeType; }
    public String getDeliveryDate() { return deliveryDate; }
    public String getLocation() { return location; }
    public int getImageResource() { return imageResource; }
}

