package com.example.sheref.autoorder.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sheref.autoorder.Adapters.MenuAdapter;
import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.MenuDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private MenuAdapter adapter;
    private int cartImage = R.drawable.add_cart;
    private int restId, catId = 0;
    private String menuDataStatus = "true";
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<Order> selectedItems;
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Order>>() {}.getType();;
    private ArrayList<MenuDataProvider> menu = new ArrayList<MenuDataProvider>();
    private ArrayList<String> itemLikes = new ArrayList<String>();
    private Controller controller = new Controller();
    private FloatingTextButton cartBtn;
    private String order= "true";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        if (sharedPreferences.contains("order")) {
            order = sharedPreferences.getString("order", "true");
            selectedItems = gson.fromJson(order, type);
        } else {
            selectedItems = new ArrayList<Order>();
        }

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);
        catId = intent.getIntExtra("catId", 0);

        populateMenu(restId, catId, selectedItems);
       // cartBtnAction();
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        if (sharedPreferences.contains("order")) {
            order = sharedPreferences.getString("order", "true");
            selectedItems = gson.fromJson(order, type);
        } else {
            selectedItems = new ArrayList<Order>();
        }

        intent = getIntent();
        restId = intent.getIntExtra("restId", 0);
        catId = intent.getIntExtra("catId", 0);

        populateMenu(restId, catId, selectedItems);
        //cartBtnAction();
    }

    public void populateMenu(int restId, int catId, List<Order> selectedItems) {

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MenuAdapter(getApplicationContext(), R.layout.menu_layout, selectedItems);
        listView.setAdapter(adapter);

        editor = sharedPreferences.edit();

        if (sharedPreferences.contains("isFirstMenu")) {
            menuDataStatus = sharedPreferences.getString("isFirstMenu", "false");
        } else {
            editor.putString("isFirstMenu", "true");
            editor.apply();
            menuDataStatus = "true";
        }

        if (menuDataStatus.equals("true")) {
            controller.executeInsertIntoMenu(getApplicationContext());
            editor.putString("isFirstMenu", "false");
            editor.apply();
            menu = controller.executeGetMenuRecords(getApplicationContext(), restId, catId);
            itemLikes = controller.executeGetMealLikesRecords(getApplicationContext());
        } else {

            menu = controller.executeGetMenuRecords(getApplicationContext(), restId, catId);
            itemLikes = controller.executeGetMealLikesRecords(getApplicationContext());
        }

        MenuDataProvider dataProvider = new MenuDataProvider();
        boolean flag = false;
        for (int i = 0; i < menu.size(); i++) {

            for (int j = 0; j < itemLikes.size(); j++) {
                flag = false;
                String[] strArr = itemLikes.get(j).split(",");
                int itemId = Integer.parseInt(strArr[0]);

                if (itemId == menu.get(i).getItemId()) {
                    flag = true;
                    if (strArr[1].equals("1")) {
                        dataProvider = new MenuDataProvider(menu.get(i).getMealName(), menu.get(i).getMealPrice(),
                                menu.get(i).getLikesNum(), menu.get(i).getDislikesNum(), menu.get(i).getMealImage(),
                                R.drawable.liked, R.drawable.dislike, cartImage, menu.get(i).getItemId());
                        dataProvider.setLikeTag("1");
                        dataProvider.setDislikeTag("0");
                        adapter.add(dataProvider);
                        break;
                    } else {
                        dataProvider = new MenuDataProvider(menu.get(i).getMealName(), menu.get(i).getMealPrice(),
                                menu.get(i).getLikesNum(), menu.get(i).getDislikesNum(), menu.get(i).getMealImage(),
                                R.drawable.like, R.drawable.disliked, cartImage, menu.get(i).getItemId());
                        dataProvider.setLikeTag("0");
                        dataProvider.setDislikeTag("1");
                        adapter.add(dataProvider);
                        break;
                    }
                }
            }

            if (flag == false) {
                dataProvider = new MenuDataProvider(menu.get(i).getMealName(), menu.get(i).getMealPrice(),
                        menu.get(i).getLikesNum(), menu.get(i).getDislikesNum(), menu.get(i).getMealImage(),
                        R.drawable.like, R.drawable.dislike, cartImage, menu.get(i).getItemId());
                dataProvider.setLikeTag("0");
                dataProvider.setDislikeTag("0");
                adapter.add(dataProvider);
            }
        }
    }

  /*  public void cartBtnAction() {
     cartBtn = (FloatingTextButton) findViewById(R.id.cartBtn);

     cartBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             intent = new Intent(getApplicationContext(), CartActivity.class);
             intent.putExtra("restId", restId);
             startActivity(intent);
             finish();
         }
     });
 }
*/

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                   // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
