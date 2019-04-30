package com.example.android.bodashops.model;

public class AccountModel {
    private String orderId;
    private String ownerId;
    private String amount;
    private String date;
    private String accId;
    private String shopId;

    public AccountModel(String orderId, String ownerId, String amount, String date, String accId, String shopId) {
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.amount = amount;
        this.date = date;
        this.accId = accId;
        this.shopId = shopId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
