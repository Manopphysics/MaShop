package com.example.manop.mashop.Function;

import com.example.manop.mashop.Product.Product;

import java.util.ArrayList;

/**
 * Created by Manop on 11/25/2018.
 */

public interface MyCallback {
    void onCallback(String value);
    void onCallbackEmailName(String name, String email);
    void onCallbackProduct(ArrayList<Product> al);
    void onCallbackSaleHistory(String date, String description, String name
                            ,String price,String quantity, String buyerName,String buyerEmail);
}
