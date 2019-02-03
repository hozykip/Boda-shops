package com.example.android.bodashops.model;

public class Product {
    private String productId, ProductName, price, quantity, image;

    public Product(String prodId, String productName, String price, String quantity, String image) {
        this.productId = prodId;
        this.ProductName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
