package com.ghteam.eventgo.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by nikit on 21.11.2017.
 */

public class BindingAdapters {

    @BindingAdapter("android:src")
    public static void imageViewAdapter(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url).into(view);
    }

}
