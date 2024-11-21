package com.example.bakelink.bakers.models;

public class OrderItem {

    String orderItemId;
    String cakeId;
    String orderType;
    private String itemTitle;
    private String flavor;
    private String weight;
    Integer quantity;
    private Double price;
    private String imageUrl;

    private String status;

    private String bakerId;

    public OrderItem() {
    }

    public OrderItem(String orderItemId, String cakeId, String orderType, String itemTitle, String flavor, String weight, Integer quantity, Double price, String imageUrl) {
        this.orderItemId = orderItemId;
        this.cakeId = cakeId;
        this.orderType = orderType;
        this.itemTitle = itemTitle;
        this.flavor = flavor;
        this.weight = weight;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getCakeId() {
        return cakeId;
    }

    public void setCakeId(String cakeId) {
        this.cakeId = cakeId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBakerId() {
        return bakerId;
    }

    public void setBakerId(String bakerId) {
        this.bakerId = bakerId;
    }
}
