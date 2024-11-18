package com.example.bakelink.bakers.models;

import java.util.List;

public class Order {
    private String orderId;
    private String orderType;  // Regular or Custom
    private String orderDate;  // The date of the order
    private String orderDetails; // Details of the order
    private String customerName;  // Name of the customer
    private String cakeType;  // Type of the cake
    private String deliveryAddress;  // Delivery address for the order
    private String imageResource; // Assuming drawable resource ID for now

    private Double orderTotal;

    private List<OrderItem> orderItems;

    public Order() {

    }

    // Constructor with all fields


    public Order(String orderId, String orderType, String orderDate, String orderDetails, String customerName, String cakeType, String deliveryAddress, String imageResource, Double orderTotal, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderDate = orderDate;
        this.orderDetails = orderDetails;
        this.customerName = customerName;
        this.cakeType = cakeType;
        this.deliveryAddress = deliveryAddress;
        this.imageResource = imageResource;
        this.orderTotal = orderTotal;
        this.orderItems = orderItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
