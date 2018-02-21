package com.ghteam.eventgo.ui;

import android.databinding.BindingAdapter;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
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

    @BindingAdapter("android:text")
    public static void intAdapter(TextView textView, int numb) {
        textView.setText(numb + "");
    }

    @BindingAdapter("android:text")
    public static void dateAdapter(TextView textView, Date date) {
        if(date!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd 'at' hh:mm a", Locale.ENGLISH);

            textView.setText(simpleDateFormat.format(date));
        }else{
            textView.setText("");
        }
    }

    @BindingAdapter("android:text")
    public static void messageAdapter(TextView textView, DiscussionMessage discussionMessage) {
        String ownerName = discussionMessage.getOwnerName();
        String message = discussionMessage.getMessage();

        textView.setText(ownerName + " " + message, TextView.BufferType.SPANNABLE);

        Spannable spannable = (Spannable) textView.getText();
        int end = ownerName.length();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(textView.getResources()
                .getColor(R.color.lightBlue));
        spannable.setSpan(foregroundColorSpan, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
