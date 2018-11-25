package com.example.manop.mashop.Function;

/**
 * Created by Manop on 11/24/2018.
 */

public class SaleHistory {
//    private final String Date;
//    private final String Buyer;
    private final String ProductName;
    private final int quantity;
    private final double totalprice;

    public SaleHistory(String ProductName, int quantity, double totalprice) {
//        this.Date = Date;
//        this.Buyer = Buyer;
        this.ProductName = ProductName;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

//    public String getDate() {
//        return Date;
//    }
//
//    public String getBuyer() {
//        return Buyer;
//    }

    public String getProductName() {
        return ProductName;
    }


    public int getQuantity() {
        return quantity;
    }

    public double getTotalprice() {
        return totalprice;
    }


}