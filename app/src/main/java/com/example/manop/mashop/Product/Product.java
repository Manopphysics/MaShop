package com.example.manop.mashop.Product;

/**
 * Created by Manop on 11/4/2018.
 */

public class Product {
    private String name;
    private String description;
    private String IMAGE;
    private String uid;
    private String price;
//    private String quantity;

    public Product(String name, String description, String IMAGE, String uid, String price, String quantity) {
        this.name = name;
        this.description = description;
        this.IMAGE = IMAGE;
        this.uid = uid;
        this.price = price;
//        this.quantity = quantity;
    }

    public Product(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
}
