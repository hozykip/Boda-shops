package com.example.android.bodashops;

public class Product {
    private String productId, ProductName;

    public Product(String prodId, String productName) {
        this.productId = prodId;
        this.ProductName = productName;
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
}
