package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.UserDataProvider;
import com.example.sheref.autoorder.R;


import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameTxt, passwordTxt;
    private TextView signupLink;
    private Intent intent;
    private Controller controller = new Controller();
    private AppCompatButton logInBtn;
    private String restDataStatus = "true";
    private UserDataProvider user = new UserDataProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initialize();
        signUpLinkAction();
        signInBtnAction();
    }

    public void initialize() {
        logInBtn = (AppCompatButton) findViewById(R.id.loginBtn);
        signupLink = (TextView) findViewById(R.id.link_signup);
        userNameTxt = (EditText) findViewById(R.id.userNameEditTxt);
        passwordTxt = (EditText) findViewById(R.id.passEditTxt);
    }

    public void signInBtnAction() {


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNameTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                } else {
                    user = controller.exeuteGetUserRecord(getApplicationContext());

                    if (user.getUserName().equals(userNameTxt.getText().toString()) &&
                            user.getUserPassword().equals(passwordTxt.getText().toString())) {

                        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isLoged", "true");
                        editor.apply();

                        intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void signUpLinkAction() {
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
