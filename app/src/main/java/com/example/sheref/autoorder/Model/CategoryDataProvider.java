package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 07/01/2018.
 */
public class CategoryDataProvider {

    private String catName;
    private int catImage, restId, catId;

    public CategoryDataProvider(String catName, int catImage, int restId, int catId) {
        this.catName = catName;
        this.catImage = catImage;
        this.restId = restId;
        this.catId = catId;
    }

    public CategoryDataProvider() {
        this.catName = "";
        this.catImage = 0;
        this.restId = 0;
        this.catId = 0;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatImage() {
        return catImage;
    }

    public void setCatImage(int catImage) {
        this.catImage = catImage;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }

    public int getCatId() { return catId; }

    public void setCatId(int catId) { this.catId = catId; }

}
