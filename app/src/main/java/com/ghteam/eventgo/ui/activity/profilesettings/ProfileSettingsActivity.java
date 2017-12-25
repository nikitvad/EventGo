package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.databinding.ActivityProfileSettingsV2Binding;
import com.ghteam.eventgo.ui.activity.eventslist.EventsActivity;
import com.ghteam.eventgo.ui.adapter.SelectedCategoriesRecyclerAdapter;
import com.ghteam.eventgo.ui.dialog.selectcategories.CategoriesDialog;
import com.ghteam.eventgo.util.CustomTextWatcher;
import com.ghteam.eventgo.util.InjectorUtil;
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
                new ProfileSettingsViewModel.ProfileSettingViewModelFactory(InjectorUtil.provideRepository(this)))
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
                viewModel.saveUserData();
            }
        });

        activityBinding.glSelectedCategories.setAdapter(categoriesRecyclerAdapter);
        activityBinding.glSelectedCategories.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));


        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        registerViewModelObservers();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerViewModelObservers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeViewModelObservers();
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

    private Observer<String> firstNameObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            activityBinding.etFirstName.setText(s);
        }
    };

    private Observer<String> lastNameObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            activityBinding.etLastName.setText(s);
        }
    };

    private Observer<String> profileImageObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Log.d(TAG, "onStatusChanged: " + s);
            if (s != null && !s.isEmpty()) {
                Picasso.with(ProfileSettingsActivity.this).load(s)
                        .into(activityBinding.ivProfilePhoto);
            } else {
                activityBinding.ivProfilePhoto.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
            }
        }
    };

    private Observer<List<Category>> categoriesObserver = new Observer<List<Category>>() {
        @Override
        public void onChanged(@Nullable List<Category> categoryEntries) {
            categoriesRecyclerAdapter.setItems(categoryEntries);
        }
    };

    private Observer<Category> addCategoryObserver = new Observer<Category>() {
        @Override
        public void onChanged(@Nullable Category category) {
            categoriesRecyclerAdapter.addItem(category);
        }
    };

    private Observer<Category> removeCategoryObserver = new Observer<Category>() {
        @Override
        public void onChanged(@Nullable Category category) {
            categoriesRecyclerAdapter.removeItem(category);
        }
    };

    private Observer<ProfileSettingsViewModel.SaveUserResult> saveUserResultObserver = new Observer<ProfileSettingsViewModel.SaveUserResult>() {
        @Override
        public void onChanged(@Nullable ProfileSettingsViewModel.SaveUserResult saveUserResult) {
            if (saveUserResult == ProfileSettingsViewModel.SaveUserResult.RESULT_OK) {
                shortToast("Success");
                Intent intent = new Intent(ProfileSettingsActivity.this, EventsActivity.class);
                startActivity(intent);

            } else if (saveUserResult == ProfileSettingsViewModel.SaveUserResult.RESULT_FAIL) {
                shortToast("Fail");
                viewModel.getSaveUserResult().setValue(ProfileSettingsViewModel.SaveUserResult.RESULT_NONE);
            }
        }
    };

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

    private Observer<Boolean> isLoadingObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            if (aBoolean) {
                showProgressBar();
            } else {
                hideProgressBar();
            }
        }
    };

    private Observer<String> userDescriptionObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            activityBinding.etDescribeYourself.setText(s);
        }
    };

    private void registerViewModelObservers() {
        viewModel.getFirstName().observeForever(firstNameObserver);
        viewModel.getLastName().observeForever(lastNameObserver);
        viewModel.getCategories().observeForever(categoriesObserver);
        viewModel.getSaveUserResult().observeForever(saveUserResultObserver);
        viewModel.getIsLoading().observeForever(isLoadingObserver);
        viewModel.getUserDescription().observeForever(userDescriptionObserver);
        viewModel.getImageUrl().observeForever(profileImageObserver);
        viewModel.getCategories().addInsertObserver(addCategoryObserver);
        viewModel.getCategories().addRemoveObserver(removeCategoryObserver);

    }

    private void removeViewModelObservers() {
        viewModel.getFirstName().removeObserver(firstNameObserver);
        viewModel.getLastName().removeObserver(lastNameObserver);
        viewModel.getCategories().removeObserver(categoriesObserver);
        viewModel.getSaveUserResult().removeObserver(saveUserResultObserver);
        viewModel.getIsLoading().removeObserver(isLoadingObserver);
        viewModel.getUserDescription().removeObserver(userDescriptionObserver);
        viewModel.getImageUrl().removeObserver(profileImageObserver);
        viewModel.getCategories().deleteRemoveObservers();
        viewModel.getCategories().deleteInsertObservers();

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

    private void shortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
