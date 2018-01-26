package com.example.sheref.autoorder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sheref.autoorder.Model.CheckoutDataProvider;
import com.example.sheref.autoorder.Model.Order;
import com.example.sheref.autoorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheref on 20/01/2018.
 */
public class CheckoutAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CheckoutDataProvider> data = new ArrayList<CheckoutDataProvider>();
    private List<Order> selectedItems = new ArrayList<Order>();


    public CheckoutAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public class DataHandler {
        CircleImageView mealCheckImg;
        TextView mealCheckNameTxt, mealCheckQuanTxt, mealSubCheckTxt;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        data.add((CheckoutDataProvider) object);
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final DataHandler handler;
        if (view == null) {
            view = inflater.inflate(R.layout.checkout_layout, parent, false);
            handler = new DataHandler();

            handler.mealCheckImg = (CircleImageView) view.findViewById(R.id.mealCheckImg);
            handler.mealCheckNameTxt = (TextView) view.findViewById(R.id.mealCheckName);
            handler.mealCheckQuanTxt = (TextView) view.findViewById(R.id.quanCheckNum);
            handler.mealSubCheckTxt = (TextView) view.findViewById(R.id.subTotalCheckNum);

            view.setTag(handler);
        } else {
            handler = (DataHandler) view.getTag();
        }

        final CheckoutDataProvider provider = (CheckoutDataProvider) this.getItem(position);

        Picasso.with(context).load(provider.getMealCheckImg()).into(handler.mealCheckImg);
        handler.mealCheckNameTxt.setText(provider.getMealCheckName());
        handler.mealCheckQuanTxt.setText(provider.getMealCheckQuan());
        handler.mealSubCheckTxt.setText(provider.getMealCheckSubTotal());

        return view;
    }

}
