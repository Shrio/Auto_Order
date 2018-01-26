package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sheref.autoorder.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private Intent intent = new Intent();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (!sharedPreferences.contains("isLoged")) {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                } else {
                    String isLoged = sharedPreferences.getString("isLoged", "false");

                    if (isLoged.equals("false")) {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
