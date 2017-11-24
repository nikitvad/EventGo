package com.ghteam.eventgo.ui.activity.profilesettings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 24.11.2017.
 */

public class CategoryGridAdapter extends BaseAdapter {

    private List<Category> categories;

    public CategoryGridAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public CategoryGridAdapter() {
        categories = new ArrayList<>();
    }

    public void setItems(List<Category> items) {
        categories.clear();
        categories.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Category category = categories.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.layout_category_item, parent, false);
        } else {
            v = convertView;
        }

        ImageView image = v.findViewById(R.id.iv_category_icon);
        Picasso.with(parent.getContext()).load(category.getIcon()).into(image);
        TextView title = v.findViewById(R.id.tv_category_name);
        title.setText(category.getName());

        return v;
    }
}
