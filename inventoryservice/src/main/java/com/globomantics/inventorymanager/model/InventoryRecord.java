package com.globomantics.inventorymanager.model;

import java.io.Serializable;

public class InventoryRecord implements Serializable {
    private Integer productId;
    private Integer quantity;
    private String productName;
    private String productCategory;

    public InventoryRecord() {
    }

    public InventoryRecord(Integer productId, Integer quantity, String productName, String productCategory) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productCategory = productCategory;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "InventoryRecord{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }
}
