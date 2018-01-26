package com.example.sheref.autoorder.Model;

import java.io.Serializable;

/**
 * Created by sheref on 07/01/2018.
 */
public class RestaurantDataProvider{
    private String restName;
    private int restImage;
    private float restRatingBar;
    private int restId;

    public RestaurantDataProvider(String restName, int restImage, float restRatingBar, int restId) {
        this.restName = restName;
        this.restImage = restImage;
        this.restRatingBar = restRatingBar;
        this.restId = restId;
    }

    public RestaurantDataProvider() {
        this.restName = "";
        this.restImage = 0;
        this.restRatingBar = 0.0f;
        this.restId = 0;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public int getRestImage() {
        return restImage;
    }

    public void setRestImage(int restImage) {
        this.restImage = restImage;
    }

    public float getRestRatingBar() {
        return restRatingBar;
    }

    public void setRestRatingBar(float restRatingBar) {
        this.restRatingBar = restRatingBar;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }
}
