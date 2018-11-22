package com.example.manop.mashop.Shop;

/**
 * Created by Manop on 11/22/2018.
 */

public class ShopModel {
    private String description;
    private String image;
    private String name;
    private String phone;

    public ShopModel() {
    }

    public ShopModel(String description, String image, String name, String phone) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


