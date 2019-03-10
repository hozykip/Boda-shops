package com.example.android.bodashops.model;

public class OrderDetailsModel
{
    private String image, prodName, qty, price;

    public OrderDetailsModel(String image, String prodName, String qty, String price) {
        this.image = image;
        this.prodName = prodName;
        this.qty = qty;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
