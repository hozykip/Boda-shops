package com.example.android.bodashops.model;

public class OrdersModel {
    private String orderId;
    private String buyerId;
    private String orderCompletion;
    private String orderTime;
    private String totalOrderPrice;
    private String buyerName;
    private String buyerPhone;

    public OrdersModel(String orderId, String buyerId, String orderCompletion, String orderTime, String totalOrderPrice,
                       String buyerName,String buyerPhone, String ordersCount, String orderLocation)
    {
        this.orderId = orderId;
        this.buyerId = buyerId;

        this.orderTime = orderTime;
        this.totalOrderPrice = totalOrderPrice;
        this.buyerName = buyerName;
        this.buyerPhone = buyerPhone;
        this.ordersCount = ordersCount;
        this.orderLocation = orderLocation;

        switch (orderCompletion)
        {
            case "2":
                this.orderCompletion = "Cancelled";
                break;
            case "1":
                this.orderCompletion = "Completed";
                break;
            default:
                this.orderCompletion = "Pending";
        }
    }

    public String getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(String ordersCount) {
        this.ordersCount = ordersCount;
    }

    private String ordersCount;

    public String getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
    }

    private String orderLocation;

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getOrderCompletion() {
        return orderCompletion;
    }

    public void setOrderCompletion(String orderCompletion) {
        this.orderCompletion = orderCompletion;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
