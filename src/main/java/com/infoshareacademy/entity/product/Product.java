package com.infoshareacademy.entity.product;

public abstract class Product {


    private String productName;

    private Double amount;

    public Product(String productName, Double amount) {
        this.productName = productName;
        this.amount = amount;
    }

    public Product() {

    }

    public String getProductName() {
        return productName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}