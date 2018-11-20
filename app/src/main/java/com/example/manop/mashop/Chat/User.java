package com.example.manop.mashop.Chat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author:
 * Created on: 9/1/2016 , 8:35 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class User {
    public String name;
    public String image;
    public String seller;
    public String uid;
    public String email;
    public String firebaseToken;

    public User() {
    }

    public User(String name, String image, String seller, String email, String uid, String firebaseToken) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.seller = seller;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }
}
