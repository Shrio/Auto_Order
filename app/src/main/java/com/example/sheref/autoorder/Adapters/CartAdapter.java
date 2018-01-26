package com.example.sheref.autoorder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheref.autoorder.Model.CartDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheref on 19/01/2018.
 */
public class CartAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<CartDataProvider> dataCart = new ArrayList<CartDataProvider>();
    private LayoutInflater inflater;
    private String quantity = "1";
    private int num = 0;
    private List<Order> selectedItems = new ArrayList<Order>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;

    public CartAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
        type = new TypeToken<List<Order>>() {
        }.getType();
    }

    class DataHandler {
        TextView mealNameTxt, mealPriceTxt, mealTotalPriceTxt, mealQuantutyTxt;
        ImageView plusImgView, minusImgView, deleteImgView;
        CircleImageView mealImgView;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        dataCart.add((CartDataProvider) object);
    }

    @Override
    public int getCount() {
        return this.dataCart.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dataCart.get(position);
    }

    @Override

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final DataHandler handler;
        if (view == null) {
            view = inflater.inflate(R.layout.cart_layout, parent, false);
            handler = new DataHandler();
            handler.mealImgView = (CircleImageView) view.findViewById(R.id.mealCartImg);
            handler.deleteImgView = (ImageView) view.findViewById(R.id.removeImg);
            handler.plusImgView = (ImageView) view.findViewById(R.id.plusImg);
            handler.minusImgView = (ImageView) view.findViewById(R.id.minusImg);

            handler.mealNameTxt = (TextView) view.findViewById(R.id.mealCartName);
            handler.mealPriceTxt = (TextView) view.findViewById(R.id.mealPriceCartTxtView);
            handler.mealQuantutyTxt = (TextView) view.findViewById(R.id.mealQuantityTxt);
            handler.mealTotalPriceTxt = (TextView) view.findViewById(R.id.mealTotalPriceCartTxtView);

            view.setTag(handler);
        } else {
            handler = (DataHandler) view.getTag();
        }

        final CartDataProvider provider = (CartDataProvider) this.getItem(position);

        handler.deleteImgView.setImageResource(provider.getDeleteImg());
        handler.plusImgView.setImageResource(provider.getPlusImg());
        handler.minusImgView.setImageResource(provider.getMinusImg());
        Picasso.with(context).load(provider.getMealImg()).into(handler.mealImgView);

        handler.mealNameTxt.setText(provider.getMealName());
        handler.mealPriceTxt.setText(provider.getMealPrice());
        handler.mealTotalPriceTxt.setText(provider.getMealTotalPrice());
        handler.mealQuantutyTxt.setText(provider.getMealQuantity());

        plusImgAction(handler, provider);
        minusImgAction(handler, provider);
        deleteImgAction(handler, provider, position);

        return view;
    }

    public void plusImgAction(final DataHandler handler, final CartDataProvider provider) {

        handler.plusImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "true";
                quantity = handler.mealQuantutyTxt.getText().toString();
                num = Integer.parseInt(quantity);
                num++;
                int quantity = num;
                handler.mealQuantutyTxt.setText(String.valueOf(num));
                String str = handler.mealPriceTxt.getText().toString();
                str = str.substring(0, str.length() - 1);
                num = num * Integer.parseInt(str);

                handler.mealTotalPriceTxt.setText(String.valueOf(num) + "$");


                json = sharedPreferences.getString("order", "true");
                selectedItems = gson.fromJson(json, type);
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).getMealId() == provider.getItemId()) {
                        selectedItems.get(i).setMealQuantity(String.valueOf(quantity));
                        break;
                    }
                }

                json = gson.toJson(selectedItems, type);
                editor.putString("order", json);
                editor.apply();
            }
        });
    }

    public void minusImgAction(final DataHandler handler, final CartDataProvider provider) {
        handler.minusImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "true";
                quantity = handler.mealQuantutyTxt.getText().toString();
                num = Integer.parseInt(quantity);
                num--;
                if (num == 0) {
                    handler.mealQuantutyTxt.setText("1");
                    Toast.makeText(context, "If you want to delete this item from cart .. Press on delete icon.", Toast.LENGTH_LONG).show();
                } else {
                    handler.mealQuantutyTxt.setText(String.valueOf(num));
                    int quantity = num;
                    String str = handler.mealPriceTxt.getText().toString();
                    str = str.substring(0, str.length() - 1);
                    num = num * Integer.parseInt(str);

                    handler.mealTotalPriceTxt.setText(String.valueOf(num) + "$");

                    json = sharedPreferences.getString("order", "true");
                    selectedItems = gson.fromJson(json, type);
                    for (int i = 0; i < selectedItems.size(); i++) {
                        if (selectedItems.get(i).getMealId() == provider.getItemId()) {
                            selectedItems.get(i).setMealQuantity(String.valueOf(quantity));
                            break;
                        }
                    }

                    json = gson.toJson(selectedItems, type);
                    editor.putString("order", json);
                    editor.apply();
                }
            }
        });
    }

    public void deleteImgAction(final DataHandler handler, final CartDataProvider provider, final int position) {
        handler.deleteImgView.setOnClickListener(new View.OnClickListener() {
            String json = "true";

            @Override
            public void onClick(View view) {

                dataCart.remove(position);
                notifyDataSetChanged();

                json = sharedPreferences.getString("order", "true");
                selectedItems = gson.fromJson(json, type);
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).getMealId() == provider.getItemId()) {
                        selectedItems.remove(i);
                        break;
                    }
                }
                json = gson.toJson(selectedItems, type);
                editor.putString("order", json);
                editor.apply();
            }
        });
    }

}
