package com.ghteam.eventgo.ui.dialog.datetimepicker;

import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.DateTimePickerBinding;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nikit on 15.12.2017.
 */

public class DateAndTimePicker extends DialogFragment {
    private DateTimePickerBinding dateTimePickerBinding;
    private ViewSwitcher viewSwitcher;

    private DatePicker datePicker;
    private TimePicker timePicker;

    private OnOkClickListener onOkClickListener;

    private int date = 0;
    private int month = 0;
    private int year = 0;

    private int hour = 0;
    private int minute = 0;

    private static final int DATE_PICKER = 0;
    private static final int TIME_PICKER = 1;

    private int currentPicker;

    private static final String TAG = DateAndTimePicker.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        dateTimePickerBinding = DataBindingUtil.inflate(inflater, R.layout.date_time_picker, container, false);

        return dateTimePickerBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        datePicker = dateTimePickerBinding.datePicker;
        timePicker = dateTimePickerBinding.timePicker;

        currentPicker = DATE_PICKER;

        viewSwitcher = dateTimePickerBinding.viewSwitcher;

        Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.view_scale_up);
        anim.setDuration(10);

        dateTimePickerBinding.textDate.startAnimation(anim);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                DateAndTimePicker.this.hour = hourOfDay;
                DateAndTimePicker.this.minute = minute;

                Log.d(TAG, "onTimeChanged: " + hourOfDay);
            }
        });

        dateTimePickerBinding.btOkNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPicker == DATE_PICKER) {

                    currentPicker = TIME_PICKER;

                    date = datePicker.getDayOfMonth();
                    month = datePicker.getMonth();
                    year = datePicker.getYear();

                    Log.d(TAG, "onClick: " + year);

                    setViewSwitcherAnimNext(view.getContext());
                    viewSwitcher.showNext();

                    startNextViewAnimation(view.getContext());

                    dateTimePickerBinding.btOkNext.setText("Ok");
                    dateTimePickerBinding.btBack.setVisibility(View.VISIBLE);
                } else {
                    if (onOkClickListener != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, date, hour, minute);

                        onOkClickListener.onOkClick(calendar.getTime());
                    }
                    DateAndTimePicker.this.dismiss();
                }
            }
        });

        dateTimePickerBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateAndTimePicker.this.dismiss();
            }
        });

        dateTimePickerBinding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPicker = DATE_PICKER;
                setViewSwitcherAnimPrev(view.getContext());
                dateTimePickerBinding.btBack.setVisibility(View.GONE);
                viewSwitcher.showPrevious();
                startPrevViewAnimation(view.getContext());
            }
        });
    }

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    private void setViewSwitcherAnimNext(Context context) {
        Animation animIn = AnimationUtils.loadAnimation(context, R.anim.view_in_left);
        Animation animOut = AnimationUtils.loadAnimation(context, R.anim.view_out_left);

        viewSwitcher.setInAnimation(animIn);
        viewSwitcher.setOutAnimation(animOut);
    }

    private void setViewSwitcherAnimPrev(Context context) {
        Animation animIn = AnimationUtils.loadAnimation(context, R.anim.view_in_right);
        Animation animOut = AnimationUtils.loadAnimation(context, R.anim.view_out_right);

        viewSwitcher.setInAnimation(animIn);
        viewSwitcher.setOutAnimation(animOut);
    }

    private void startNextViewAnimation(Context context) {
        Animation animScaleUp = AnimationUtils.loadAnimation(context, R.anim.view_scale_up);
        Animation animScaleDown = AnimationUtils.loadAnimation(context, R.anim.view_scale_down);

        dateTimePickerBinding.textDate.startAnimation(animScaleDown);
        dateTimePickerBinding.textTime.startAnimation(animScaleUp);
    }

    private void startPrevViewAnimation(Context context) {
        Animation animScaleUp = AnimationUtils.loadAnimation(context, R.anim.view_scale_up);
        Animation animScaleDown = AnimationUtils.loadAnimation(context, R.anim.view_scale_down);

        dateTimePickerBinding.textDate.startAnimation(animScaleUp);
        dateTimePickerBinding.textTime.startAnimation(animScaleDown);
    }

    public interface OnOkClickListener {
        void onOkClick(Date date);
    }
}