package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 07/01/2018.
 */
public class MenuDataProvider {
    private String mealName, mealImage;
    private String mealPrice;
    private String likesNum, dislikesNum;
    private int likeImage, dislikeImage, cartImage;
    private int itemId, restId, catId;

    private String likeTag, dislikeTag = "0";

    public MenuDataProvider(String mealName, String mealPrice, String likesNum, String dislikesNum, String mealImage,
                            int likeImage, int dislikeImage, int cartImage, int itemId) {
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.likesNum = likesNum;
        this.dislikesNum = dislikesNum;
        this.mealImage = mealImage;
        this.likeImage = likeImage;
        this.dislikeImage = dislikeImage;
        this.cartImage = cartImage;
        this.itemId = itemId;
    }


    public MenuDataProvider(int itemId, int restId, int catId, String mealName, String mealPrice, String mealImage,
                            String likesNum, String dislikesNum) {
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.likesNum = likesNum;
        this.dislikesNum = dislikesNum;
        this.mealImage = mealImage;
        this.itemId = itemId;
        this.restId = restId;
        this.catId = catId;
    }

    public MenuDataProvider() {
        this.mealName = "";
        this.mealPrice = "";
        this.likesNum = "";
        this.dislikesNum = "";
        this.mealImage = "";
        this.itemId = 0;
        this.restId = 0;
        this.catId = 0;
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

    public String getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(String likesNum) {
        this.likesNum = likesNum;
    }

    public String getDislikesNum() {
        return dislikesNum;
    }

    public void setDislikesNum(String dislikesNum) {
        this.dislikesNum = dislikesNum;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public int getLikeImage() {
        return likeImage;
    }

    public void setLikeImage(int likeImage) {
        this.likeImage = likeImage;
    }

    public int getDislikeImage() {
        return dislikeImage;
    }

    public void setDislikeImage(int dislikeImage) {
        this.dislikeImage = dislikeImage;
    }

    public int getCartImage() {
        return cartImage;
    }

    public void setCartImage(int cartImage) {
        this.cartImage = cartImage;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getLikeTag() {
        return likeTag;
    }

    public void setLikeTag(String likeTag) {
        this.likeTag = likeTag;
    }

    public String getDislikeTag() {
        return dislikeTag;
    }

    public void setDislikeTag(String dislikeTag) {
        this.dislikeTag = dislikeTag;
    }
}
