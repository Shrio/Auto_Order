package com.example.sheref.autoorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.sheref.autoorder.Model.CategoryDataProvider;
import com.example.sheref.autoorder.Model.MenuDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.Model.RestaurantDataProvider;
import com.example.sheref.autoorder.Model.UserDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheref on 13/01/2018.
 */
public class DBConnection extends SQLiteOpenHelper {

    public final static String DBName = "MyDB.db";
    final static int version = 1;

    private ArrayList<RestaurantDataProvider> restList = new ArrayList<RestaurantDataProvider>();
    private ArrayList<CategoryDataProvider> catList = new ArrayList<CategoryDataProvider>();
    private ArrayList<MenuDataProvider> menuItems = new ArrayList<MenuDataProvider>();
    private Context context;

    public DBConnection(Context context) {
        super(context, DBName, null, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS User (userId INT primary key, userName TEXT NOT NULL" +
                " , userAddr TEXT NOT NULL, userPhone TEXT NOT NULL, userPassword TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Restaurant (restId INT primary key, restName TEXT NOT NULL, " +
                "restImg INT NOT NULL, restRate FLOAT NOT NULL);");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Categories (catId INT primary key, catName TEXT NOT NULL, " +
                "catImg INT NOT NULL ,rest_id INT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Menu (itemId INT Primary key, restId INT NOT NULL, catId INT NOT NULL," +
                "mealName TEXT NOT NULL, mealPrice TEXT NOT NULL, mealImg TEXT NOT NULL ,likesNum TEXT NOT NULL," +
                " dislikesNum TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS MealLikes (item_id INT primary key,isLike INT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS RestOrders (rest_id INT primary key, isOrder INT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Orders (rest_id INT, meal_id INT, mealImg TEXT NOT NULL, " +
                "mealName TEXT NOT NULL, mealPrice TEXT NOT NULL, mealQuantity TEXT NOT NULL," +
                "PRIMARY KEY (rest_id,meal_id ) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop IF EXISTS table User");
        sqLiteDatabase.execSQL("drop IF EXISTS table Restaurant");
        sqLiteDatabase.execSQL("drop IF EXISTS table Categories");
        sqLiteDatabase.execSQL("drop IF EXISTS table Menu");
        sqLiteDatabase.execSQL("drop IF EXISTS table MealLikes");
        sqLiteDatabase.execSQL("drop IF EXISTS table RestOrders");
        sqLiteDatabase.execSQL("drop IF EXISTS table Orders");
        onCreate(sqLiteDatabase);
    }

    /* User Table */
    public void insertIntoUser(UserDataProvider user) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId", user.getUserId());
        values.put("userName", user.getUserName());
        values.put("userAddr", user.getUserAddress());
        values.put("userPhone", user.getUserPhone());
        values.put("userPassword", user.getUserPassword());

        //  sqLiteDatabase.insert("User", null, values);
        sql.insertWithOnConflict("User", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public UserDataProvider getUserRecord() {

        UserDataProvider user = new UserDataProvider();
        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select userId ,userName, userPassword, userAddr, userPhone  from User", null);

        if (cursor != null && cursor.moveToFirst()) {


            user.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            user.setUserPassword(cursor.getString(cursor.getColumnIndex("userPassword")));
            user.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
            user.setUserAddress(cursor.getString(cursor.getColumnIndex("userAddr")));
            user.setUserPhone(cursor.getString(cursor.getColumnIndex("userPhone")));


        }
        return user;
    }

    public void updateUserData(UserDataProvider user) {
        SQLiteDatabase sql = this.getWritableDatabase();

        sql.execSQL("UPDATE User set userName = '" + user.getUserName() + "', userPhone = '" + user.getUserPhone() +
                "', userAddr = '" + user.getUserAddress() + "'");
    }

    /* Restaurant Table */
    public void addToRestList() {

        restList.add(new RestaurantDataProvider("Macdonals", R.drawable.mac_logo, 3.5f, 1));
        restList.add(new RestaurantDataProvider("Papa John's", R.drawable.papa_logo, 2.5f, 2));
    }

    public boolean insertIntoRestaurant() {

        this.addToRestList();
        SQLiteDatabase sql = this.getWritableDatabase();

        for (int i = 0; i < this.restList.size(); i++) {
            ContentValues values = new ContentValues();

            values.put("restId", restList.get(i).getRestId());
            values.put("restName", restList.get(i).getRestName());
            values.put("restImg", restList.get(i).getRestImage());
            values.put("restRate", restList.get(i).getRestRatingBar());

            // sql.insert("Restaurant", null, values);
            sql.insertWithOnConflict("Restaurant", null, values, SQLiteDatabase.CONFLICT_REPLACE);

            this.fillRestOrdersTable();
        }

        return true;
    }


    public ArrayList<RestaurantDataProvider> getRestRecords() {

        ArrayList<RestaurantDataProvider> restaurants = new ArrayList<RestaurantDataProvider>();

        SQLiteDatabase sql = this.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from Restaurant", null);

        while (cursor != null && cursor.moveToNext()) {

            RestaurantDataProvider restaurant = new RestaurantDataProvider();

            restaurant.setRestName(cursor.getString(cursor.getColumnIndex("restName")));
            restaurant.setRestImage(cursor.getInt(cursor.getColumnIndex("restImg")));
            restaurant.setRestRatingBar(cursor.getFloat(cursor.getColumnIndex("restRate")));
            restaurant.setRestId(cursor.getInt(cursor.getColumnIndex("restId")));

            restaurants.add(restaurant);
        }
        return restaurants;
    }

    public void updateLikesNum(int itemId, String likesNum) {

        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("UPDATE Menu set likesNum = " + likesNum + " where itemId = " + itemId);
    }


    public void updateDisLikesNum(int itemId, String disLikesNum) {

        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("UPDATE Menu set dislikesNum = " + disLikesNum + " where itemId = " + itemId);
    }

    public void insertIntoMealLikes(int itemId, int isLike) {

        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("item_id", itemId);
        values.put("isLike", isLike);
        sql.insertWithOnConflict("MealLikes", null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public boolean checkRow(int itemId) {
        SQLiteDatabase sql = this.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from MealLikes where item_id = " + itemId, null);

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateMealLikes(int itemId, int isLike) {
        SQLiteDatabase sql = this.getWritableDatabase();

        if (this.checkRow(itemId)) {
            sql.execSQL("UPDATE MealLikes set isLike = " + isLike + " where item_id =  " + itemId);
        } else {
            this.insertIntoMealLikes(itemId, isLike);
        }
    }

    public ArrayList<String> getMealLikesRecords() {
        ArrayList<String> itemLikes = new ArrayList<String>();
        SQLiteDatabase sql = this.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select item_id, isLike from MealLikes", null);
        while (cursor != null && cursor.moveToNext()) {
            String strTmp = "true";
            strTmp = String.valueOf(cursor.getInt(cursor.getColumnIndex("item_id")));
            strTmp += ",";
            strTmp += String.valueOf(cursor.getInt(cursor.getColumnIndex("isLike")));
            itemLikes.add(strTmp);
        }
        return itemLikes;
    }

    /* Category Table */
    public void addToCatList() {
        //Mac
        catList.add(new CategoryDataProvider("Beef", R.drawable.meat_icon, 1, 1));
        catList.add(new CategoryDataProvider("Chicken", R.drawable.chicken_icon, 1, 2));
        catList.add(new CategoryDataProvider("Fish", R.drawable.fish_icon, 1, 3));
        catList.add(new CategoryDataProvider("Salads", R.drawable.salad_icon, 1, 4));
        catList.add(new CategoryDataProvider("Desert", R.drawable.desert_icon, 1, 5));

        catList.add(new CategoryDataProvider("Soft drinks", R.drawable.soft_drinks_icon, 1, 6));
        catList.add(new CategoryDataProvider("Hot drinks", R.drawable.hot_drinks_icon, 1, 7));
        catList.add(new CategoryDataProvider("Milk shakes", R.drawable.milkshake_icon, 1, 8));

        //Papa
        catList.add(new CategoryDataProvider("Beef", R.drawable.meat_icon, 2, 9));
        catList.add(new CategoryDataProvider("Chicken", R.drawable.chicken_icon, 2, 10));
        catList.add(new CategoryDataProvider("Fish", R.drawable.fish_icon, 2, 11));
        catList.add(new CategoryDataProvider("Veggie", R.drawable.veggie_icon, 2, 12));
        catList.add(new CategoryDataProvider("Cheese", R.drawable.cheese_icon, 2, 13));
        catList.add(new CategoryDataProvider("Desert", R.drawable.desert_icon, 2, 14));

        catList.add(new CategoryDataProvider("Soft drinks", R.drawable.soft_drinks_icon, 2, 15));
    }

    public void insertIntoCat() {
        this.addToCatList();

        SQLiteDatabase sql = this.getWritableDatabase();

        for (int i = 0; i < this.catList.size(); i++) {
            ContentValues values = new ContentValues();

            values.put("catName", catList.get(i).getCatName());
            values.put("catImg", catList.get(i).getCatImage());
            values.put("rest_id", catList.get(i).getRestId());
            values.put("catId", catList.get(i).getCatId());

            // sql.insert("Categories", null, values);
            sql.insertWithOnConflict("Categories", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public ArrayList<CategoryDataProvider> getCatRecords(int restId) {

        ArrayList<CategoryDataProvider> categories = new ArrayList<CategoryDataProvider>();

        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select catName ,rest_id, catImg, catId from Categories where rest_id = " + restId, null);

        while (cursor != null && cursor.moveToNext()) {

            CategoryDataProvider categoryDataProvider = new CategoryDataProvider();

            categoryDataProvider.setCatId(cursor.getInt(cursor.getColumnIndex("catId")));
            categoryDataProvider.setCatName(cursor.getString(cursor.getColumnIndex("catName")));
            categoryDataProvider.setCatImage(cursor.getInt(cursor.getColumnIndex("catImg")));
            categoryDataProvider.setRestId(cursor.getInt(cursor.getColumnIndex("rest_id")));

            categories.add(categoryDataProvider);
        }

        return categories;
    }



/* Menu Table */

    public void addToMenuItemsList() {

        // Beef mac
        menuItems.add(new MenuDataProvider(1, 1, 1, "Kofta Burger", "60$", "https://i.imgur.com/U1LnCLF.jpg", "61", "20"));
        menuItems.add(new MenuDataProvider(2, 1, 1, "Quarter Pounder", "85$", "https://i.imgur.com/0gph7tX.jpg", "90", "132"));
        menuItems.add(new MenuDataProvider(3, 1, 1, "Big Mac", "125$", "https://i.imgur.com/GkpPGEJ.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(4, 1, 1, "Big Tasty", "100$", "https://i.imgur.com/kgurzdM.jpg", "30", "25"));

        // chicken mac
        menuItems.add(new MenuDataProvider(5, 1, 2, "Grand Chicken", "60$", "https://i.imgur.com/0sasH7r.jpg", "61", "20"));
        menuItems.add(new MenuDataProvider(6, 1, 2, "Grand Spicy", "85$", "https://i.imgur.com/iojD29i.jpg", "90", "132"));
        menuItems.add(new MenuDataProvider(7, 1, 2, "Big Mac", "125$", "https://i.imgur.com/Jw51X3v.png", "55", "69"));
        menuItems.add(new MenuDataProvider(8, 1, 2, "Big Tasty", "100$", "https://i.imgur.com/Z3tpwDy.jpg", "30", "25"));

        // fish mac
        menuItems.add(new MenuDataProvider(9, 1, 3, "Filet Fish", "60$", "https://i.imgur.com/eHcQwtv.png", "55", "69"));
        menuItems.add(new MenuDataProvider(10, 1, 3, "Double Filet", "85$", "https://i.imgur.com/8XJhmw6.png", "30", "25"));

        // salads mac
        menuItems.add(new MenuDataProvider(11, 1, 4, "Chicken Salad", "20$", "https://i.imgur.com/OftiyDE.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(12, 1, 4, "Garden Side", "13$", "https://i.imgur.com/9zXj5lx.png", "30", "25"));

        // desert mac
        menuItems.add(new MenuDataProvider(13, 1, 5, "Blueberry sundae", "60$", "https://i.imgur.com/CKhgrfg.jpg", "61", "20"));
        menuItems.add(new MenuDataProvider(14, 1, 5, "Apple pie", "44$", "https://i.imgur.com/wFe0rMt.jpg", "90", "132"));
        menuItems.add(new MenuDataProvider(15, 1, 5, "Custard pie", "30$", "https://i.imgur.com/kdvRERb.png", "55", "69"));
        menuItems.add(new MenuDataProvider(16, 1, 5, "Wafel mcflurry", "35$", "https://i.imgur.com/cSr7aNt.jpg", "30", "25"));
        menuItems.add(new MenuDataProvider(17, 1, 5, "Oreo mcflurry", "20$", "https://i.imgur.com/85U8ks2.jpg", "30", "25"));

        // soft drinks mac
        menuItems.add(new MenuDataProvider(18, 1, 6, "Cola", "7$", "https://i.imgur.com/OQofgdZ.jpg", "61", "20"));
        menuItems.add(new MenuDataProvider(19, 1, 6, "Sprite", "7$", "https://i.imgur.com/WXj5Ema.png", "90", "132"));
        menuItems.add(new MenuDataProvider(20, 1, 6, "Mirinda", "8$", "https://i.imgur.com/VdRoBBa.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(21, 1, 6, "Orange juice", "3$", "https://i.imgur.com/8NIm3Tz.png", "30", "25"));

        // hor drinks mac
        menuItems.add(new MenuDataProvider(22, 1, 7, "Tea cup", "7$", "https://i.imgur.com/M3kgLV1.png", "90", "132"));
        menuItems.add(new MenuDataProvider(23, 1, 7, "Coffee cup", "8$", "https://i.imgur.com/CztJpdO.png", "55", "69"));
        menuItems.add(new MenuDataProvider(24, 1, 7, "Cappuccino", "10$", "https://i.imgur.com/905FADB.png", "30", "25"));

        // milk shakes mac
        menuItems.add(new MenuDataProvider(25, 1, 8, "Vanilla", "10$", "https://i.imgur.com/YeOP6Lr.png", "90", "132"));
        menuItems.add(new MenuDataProvider(26, 1, 8, "Chocolate", "15$", "https://i.imgur.com/ZnvXUYI.png", "55", "69"));
        menuItems.add(new MenuDataProvider(27, 1, 8, "Strawberry", "25$", "https://i.imgur.com/PXEQbEo.png", "30", "25"));

        // Beef papa johns
        menuItems.add(new MenuDataProvider(28, 2, 9, "Super papa's", "85$", "https://i.imgur.com/kBQeH1m.jpg", "61", "20"));
        menuItems.add(new MenuDataProvider(29, 2, 9, "Hot & Spicy", "90$", "https://i.imgur.com/Zz6HLCj.jpg", "90", "132"));
        menuItems.add(new MenuDataProvider(30, 2, 9, "Little italy", "80$", "https://i.imgur.com/SU45TAq.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(31, 2, 9, "Pepperoni", "100$", "https://i.imgur.com/wEspQ9y.jpg", "30", "25"));
        menuItems.add(new MenuDataProvider(32, 2, 9, "Smokey", "45$", "https://i.imgur.com/RHU6Wqt.jpg", "30", "25"));

        // Chicken papa johns
        menuItems.add(new MenuDataProvider(33, 2, 10, "Chicken BBQ", "90$", "https://i.imgur.com/eRAayKo.jpg", "90", "132"));
        menuItems.add(new MenuDataProvider(34, 2, 10, "Cha Cha", "80$", "https://i.imgur.com/GttftKG.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(35, 2, 10, "Mexican ole", "100$", "https://i.imgur.com/abe8tQc.jpg", "30", "25"));
        menuItems.add(new MenuDataProvider(36, 2, 10, "Chicken ranch", "45$", "https://i.imgur.com/MSDKu69.jpg", "30", "25"));

        // Fish papa johns
        menuItems.add(new MenuDataProvider(37, 2, 11, "Tuna pizza", "60$", "https://i.imgur.com/7eZoQHX.jpg", "55", "69"));
        menuItems.add(new MenuDataProvider(38, 2, 11, "Seafood pizza", "90$", "https://i.imgur.com/0rcwGbE.jpg", "30", "25"));
        menuItems.add(new MenuDataProvider(39, 2, 11, "Shrimp pizza", "70$", "https://i.imgur.com/M7LTba6.jpg", "30", "25"));

        // Veggie papa johns
        menuItems.add(new MenuDataProvider(40, 2, 12, "Garden special", "55$", "https://i.imgur.com/dCqGEpe.jpg", "30", "25"));

        // Cheese papa johns
        menuItems.add(new MenuDataProvider(41, 2, 13, "Margarita", "50$", "https://i.imgur.com/lF0CMVK.jpg", "30", "25"));
        menuItems.add(new MenuDataProvider(42, 2, 13, "6-Cheese", "55$", "https://i.imgur.com/2QrERs0.jpg", "30", "25"));

        // Desert papa johns
        menuItems.add(new MenuDataProvider(43, 2, 14, "Nutella pie", "40$", "https://i.imgur.com/7W3ybQ0.jpg", "30", "25"));

        // Soft drinks papa johns
        menuItems.add(new MenuDataProvider(44, 2, 15, "Pepsi", "7$", "https://i.imgur.com/HIkBPQx.png", "30", "25"));
        menuItems.add(new MenuDataProvider(45, 2, 15, "Mirinda", "5$", "https://i.imgur.com/VLKYsDa.png", "30", "25"));
        menuItems.add(new MenuDataProvider(46, 2, 15, "Seven up", "5$", "https://i.imgur.com/5xRdyek.png", "30", "25"));
    }

    public void insertIntoMenu() {
        this.addToMenuItemsList();

        SQLiteDatabase sql = this.getWritableDatabase();

        for (int i = 0; i < this.menuItems.size(); i++) {
            ContentValues values = new ContentValues();

            values.put("itemId", menuItems.get(i).getItemId());
            values.put("restId", menuItems.get(i).getRestId());
            values.put("catId", menuItems.get(i).getCatId());
            values.put("mealName", menuItems.get(i).getMealName());
            values.put("mealPrice", menuItems.get(i).getMealPrice());
            values.put("mealImg", menuItems.get(i).getMealImage());
            values.put("likesNum", menuItems.get(i).getLikesNum());
            values.put("dislikesNum", menuItems.get(i).getDislikesNum());

            // sql.insert("Menu", null, values);
            sql.insertWithOnConflict("Menu", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
    }

    public ArrayList<MenuDataProvider> getMenuRecords(int restId, int catId) {

        ArrayList<MenuDataProvider> menu = new ArrayList<MenuDataProvider>();

        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select itemId, mealName, mealPrice, mealImg, likesNum, dislikesNum from Menu where " +
                "restId = " + restId + " AND catId = " + catId, null);
        while (cursor != null && cursor.moveToNext()) {
            MenuDataProvider menuDataProvider = new MenuDataProvider();

            menuDataProvider.setItemId(cursor.getInt(cursor.getColumnIndex("itemId")));
            menuDataProvider.setMealName(cursor.getString(cursor.getColumnIndex("mealName")));
            menuDataProvider.setMealPrice(cursor.getString(cursor.getColumnIndex("mealPrice")));
            menuDataProvider.setMealImage(cursor.getString(cursor.getColumnIndex("mealImg")));
            menuDataProvider.setLikesNum(cursor.getString(cursor.getColumnIndex("likesNum")));
            menuDataProvider.setDislikesNum(cursor.getString(cursor.getColumnIndex("dislikesNum")));

            menu.add(menuDataProvider);
        }

        return menu;
    }

    /* RestOrders Table */

    public void fillRestOrdersTable() {
        SQLiteDatabase sql = this.getWritableDatabase();
        for (int i = 0; i < 2; i++) {
            ContentValues values = new ContentValues();
            values.put("rest_id", i + 1);
            values.put("isOrder", 0);
            sql.insertWithOnConflict("RestOrders", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public void updateIsOrder(int restId) {
        SQLiteDatabase sql = this.getWritableDatabase();

        sql.execSQL("UPDATE RestOrders set isOrder = 1 where rest_id = " + restId);
    }


    /* Orders Table */

    public void insertIntoOrdersTable(int restId, List<Order> myOrder) {
        SQLiteDatabase sql = this.getWritableDatabase();

        for (int i = 0; i < myOrder.size(); i++) {
            ContentValues values = new ContentValues();

            values.put("rest_id", restId);
            values.put("meal_id", myOrder.get(i).getMealId());
            values.put("mealName", myOrder.get(i).getMealName());
            values.put("mealImg", myOrder.get(i).getMealImg());
            values.put("mealPrice", myOrder.get(i).getMealPrice());
            values.put("mealQuantity", myOrder.get(i).getMealQuantity());

            sql.insertWithOnConflict("Orders", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
}
