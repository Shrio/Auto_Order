package com.example.sheref.autoorder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sheref.autoorder.Views.CategoryActivity;
import com.example.sheref.autoorder.Model.RestaurantDataProvider;
import com.example.sheref.autoorder.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheref
 * on 07/01/2018.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.DataHandler> {
    private List<RestaurantDataProvider> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Intent intent;
    private  int userId;

    public RestaurantListAdapter(List<RestaurantDataProvider> list, Context context) {
        this.list = list;
        this.context = context;
        this.userId = userId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DataHandler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rest_layout, parent, false);

        DataHandler handler = new DataHandler(view);

        return handler;
    }

    @Override
    public void onBindViewHolder(DataHandler holder, int position) {
        RestaurantDataProvider provider = list.get(position);

        holder.restName.setText(provider.getRestName());
        holder.restImage.setImageResource(provider.getRestImage());
        holder.restRatingBar.setRating(provider.getRestRatingBar());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        private CircleImageView restImage;
        private TextView restName;
        private RatingBar restRatingBar;

        public DataHandler(View itemView) {
            super(itemView);
            context = itemView.getContext();
            restImage = (CircleImageView) itemView.findViewById(R.id.logoImage);
            restName = (TextView) itemView.findViewById(R.id.restName);
            restRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String restNameTmp = restName.getText().toString();
                    int restId = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (restNameTmp.equals(list.get(i).getRestName())) {
                            restId = list.get(i).getRestId();
                            break;
                        }
                    }
                    intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("restId", restId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }

    }
}
