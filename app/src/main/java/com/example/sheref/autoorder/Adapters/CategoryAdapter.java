package com.example.sheref.autoorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheref.autoorder.Model.CategoryDataProvider;
import com.example.sheref.autoorder.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sheref on 07/01/2018.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {

    private List<String> headerNames;
    private HashMap<String, List<CategoryDataProvider>> listDataChild;
    private Context context;
    private LayoutInflater inflater;

    public CategoryAdapter(List<String> headerNames, Context context, HashMap<String, List<CategoryDataProvider>> listDataChild) {
        this.headerNames = headerNames;
        this.context = context;
        this.listDataChild = listDataChild;
        inflater = LayoutInflater.from(context);
    }

    public class DataHandler {
        private ImageView catIcon;
        private TextView catName;
    }

    @Override
    public int getGroupCount() {
        return headerNames.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listDataChild.get(this.headerNames.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return headerNames.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.listDataChild.get(this.headerNames.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerName = (String) this.getGroup(i);
        view = inflater.inflate(R.layout.category_header, viewGroup, false);

        TextView headerNameTxt = (TextView) view.findViewById(R.id.foodCatTxtView);
        headerNameTxt.setText(headerName);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        CategoryDataProvider provider = (CategoryDataProvider) this.getChild(i, i1);
        view = inflater.inflate(R.layout.category_list, viewGroup, false);
        DataHandler handler = new DataHandler();

        handler.catIcon = (ImageView) view.findViewById(R.id.catIcon);
        handler.catName = (TextView) view.findViewById(R.id.categoryName);

        handler.catName.setText(provider.getCatName());
        handler.catIcon.setImageResource(provider.getCatImage());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    }
