package com.ghteam.eventgo.ui.dialog.selectcategories;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.entity.Category;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.databinding.DialogSelectCategoriesBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesDialog extends DialogFragment {
    private DialogSelectCategoriesBinding dialogBinding;

    private Repository mRepository;
    private CategoriesViewModel viewModel;
    private CategoriesRecyclerAdapter recyclerAdapter;
    private OnConfirmChoiceListener mOnConfirmListener;

    private List<Category> selectedCategories = new ArrayList<>();

    public static final String TAG = CategoriesDialog.class.getSimpleName();

    public static final int NONE = CategoriesRecyclerAdapter.SELECTION_TYPE_NONE;
    public static final int SINGLE_SELECT = CategoriesRecyclerAdapter.SELECTION_TYPE_ENABLED;
    public static final int MULTI_SELECT = CategoriesRecyclerAdapter.SELECTION_TYPE_MULTI_SELECT;

    private int selectionType = NONE;

    public CategoriesDialog() {
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

        mRepository = Repository.getInstance(getContext());
        viewModel = ViewModelProviders.of(this, new CategoriesViewModelFactory(mRepository))
                .get(CategoriesViewModel.class);

        recyclerAdapter = new CategoriesRecyclerAdapter();
        recyclerAdapter.setSelectionType(selectionType);

        recyclerAdapter.setSelectItemListener(new CategoriesRecyclerAdapter.OnSelectItemListener() {
            @Override
            public void onSelected(Category category) {
                selectedCategories.add(category);
            }

            @Override
            public void onUnselected(Category category) {
                selectedCategories.remove(category);
            }
        });

        dialogBinding.rvCategoryList.setAdapter(recyclerAdapter);
        dialogBinding.rvCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        dialogBinding.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm(selectedCategories);
                }

                CategoriesDialog.this.dismiss();
            }
        });

        dialogBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesDialog.this.dismiss();
            }
        });

        registerViewModelObservers();

    }

    private void registerViewModelObservers() {
        viewModel.getCategoriesList().observeForever(new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categoryEntries) {
                recyclerAdapter.setItems(categoryEntries);
            }
        });

        viewModel.getLoadingTaskStatus().observeForever(new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                switch (taskStatus) {
                    case IN_PROGRESS:
                        dialogBinding.progressBar.setVisibility(View.INVISIBLE);
                        dialogBinding.rvCategoryList.setVisibility(View.INVISIBLE);
                        return;
                    default:
                        dialogBinding.progressBar.setVisibility(View.GONE);
                        dialogBinding.rvCategoryList.setVisibility(View.VISIBLE);
                        return;
                }
            }
        });
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public void setOnConfirmListener(OnConfirmChoiceListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public interface OnConfirmChoiceListener {
        void onConfirm(List<Category> categories);
    }
}
