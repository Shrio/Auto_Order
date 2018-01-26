package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.sheref.autoorder.Adapters.CategoryAdapter;
import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.CategoryDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> categoryNames;
    private HashMap<String, List<CategoryDataProvider>> listDataChild;
    private List<CategoryDataProvider> foodCategories;
    private List<CategoryDataProvider> drinkCategories;
    private ArrayList<CategoryDataProvider> categories = new ArrayList<CategoryDataProvider>();
    private CategoryAdapter adapter;
    private Intent intent;
    private Controller controller = new Controller();
    private String catDataStatus = "true";
    private int restId, userId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Order>>() {
    }.getType();
    private List<Order> selectedItems = new ArrayList<Order>();
    String order = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);

        populateCategory(restId);
    }

    @Override
    public void onResume() {
        super.onResume();

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);

        populateCategory(restId);

    }

    public void populateCategory(final int restId) {

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains("isFirstCat")) {
            catDataStatus = sharedPreferences.getString("isFirstCat", "false");
        } else {
            editor.putString("isFirstCat", "true");
            editor.apply();
            catDataStatus = "true";
        }

        if (catDataStatus.equals("true")) {

            controller.executeInsertIntoCat(getApplicationContext());

            editor.putString("isFirstCat", "false");
            editor.apply();

            categories = controller.executeGetCatRecords(getApplicationContext(), restId);

        } else if (catDataStatus.equals("false")) {
            categories = controller.executeGetCatRecords(getApplicationContext(), restId);
        }

        expandableListView = (ExpandableListView) findViewById(R.id.categories);
        categoryNames = new ArrayList<>();
        listDataChild = new HashMap<String, List<CategoryDataProvider>>();
        foodCategories = new ArrayList<CategoryDataProvider>();
        drinkCategories = new ArrayList<CategoryDataProvider>();

        categoryNames.add("Food");
        categoryNames.add("Drinks");

        if (restId == 1) {

            for (int i = 0; i < categories.size(); i++) {
                if (i >= 5) {
                    drinkCategories.add(new CategoryDataProvider(categories.get(i).getCatName(), categories.get(i).getCatImage(),
                            categories.get(i).getRestId(), categories.get(i).getCatId()));
                } else {
                    foodCategories.add(new CategoryDataProvider(categories.get(i).getCatName(), categories.get(i).getCatImage(),
                            categories.get(i).getRestId(), categories.get(i).getCatId()));
                }
            }
        } else if (restId == 2) {

            for (int i = 0; i < categories.size(); i++) {
                if (i == categories.size() - 1) {
                    drinkCategories.add(new CategoryDataProvider(categories.get(i).getCatName(), categories.get(i).getCatImage(),
                            categories.get(i).getRestId(), categories.get(i).getCatId()));
                } else {
                    foodCategories.add(new CategoryDataProvider(categories.get(i).getCatName(), categories.get(i).getCatImage(),
                            categories.get(i).getRestId(), categories.get(i).getCatId()));
                }
            }
        }


        listDataChild.put(categoryNames.get(0), foodCategories);
        listDataChild.put(categoryNames.get(1), drinkCategories);

        adapter = new CategoryAdapter(categoryNames, getApplicationContext(), listDataChild);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                CategoryDataProvider provider = listDataChild.get(categoryNames.get(i)).get(i2);
                int catId = 0;
                for (int j = 0; j < categories.size(); j++) {
                    if (provider.getCatName().equals(categories.get(j).getCatName())) {
                        catId = categories.get(j).getCatId();
                        break;
                    }
                }
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("restId", restId);
                intent.putExtra("catId", catId);
                startActivity(intent);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profileBar) {
            intent = new Intent(getApplicationContext(), AccountEditingActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.homeBar) {
            intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if(id == R.id.logoutBar) {
            sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("isLoged", "false");
            editor.apply();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }  else if (id == R.id.goCart) {
            sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
            if (sharedPreferences.contains("order")) {
                order = sharedPreferences.getString("order", "true");
                selectedItems = gson.fromJson(order, type);
                if (selectedItems.size() == 0) {
                    Toast.makeText(getApplicationContext(), "The cart is empty .. Choose your items in first from menu."
                            , Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(getApplicationContext(), "The cart is empty .. Choose your items in first from menu."
                        , Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }
}
