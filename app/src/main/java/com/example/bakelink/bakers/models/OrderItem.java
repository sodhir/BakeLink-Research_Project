package com.example.bakelink.bakers.models;

public class OrderItem {

    String orderItemId;
    String cakeId;
    String orderType;

    Integer quantity;

    public OrderItem() {
    }

    public OrderItem(String cakeId, String orderItemId, Integer quantity, String orderType) {
        cakeId = cakeId;
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.orderType = orderType;
    }

    public String getCakeId() {
        return cakeId;
    }

    public void setCakeId(String cakeId) {
        cakeId = cakeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }
}
