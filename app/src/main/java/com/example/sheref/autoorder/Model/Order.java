package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 17/01/2018.
 */
public class Order {

    private String mealImg, mealName, mealPrice, mealQuantity;
    private int mealId ;

    public Order(String mealImg, String mealName, String mealPrice, int mealId, String mealQuantity) {
        this.mealImg = mealImg;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.mealId = mealId;
        this.mealQuantity = mealQuantity;
    }

    public Order() {
        this.mealImg = "";
        this.mealName = "";
        this.mealPrice = "";
        this.mealQuantity = "";
        this.mealId = 0;
    }

    public String getMealImg() {
        return mealImg;
    }

    public void setMealImg(String mealImg) {
        this.mealImg = mealImg;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(String mealPrice) {
        this.mealPrice = mealPrice;
    }

    public String getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(String mealQuantity) {
        this.mealQuantity = mealQuantity;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
