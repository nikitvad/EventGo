package com.ghteam.eventgo.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * Created by nikit on 26.11.2017.
 */

public class ProgressBarUtil {
    public static void showProgressBar(ProgressBar progressBar, @Nullable Activity activity, @Nullable View parentView) {

        progressBar.setVisibility(View.VISIBLE);
        if (activity != null && parentView != null) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            parentView.setAlpha(0.5f);
        }
    }

    public static void hideProgressBar(ProgressBar progressBar, @Nullable Activity activity, @Nullable View parentView) {
        progressBar.setVisibility(View.GONE);
        if (activity != null && parentView != null) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            parentView.setAlpha(1f);
        }
    }
}
