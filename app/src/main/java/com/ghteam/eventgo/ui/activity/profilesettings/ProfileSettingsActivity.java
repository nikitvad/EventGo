package com.ghteam.eventgo.ui.activity.profilesettings;

import android.app.DialogFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.CategoryEntry;
import com.ghteam.eventgo.data.network.CategoriesDataSource;
import com.ghteam.eventgo.ui.activity.eventslist.EventsListActivity;
import com.ghteam.eventgo.ui.dialog.SelectCategoriesDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileSettingsActivity extends AppCompatActivity {

    private SelectCategoriesDialog selectCategoriesDialog;


    public static final String TAG = ProfileSettingsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        ButterKnife.bind(this);
        selectCategoriesDialog = new SelectCategoriesDialog();


//        categories = dataSource.getCurrentCategories();
//        dataSource.fetchCategories();
//
//
//        categories.observeForever(new Observer<List<CategoryEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
//                Log.d(TAG, "onChanged: " + categoryEntries.toString());
//            }
//        });

    }

    @OnClick(R.id.bt_submit)
    void submit() {
        selectCategoriesDialog.show(getSupportFragmentManager(), "TAG1");
    }
}
