package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.Model.UserDataProvider;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccountEditingActivity extends AppCompatActivity {

    private AppCompatButton submitBtn;
    private EditText userNameTxt, addressTxt, phoneTxt;
    private Controller controller;
    private UserDataProvider user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Order>>() {
    }.getType();
    private List<Order> selectedItems = new ArrayList<Order>();
    String order = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editing);

        initialize();
        showUserData();
        updateUserData();
    }

    public void initialize() {
        submitBtn = (AppCompatButton) findViewById(R.id.submitBtn);
        userNameTxt = (EditText) findViewById(R.id.usernameAccEdit);
        addressTxt = (EditText) findViewById(R.id.addressAccEdit);
        phoneTxt = (EditText) findViewById(R.id.phoneAccEdit);

        controller = new Controller();
        user = new UserDataProvider();
    }

    public void showUserData() {
        user = controller.exeuteGetUserRecord(getApplicationContext());

        userNameTxt.setText(user.getUserName());
        addressTxt.setText(user.getUserAddress());
        phoneTxt.setText(user.getUserPhone());
    }

    public void updateUserData() {

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userNameTxt.getText().toString().equals("") || addressTxt.getText().toString().equals("")
                        || phoneTxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, fill all input fields", Toast.LENGTH_LONG).show();

                } else if (phoneTxt.getText().toString().length() != 11) {
                    Toast.makeText(getApplicationContext(), "Phone number digits must equal to 11 digits.", Toast.LENGTH_LONG)
                            .show();
                } else {
                    user.setUserName(userNameTxt.getText().toString());
                    user.setUserPhone(phoneTxt.getText().toString());
                    user.setUserAddress(addressTxt.getText().toString());

                    controller.executeUpdateUserData(getApplicationContext(), user);

                    Toast.makeText(getApplicationContext(), "User data updated successfully.", Toast.LENGTH_LONG).show();
                }
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

        if (id == R.id.homeBar) {
            intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.logoutBar) {
            sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("isLoged", "false");
            editor.apply();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(getApplicationContext(), "The cart is empty .. Choose your items in first from menu."
                        , Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }
}
