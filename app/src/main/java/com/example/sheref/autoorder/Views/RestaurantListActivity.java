package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.Model.RestaurantDataProvider;
import com.example.sheref.autoorder.Adapters.RestaurantListAdapter;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private Intent intent;
    private List<RestaurantDataProvider> restaurants = new ArrayList<>();
    private String restDataStatus = "true";
    private int userId = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Controller controller = new Controller();
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Order>>() {
    }.getType();
    private List<Order> selectedItems = new ArrayList<Order>();
    String order = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        getSupportActionBar().setIcon(R.drawable.go_cart);

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("isFirstTime")) {
            restDataStatus = sharedPreferences.getString("isFirstTime", "false");
        } else {
            editor.putString("isFirstTime", "true");
            editor.apply();
            restDataStatus = sharedPreferences.getString("isFirstTime", "true");
        }

        populateRestaurants(userId);
    }

    public void populateRestaurants(int userId) {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (restDataStatus.equals("true")) {
            if (controller.executeInsertIntoRest(getApplicationContext())) {
                sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("isFirstTime", "false");
                editor.apply();
                restaurants = controller.exeuteGetRestRecords(getApplicationContext());
            }
        } else if (restDataStatus.equals("false")) {
            restaurants = controller.exeuteGetRestRecords(getApplicationContext());
        }
        adapter = new RestaurantListAdapter(restaurants, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profileBar) {
            intent = new Intent(getApplicationContext(), AccountEditingActivity.class);
            startActivity(intent);
        } else if (id == R.id.logoutBar) {
            sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("isLoged", "false");
            editor.apply();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.goCart) {
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
