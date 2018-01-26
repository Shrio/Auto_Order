package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 19/01/2018.
 */
public class CartDataProvider {
    private String mealImg, mealName, mealPrice, mealQuantity, mealTotalPrice;
    private int plusImg, minusImg, deleteImg, itemId;

    public CartDataProvider(String mealImg, String mealName, String mealPrice, String mealQuantity, String mealTotalPrice,
                            int plusImg, int minusImg, int deleteImg, int itemId) {
        this.mealImg = mealImg;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.mealQuantity = mealQuantity;
        this.mealTotalPrice = mealTotalPrice;
        this.plusImg = plusImg;
        this.minusImg = minusImg;
        this.deleteImg = deleteImg;
        this.itemId = itemId;
    }

    public CartDataProvider() {
        this.mealImg = "";
        this.mealName = "";
        this.mealPrice = "";
        this.mealQuantity = "";
        this.mealTotalPrice = "";
        this.plusImg = 0;
        this.minusImg = 0;
        this.deleteImg = 0;
        this.itemId = 0;
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

    public String getMealTotalPrice() {
        return mealTotalPrice;
    }

    public void setMealTotalPrice(String mealTotalPrice) {
        this.mealTotalPrice = mealTotalPrice;
    }

    public int getPlusImg() {
        return plusImg;
    }

    public void setPlusImg(int plusImg) {
        this.plusImg = plusImg;
    }

    public int getMinusImg() {
        return minusImg;
    }

    public void setMinusImg(int minusImg) {
        this.minusImg = minusImg;
    }

    public int getDeleteImg() {
        return deleteImg;
    }

    public void setDeleteImg(int deleteImg) {
        this.deleteImg = deleteImg;
    }

    public int getItemId() { return itemId; }

    public void setItemId(int itemId) { this.itemId = itemId; }
}
