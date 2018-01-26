package com.example.sheref.autoorder.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sheref.autoorder.Adapters.CartAdapter;
import com.example.sheref.autoorder.Model.CartDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class CartActivity extends AppCompatActivity {

    private Intent intent;
    private String order = "true";
    private List<Order> selectedItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Type type;
    private ListView listView;
    private CartAdapter adapter;
    private FloatingTextButton checkoutBtn;
    private int restId = 0;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);

        gson = new Gson();
        type = new TypeToken<List<Order>>() {}.getType();
        order = sharedPreferences.getString("order", "true");
        selectedItems = gson.fromJson(order, type);

        initialize();
        populateCart();
        checkoutBtnAction();


    }

    public void initialize() {
        listView = (ListView) findViewById(R.id.cartListView);
        adapter = new CartAdapter(getApplicationContext(), R.layout.cart_layout);
        listView.setAdapter(adapter);
    }

    public void populateCart() {


        for (int i = 0; i < selectedItems.size(); i++) {

            String str = selectedItems.get(i).getMealPrice();
            str = str.substring(0, str.length() - 1);
            int num = Integer.parseInt(str) * Integer.parseInt(selectedItems.get(i).getMealQuantity());

            adapter.add(new CartDataProvider(selectedItems.get(i).getMealImg(), selectedItems.get(i).getMealName(),
                    selectedItems.get(i).getMealPrice(), selectedItems.get(i).getMealQuantity(), String.valueOf(num) + "$",
                    R.drawable.plus_icon, R.drawable.minus_icon, R.drawable.delete_icon, selectedItems.get(i).getMealId()));
        }
    }

    public void checkoutBtnAction() {
        checkoutBtn = (FloatingTextButton) findViewById(R.id.checkoutBtn);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra("restId", restId);
                startActivity(intent);
            }
        });
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
            finish();
        } else if (id == R.id.homeBar) {
            intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.logoutBar) {
            sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("isLoged", "false");
            editor.apply();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return true;
    }
}
