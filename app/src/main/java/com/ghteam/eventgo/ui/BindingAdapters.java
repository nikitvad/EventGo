package com.ghteam.eventgo.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
