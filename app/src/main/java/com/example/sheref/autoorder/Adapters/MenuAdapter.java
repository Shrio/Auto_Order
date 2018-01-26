package com.example.sheref.autoorder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheref.autoorder.Controller;
import com.example.sheref.autoorder.Model.MenuDataProvider;
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
 * Created by sheref on 07/01/2018.
 */
public class MenuAdapter extends ArrayAdapter {
    private Context context;
    private List<MenuDataProvider> data = new ArrayList<>();
    private Controller controller = new Controller();
    private LayoutInflater inflater;
    private List<Order> selectedItems = new ArrayList<Order>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;

    public MenuAdapter(Context context, int resource, List<Order> selectedItems) {
        super(context, resource);
        this.context = context;
        this.selectedItems = selectedItems;
        inflater = LayoutInflater.from(context);
        this.sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
        type = new TypeToken<List<Order>>() {}.getType();
    }


    public class DataHandler {
        private CircleImageView mealImage;
        private ImageView cartImage, likeImage, dislikeImage;
        private TextView mealName, mealPrice, likesNum, dislikesNum;

    }

    @Override
    public void add(Object object) {
        super.add(object);
        data.add((MenuDataProvider) object);
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }


    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        boolean isFind = false;

        View row = convertView;
        final DataHandler handler;
        if (row == null) {
            row = inflater.inflate(R.layout.menu_layout, parent, false);
            handler = new DataHandler();
            handler.mealImage = (CircleImageView) row.findViewById(R.id.mealImage);
            handler.cartImage = (ImageView) row.findViewById(R.id.cartImage);
            handler.likeImage = (ImageView) row.findViewById(R.id.like);
            handler.dislikeImage = (ImageView) row.findViewById(R.id.dislike);
            handler.mealName = (TextView) row.findViewById(R.id.mealName);
            handler.mealPrice = (TextView) row.findViewById(R.id.mealPrice);
            handler.likesNum = (TextView) row.findViewById(R.id.likesNum);
            handler.dislikesNum = (TextView) row.findViewById(R.id.dislikesNum);

            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        final MenuDataProvider provider;
        provider = (MenuDataProvider) this.getItem(position);

        for (int i = 0; i < selectedItems.size(); i++) {
            if (provider.getItemId() == selectedItems.get(i).getMealId()) {
                isFind = true;
                break;
            }
        }

        Picasso.with(context).load(provider.getMealImage()).into(handler.mealImage);
        handler.mealName.setText(provider.getMealName());
        handler.mealPrice.setText(provider.getMealPrice());

        if (isFind) {
            handler.cartImage.setImageResource(R.drawable.clear_cart);
            handler.cartImage.setTag("2");
        } else {
            handler.cartImage.setImageResource(provider.getCartImage());
        }

        handler.likeImage.setImageResource(provider.getLikeImage());
        handler.dislikeImage.setImageResource(provider.getDislikeImage());
        handler.likesNum.setText(provider.getLikesNum());
        handler.dislikesNum.setText(provider.getDislikesNum());

        handler.likeImage.setTag(provider.getLikeTag());
        handler.dislikeImage.setTag(provider.getDislikeTag());


        handler.cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json, order = "true";

                if (handler.cartImage.getTag().equals("1")) {
                    handler.cartImage.setTag("2");
                    handler.cartImage.setImageResource(R.drawable.clear_cart);
                    selectedItems.add(new Order(provider.getMealImage(), provider.getMealName(),
                            provider.getMealPrice(), provider.getItemId(), "1"));
                    order = gson.toJson(selectedItems, type);
                    editor.putString("order", order);
                    editor.apply();

                } else if (handler.cartImage.getTag().equals("2")) {
                    handler.cartImage.setTag("1");
                    handler.cartImage.setImageResource(R.drawable.add_cart);

                    order = sharedPreferences.getString("order", "true");
                    selectedItems = gson.fromJson(order, type);

                    for (int i = 0; i < selectedItems.size(); i++) {
                        if (provider.getItemId() == selectedItems.get(i).getMealId()) {
                            selectedItems.remove(i);
                            break;
                        }
                    }
                    order = gson.toJson(selectedItems, type);
                    editor.putString("order", order);
                    editor.apply();
                }
            }
        });


        handler.likeImage.setOnClickListener(new View.OnClickListener() {
            int num = 0;
            String likesNumTxt, disLikesNumTxt = "0";

            @Override
            public void onClick(View view) {

                if (handler.likeImage.getTag().equals("0")) {

                    handler.likeImage.setTag("1");
                    handler.likeImage.setImageResource(R.drawable.liked);
                    likesNumTxt = handler.likesNum.getText().toString();
                    num = Integer.parseInt(likesNumTxt);
                    num += 1;
                    handler.likesNum.setText(String.valueOf(num));
                    controller.executeUpdateLikesNum(context, provider.getItemId(), String.valueOf(num));
                    controller.executeUpdateMealLikes(context, provider.getItemId(), 1);

                    if (handler.dislikeImage.getTag().equals("1")) {

                        handler.dislikeImage.setTag("0");
                        handler.dislikeImage.setImageResource(R.drawable.dislike);
                        disLikesNumTxt = handler.dislikesNum.getText().toString();
                        num = Integer.parseInt(disLikesNumTxt);
                        num -= 1;
                        handler.dislikesNum.setText(String.valueOf(num));
                        controller.executeUpdateDislikesNum(context, provider.getItemId(), String.valueOf(num));
                    }
                }
            }
        });

        handler.dislikeImage.setOnClickListener(new View.OnClickListener() {
            int num = 0;
            String likesNumTxt, disLikesNumTxt = "0";

            @Override
            public void onClick(View view) {
                if (handler.dislikeImage.getTag().equals("0")) {
                    handler.dislikeImage.setTag("1");
                    handler.dislikeImage.setImageResource(R.drawable.disliked);
                    disLikesNumTxt = handler.dislikesNum.getText().toString();
                    num = Integer.parseInt(disLikesNumTxt);
                    num += 1;
                    handler.dislikesNum.setText(String.valueOf(num));

                    controller.executeUpdateDislikesNum(context, provider.getItemId(), String.valueOf(num));
                    controller.executeUpdateMealLikes(context, provider.getItemId(), 0);

                    if (handler.likeImage.getTag().equals("1")) {
                        handler.likeImage.setTag("0");
                        handler.likeImage.setImageResource(R.drawable.like);
                        likesNumTxt = handler.likesNum.getText().toString();
                        num = Integer.parseInt(likesNumTxt);
                        num -= 1;
                        handler.likesNum.setText(String.valueOf(num));
                        controller.executeUpdateLikesNum(context, provider.getItemId(), String.valueOf(num));
                    }
                }
            }
        });

        return row;

    }

}
