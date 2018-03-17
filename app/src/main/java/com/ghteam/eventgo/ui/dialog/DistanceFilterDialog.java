package com.ghteam.eventgo.ui.dialog;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.DistanceFilterDialogBinding;

/**
 * Created by nikit on 16.03.2018.
 */

public class DistanceFilterDialog extends DialogFragment {

    private DistanceFilterDialogBinding dialogBinding;

    private int currentValue;

    private OnDistanceFilterChangeListener onDistanceFilterChanged;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dialogBinding = DataBindingUtil.inflate(inflater, R.layout.distance_filter_dialog, container, false);

        return dialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindClickListeners();

        dialogBinding.sbDistanceFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentValue = progress;
                dialogBinding.tvDistance.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogBinding.tvDistance.setText(dialogBinding.sbDistanceFilter.getProgress() + "");
    }

    private void bindClickListeners() {
        dialogBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialogBinding.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDistanceFilterChanged != null) {
                    onDistanceFilterChanged.onDistanceChanged(currentValue);
                }
                dismiss();
            }
        });
    }

    public void setOnDistanceFilterChanged(OnDistanceFilterChangeListener onDistanceFilterChanged) {
        this.onDistanceFilterChanged = onDistanceFilterChanged;
    }

    public interface OnDistanceFilterChangeListener {
        void onDistanceChanged(int value);
    }


}
