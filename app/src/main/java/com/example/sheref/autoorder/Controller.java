package com.example.sheref.autoorder;

import android.content.Context;

import com.example.sheref.autoorder.Model.CategoryDataProvider;
import com.example.sheref.autoorder.Model.MenuDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.Model.RestaurantDataProvider;
import com.example.sheref.autoorder.Model.UserDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheref on 11/01/2018.
 */
public class Controller {

    private DBConnection connection;

    public UserDataProvider exeuteGetUserRecord(Context context) {
        connection = new DBConnection(context);
        UserDataProvider user = connection.getUserRecord();
        return user;

    }

    public void executeUpdateUserData(Context context, UserDataProvider user) {
        connection = new DBConnection(context);
        connection.updateUserData(user);
    }

    public void executeInsertIntoUser(Context context, UserDataProvider user) {
        connection = new DBConnection(context);
        connection.insertIntoUser(user);
    }

    public ArrayList<RestaurantDataProvider> exeuteGetRestRecords(Context context) {
        connection = new DBConnection(context);
        ArrayList<RestaurantDataProvider> restaurants = connection.getRestRecords();
        return restaurants;

    }

    public boolean executeInsertIntoRest(Context context) {
        connection = new DBConnection(context);
        return connection.insertIntoRestaurant();
    }

    public void executeInsertIntoCat(Context context) {
        connection = new DBConnection(context);
        connection.insertIntoCat();
    }

    public ArrayList<CategoryDataProvider> executeGetCatRecords(Context context, int restId) {
        connection = new DBConnection(context);
        ArrayList<CategoryDataProvider> categories = connection.getCatRecords(restId);

        return categories;
    }

    public void executeInsertIntoMenu(Context context) {
        connection = new DBConnection(context);
        connection.insertIntoMenu();
    }

    public ArrayList<MenuDataProvider> executeGetMenuRecords(Context context, int restId, int catId) {
        connection = new DBConnection(context);
        ArrayList<MenuDataProvider> menu = connection.getMenuRecords(restId, catId);

        return menu;
    }

    public void executeUpdateMealLikes(Context context, int itemId, int isLike) {
        connection = new DBConnection(context);
        connection.updateMealLikes(itemId, isLike);
    }

    public ArrayList<String> executeGetMealLikesRecords(Context context) {
        connection = new DBConnection(context);
        ArrayList<String> itemLikes = connection.getMealLikesRecords();

        return itemLikes;
    }

    public void executeUpdateLikesNum(Context context, int itemId, String likesNum) {
        connection = new DBConnection(context);
        connection.updateLikesNum(itemId, likesNum);
    }

    public void executeUpdateDislikesNum(Context context, int itemId, String dislikesNum) {
        connection = new DBConnection(context);
        connection.updateDisLikesNum(itemId, dislikesNum);
    }

    public void executeUpdateIsOrder(Context context, int restId) {
        connection = new DBConnection(context);
        connection.updateIsOrder(restId);
    }

    public void executeInsertIntoOrdersTable(Context context, int restId, List<Order> myOrder) {
        connection = new DBConnection(context);
        connection.insertIntoOrdersTable(restId, myOrder);
    }
}
