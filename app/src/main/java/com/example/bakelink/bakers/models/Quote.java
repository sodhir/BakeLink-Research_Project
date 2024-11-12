package com.example.bakelink.bakers.models;

public class Quote {
    private String customerName;
    private double amount;
    private String status;
    private int imageResource; // Assuming drawable resource ID for now

    // Constructor
    public Quote(String customerName, double amount, String status, int imageResource) {
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
        this.imageResource = imageResource;
    }

    // Getter and Setter for Customer Name
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter and Setter for Amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getter and Setter for Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
