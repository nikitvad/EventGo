package com.ghteam.eventgo.ui.dialog;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.CategoryEntry;
import com.ghteam.eventgo.databinding.FragmentSelectCategoriesDialogBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCategoriesDialog extends DialogFragment {
    private FragmentSelectCategoriesDialogBinding dialogBinding;

    private Repository mRepository;
    private CategoriesViewModel viewModel;

    public static final String TAG = SelectCategoriesDialog.class.getSimpleName();

    public SelectCategoriesDialog() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_select_categories_dialog, container, false);

        getDialog().setTitle("Select categories");
        return dialogBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRepository = Repository.getInstance(this.getContext());
        viewModel = ViewModelProviders.of(this, new CategoriesViewModelFactory(mRepository))
                .get(CategoriesViewModel.class);


        final RecyclerBindingAdapter<CategoryEntry> recyclerAdapter
                = new RecyclerBindingAdapter<>(R.layout.layout_category_item,
                BR.categoryEntry,
                new ArrayList<CategoryEntry>()
        );


        viewModel.getCategoriesList().observeForever(new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                recyclerAdapter.changeItems((ArrayList<CategoryEntry>) categoryEntries);
                Log.d(TAG, "onChanged: " + categoryEntries.toString());
            }
        });


        dialogBinding.rvCategoryList.setAdapter(recyclerAdapter);
        dialogBinding.rvCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));


    }
}
