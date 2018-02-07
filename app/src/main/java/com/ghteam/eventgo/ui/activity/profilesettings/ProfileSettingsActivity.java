package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.databinding.ActivityProfileSettingsV2Binding;
import com.ghteam.eventgo.ui.adapter.SelectedCategoriesRecyclerAdapter;
import com.ghteam.eventgo.ui.dialog.selectcategories.CategoriesDialog;
import com.ghteam.eventgo.util.CustomTextWatcher;
import com.ghteam.eventgo.util.InjectorUtil;
import com.ghteam.eventgo.util.PrefsUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;

public class ProfileSettingsActivity extends AppCompatActivity {

    private CategoriesDialog categoriesDialog;
    private ProfileSettingsViewModel viewModel;
    private ActivityProfileSettingsV2Binding activityBinding;

    private InputMethodManager mInputMethodManager;

    private SelectedCategoriesRecyclerAdapter categoriesRecyclerAdapter;

    public static final String TAG = ProfileSettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_profile_settings_v2);

        bindTextChangeListeners();
        categoriesDialog = getCategoriesDialog();

        viewModel = ViewModelProviders.of(this,
                InjectorUtil.profileSettingViewModelFactory(this))
                .get(ProfileSettingsViewModel.class);

        categoriesRecyclerAdapter = new SelectedCategoriesRecyclerAdapter();

        categoriesRecyclerAdapter.setItemClickListener(new SelectedCategoriesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category item) {
                viewModel.getCategories().removeItem(item);
            }
        });

        categoriesRecyclerAdapter.setAddItemClickListener(new SelectedCategoriesRecyclerAdapter.OnAddItemClickListener() {
            @Override
            public void onAddItemClick() {
                categoriesDialog.show(getSupportFragmentManager(), "TAG");
            }
        });

        activityBinding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateUser();
            }
        });

        activityBinding.glSelectedCategories.setAdapter(categoriesRecyclerAdapter);
        activityBinding.glSelectedCategories.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));


        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        registerViewModelObservers();

        viewModel.loadCurrentUser();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerViewModelObservers();
    }

    private void bindUserToUi(User user) {
        activityBinding.etFirstName.setText(user.getFirstName());
        activityBinding.etLastName.setText(user.getLastName());
        activityBinding.etDescribeYourself.setText(user.getDescription());

        if (!user.getProfileImageUrl().isEmpty()) {
            Picasso.with(this).load(user.getProfileImageUrl())
                    .into(activityBinding.ivProfilePhoto);
        }

        categoriesRecyclerAdapter.setItems(user.getInterests());

    }

    private CategoriesDialog getCategoriesDialog() {
        CategoriesDialog categoriesDialog = new CategoriesDialog();

        categoriesDialog.setSelectionType(CategoriesDialog.MULTI_SELECT);

        categoriesDialog.setOnConfirmListener(new CategoriesDialog.OnConfirmChoiceListener() {
            @Override
            public void onConfirm(List<Category> categories) {
                viewModel.setCategories(categories);
            }
        });

        return categoriesDialog;
    }


    private void bindTextChangeListeners() {
        activityBinding.etFirstName.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setFirstName(s.toString());
            }
        });

        activityBinding.etLastName.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setLastName(s.toString());
            }
        });

        activityBinding.etDescribeYourself.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setUserDescription(s.toString());
            }
        });
    }


    private void registerViewModelObservers() {

        viewModel.getCurrentUser().observeForever(new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    bindUserToUi(user);
                }
            }
        });

        viewModel.getLoadCurrentUserTaskStatus().observeForever(new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                if (taskStatus != null) {
                    handleTaskStatus(taskStatus);
                }
            }
        });

        viewModel.getUpdateUserTaskStatus().observeForever(new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                switch (taskStatus) {
                    case IN_PROGRESS:
                        showProgressBar();
                        return;

                    case SUCCESS:
                        PrefsUtil.setUserDisplayName(viewModel.getFirstName().getValue() + " "
                                + viewModel.getLastName().getValue());
                        PrefsUtil.setUserProfilePicture(viewModel.getImageUrl().getValue());
                        hideProgressBar();
                    default:
                        hideProgressBar();
                }

            }
        });

    }

    private void handleTaskStatus(TaskStatus taskStatus) {
        switch (taskStatus) {
            case IN_PROGRESS:
                showProgressBar();
                return;
            default:
                hideProgressBar();
        }
    }

    private void showProgressBar() {
        activityBinding.progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.mainContainer.setAlpha(0.5f);
    }

    private void hideProgressBar() {
        activityBinding.progressBar.setVisibility(GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.mainContainer.setAlpha(1f);
    }

    private void showKeyboard(View view) {
        view.requestFocus();
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        View v = this.getCurrentFocus();
        mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
