package com.ghteam.eventgo.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghteam.eventgo.data_new.entity.Location;
import com.ghteam.eventgo.util.network.LocationUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by nikit on 21.11.2017.
 */

public class BindingAdapters {

    @BindingAdapter("android:src")
    public static void imageViewAdapter(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url).into(view);
    }

    @BindingAdapter({"address", "location"})
    public static void addressLocationAdapter(TextView textView, String address, Location location) {
        String finalText = "";

        if (address != null) {
            finalText = finalText + " " + address;
        }

        if (location != null && LocationUtil.getLastKnownLocation() != null) {
            android.location.Location lastKnownLocation = LocationUtil.getLastKnownLocation();

            double distance = LocationUtil.calculateDistance(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude());


            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            finalText = finalText + " " + decimalFormat.format(distance) + " km";
        }

        textView.setText(finalText);
    }

}
