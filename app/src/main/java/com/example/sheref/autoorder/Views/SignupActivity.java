package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.UserDataProvider;
import com.example.sheref.autoorder.R;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class SignupActivity extends AppCompatActivity {
    private EditText userNameTxt, addrTxt, phoneTxt, passTxt;
    private AppCompatButton createAccBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Controller controller = new Controller();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        initialize();
        createAccBtnAction();
    }

    public void initialize() {
        userNameTxt = (EditText) findViewById(R.id.usernameEdTxt);
        addrTxt = (EditText) findViewById(R.id.addEdTxt);
        phoneTxt = (EditText) findViewById(R.id.phoneEdTxt);
        passTxt = (EditText) findViewById(R.id.passEdTxt);

        createAccBtn = (AppCompatButton) findViewById(R.id.createAccBtn);
    }

    public void createAccBtnAction() {

        final UserDataProvider userDataProvider = new UserDataProvider();
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNameTxt.getText().toString().equals("") || addrTxt.getText().toString().equals("") ||
                        phoneTxt.getText().toString().equals("") || passTxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please .. fill all empty input fields.", Toast.LENGTH_LONG).show();
                } else if (phoneTxt.getText().toString().length() != 11) {

                    Toast.makeText(getApplicationContext(), "Phone number digits must equal to 11 digits.", Toast.LENGTH_LONG)
                            .show();
                } else if (passTxt.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password length must contain at least 8 characters or numbers.",
                            Toast.LENGTH_LONG).show();
                } else {
                    userDataProvider.setUserName(userNameTxt.getText().toString());
                    userDataProvider.setUserAddress(addrTxt.getText().toString());
                    userDataProvider.setUserPhone(phoneTxt.getText().toString());
                    userDataProvider.setUserPassword(passTxt.getText().toString());
                    userDataProvider.setUserId(1);

                    controller.executeInsertIntoUser(getApplicationContext(), userDataProvider);

                    sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("isLoged", "true");
                    editor.apply();

                    intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
