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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheref.autoorder.Adapters.CheckoutAdapter;
import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.CheckoutDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class CheckoutActivity extends AppCompatActivity {

    private int restId = 0;
    private Intent intent;
    private FloatingTextButton confirmBtn;
    private ListView listView;
    private CheckoutAdapter adapter;
    private List<Order> selectedItems = new ArrayList<Order>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;
    private String order = "true";
    private int subTotal = 0;
    private Controller controller = new Controller();
    int totalPrice = 0;
    String deliveryNum = "10$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        this.sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        this.gson = new Gson();
        type = new TypeToken<List<Order>>() {}.getType();

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);

        order = sharedPreferences.getString("order", "true");
        selectedItems = gson.fromJson(order, type);

        initialize();
        populateCheckList();
        calcTotalPrice();
        confirmBtnAction();

    }

    public void initialize() {
        listView = (ListView) findViewById(R.id.checkoutList);
        adapter = new CheckoutAdapter(getApplicationContext(), R.layout.checkout_layout);
        listView.setAdapter(adapter);
        confirmBtn = (FloatingTextButton) findViewById(R.id.confirmBtn);
    }

    public void populateCheckList() {

        subTotal = 0;
        for (int i = 0; i < selectedItems.size(); i++) {

            String str = selectedItems.get(i).getMealPrice();
            str = str.substring(0, str.length() - 1);
            int num = Integer.parseInt(str) * Integer.parseInt(selectedItems.get(i).getMealQuantity());

            adapter.add(new CheckoutDataProvider(selectedItems.get(i).getMealImg(), selectedItems.get(i).getMealName(),
                    selectedItems.get(i).getMealQuantity(), String.valueOf(num) + "$"));
            subTotal += num;
        }
    }


    public void calcTotalPrice() {

        deliveryNum = deliveryNum.substring(0, deliveryNum.length() - 1);
        totalPrice = Integer.parseInt(deliveryNum) + subTotal;
    }

    public void confirmBtnAction() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CheckoutActivity.this).setIcon(R.drawable.confirmation_alert)
                        .setTitle("Confirmation").setMessage("SubTotal: $" + subTotal + '\n' + '\n' + "Delivery: $" + deliveryNum
                        + '\n' + '\n' + "Total: $" + totalPrice + '\n' + "")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                controller.executeInsertIntoOrdersTable(getApplicationContext(), restId, selectedItems);
                                controller.executeUpdateIsOrder(getApplicationContext(), restId);

                                sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.remove("order");
                                editor.apply();

                                intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                }).show();
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
        } else if (id == R.id.goCart) {
            intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
