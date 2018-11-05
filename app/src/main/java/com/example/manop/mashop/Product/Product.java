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

    public Product(String name, String description, String IMAGE, String uid, String price) {
        this.name = name;
        this.description = description;
        this.IMAGE = IMAGE;
        this.uid = uid;
        this.price = price;
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
}
