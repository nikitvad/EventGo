package com.ghteam.eventgo.ui.dialog.selectcategories;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.databinding.DialogSelectCategoriesBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCategoriesDialog extends DialogFragment {
    private DialogSelectCategoriesBinding dialogBinding;

    private Repository mRepository;
    private CategoriesViewModel viewModel;
    private CategoriesRecyclerAdapter recyclerAdapter;
    private OnConfirmChoiceListener mOnConfirmListener;

    private HashSet<Category> selectedCategories = new HashSet<>();

    public static final String TAG = SelectCategoriesDialog.class.getSimpleName();

    public SelectCategoriesDialog() {
    }

    @Override
    public void onStart() {
        super.onStart();
        selectedCategories.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dialogBinding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_select_categories, container, false);

        return dialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRepository = Repository.getInstance(this.getContext());
        viewModel = ViewModelProviders.of(this, new CategoriesViewModelFactory(mRepository))
                .get(CategoriesViewModel.class);

        recyclerAdapter = new CategoriesRecyclerAdapter();
        recyclerAdapter.setEnablToSelectItems(true);

        recyclerAdapter.setSelectItemListener(new CategoriesRecyclerAdapter.OnSelectItemListener() {
            @Override
            public void onSelected(Category category) {
                selectedCategories.add(category);
                Log.d(TAG, "onSelected: " + selectedCategories.toString());
            }

            @Override
            public void onUnselected(Category category) {
                selectedCategories.remove(category);
                Log.d(TAG, "onUnselected: " + selectedCategories.toString());
            }
        });

        dialogBinding.rvCategoryList.setAdapter(recyclerAdapter);
        dialogBinding.rvCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        dialogBinding.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm(selectedCategories);
                }

                SelectCategoriesDialog.this.dismiss();
            }
        });

        dialogBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCategoriesDialog.this.dismiss();
            }
        });

        registerViewModelObservers();

    }

    private void registerViewModelObservers() {
        viewModel.getCategoriesList().observeForever(new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categoryEntries) {
                recyclerAdapter.setItems(categoryEntries);
                if (recyclerAdapter.getItemCount() > 0) {
                    viewModel.getIsLoading().setValue(false);
                } else {
                    viewModel.getIsLoading().setValue(true);
                }
            }
        });

        viewModel.getIsLoading().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    dialogBinding.progressBar.setVisibility(View.INVISIBLE);
                    dialogBinding.rvCategoryList.setVisibility(View.INVISIBLE);
                } else {
                    dialogBinding.progressBar.setVisibility(View.GONE);
                    dialogBinding.rvCategoryList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setOnConfirmListener(OnConfirmChoiceListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public interface OnConfirmChoiceListener {
        void onConfirm(Set<Category> categories);
    }


}
