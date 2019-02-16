package com.example.android.bodashops.model;

public class ProductAttributesModel {
    private String productId, attribute, value;

    public ProductAttributesModel(String productId, String attribute, String value) {
        this.productId = productId;
        this.attribute = attribute;
        this.value = value;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
