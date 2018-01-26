package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 20/01/2018.
 */
public class CheckoutDataProvider {

    private String mealCheckImg, mealCheckName, mealCheckQuan, mealCheckSubTotal;

    public CheckoutDataProvider(String mealCheckImg, String mealCheckName, String mealCheckQuan, String mealCheckSubTotal) {
        this.mealCheckImg = mealCheckImg;
        this.mealCheckName = mealCheckName;
        this.mealCheckQuan = mealCheckQuan;
        this.mealCheckSubTotal = mealCheckSubTotal;
    }

    public String getMealCheckImg() {
        return mealCheckImg;
    }

    public void setMealCheckImg(String mealCheckImg) {
        this.mealCheckImg = mealCheckImg;
    }

    public String getMealCheckName() {
        return mealCheckName;
    }

    public void setMealCheckName(String mealCheckName) {
        this.mealCheckName = mealCheckName;
    }

    public String getMealCheckQuan() {
        return mealCheckQuan;
    }

    public void setMealCheckQuan(String mealCheckQuan) {
        this.mealCheckQuan = mealCheckQuan;
    }

    public String getMealCheckSubTotal() {
        return mealCheckSubTotal;
    }

    public void setMealCheckSubTotal(String mealCheckSubTotal) {
        this.mealCheckSubTotal = mealCheckSubTotal;
    }
}
