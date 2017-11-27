package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.databinding.ActivityProfileSettingsBinding;
import com.ghteam.eventgo.ui.activity.eventslist.EventsListActivity;
import com.ghteam.eventgo.ui.dialog.selectcategories.SelectCategoriesDialog;
import com.ghteam.eventgo.util.FacebookUserJsonConverter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class ProfileSettingsActivity extends AppCompatActivity {

    private SelectCategoriesDialog selectCategoriesDialog;
    private ProfileSettingsViewModel viewModel;
    private ActivityProfileSettingsBinding activityBinding;

    private InputMethodManager mInputMethodManager;

    private CategoryGridAdapter grindCategoryAdapter;

    public static final String TAG = ProfileSettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_profile_settings);

        ButterKnife.bind(this);

        grindCategoryAdapter = new CategoryGridAdapter();

        activityBinding.glSelectedCategories.setAdapter(grindCategoryAdapter);
        activityBinding.glSelectedCategories.setExpanded(true);

        selectCategoriesDialog = new SelectCategoriesDialog();
        selectCategoriesDialog.setOnConfirmListener(new SelectCategoriesDialog.OnConfirmChoiceListener() {
            @Override
            public void onConfirm(Set<Category> categories) {
                viewModel.setCategories(categories);
            }
        });

        viewModel = ViewModelProviders.of(this,
                new ProfileSettingsViewModel.ProfileSettingViewModelFactory(Repository.getInstance(this)))
                .get(ProfileSettingsViewModel.class);

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

//    private void loadFacebookUserInfo(AccessToken token) {
//        viewModel.getIsLoading().setValue(true);
//        Bundle params = new Bundle();
//        params.putString("fields", "first_name,last_name,picture");
//        new GraphRequest(
//                token,
//                "/" + token.getUserId(),
//                params,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        viewModel.getIsLoading().setValue(false);
//                        bindFacebookUserToViewModel(FacebookUserJsonConverter.getUser(response));
//                    }
//                }
//        ).executeAsync();
//    }

    private Observer<String> firstNameObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            activityBinding.tvFirstName.setText(s);
            activityBinding.etFirstName.setText(s);
        }
    };

    private Observer<String> lastNameObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            activityBinding.tvLastName.setText(s);
            activityBinding.etLastName.setText(s);
        }
    };

    private Observer<Set<Category>> categoriesObserver = new Observer<Set<Category>>() {
        @Override
        public void onChanged(@Nullable Set<Category> categoryEntries) {
            grindCategoryAdapter.setItems(new ArrayList<Category>(categoryEntries));
        }
    };

    Observer<ProfileSettingsViewModel.SaveUserResult> saveUserResultObserver = new Observer<ProfileSettingsViewModel.SaveUserResult>() {
        @Override
        public void onChanged(@Nullable ProfileSettingsViewModel.SaveUserResult saveUserResult) {
            if (saveUserResult == ProfileSettingsViewModel.SaveUserResult.RESULT_OK) {
                shortToast("Success");
                Intent intent = new Intent(ProfileSettingsActivity.this, EventsListActivity.class);
                startActivity(intent);

            } else if (saveUserResult == ProfileSettingsViewModel.SaveUserResult.RESULT_FAIL) {
                shortToast("Fail");
                viewModel.getSaveUserResult().setValue(ProfileSettingsViewModel.SaveUserResult.RESULT_NONE);
            }
        }
    };

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
            activityBinding.tvDescription.setText(s);
        }
    };

    private void registerViewModelObservers() {
        viewModel.getFirstName().observeForever(firstNameObserver);
        viewModel.getLastName().observeForever(lastNameObserver);
        viewModel.getCategories().observeForever(categoriesObserver);
        viewModel.getSaveUserResult().observeForever(saveUserResultObserver);
        viewModel.getIsLoading().observeForever(isLoadingObserver);
        viewModel.getUserDescription().observeForever(userDescriptionObserver);

    }

    private void removeViewModelObservers() {
        viewModel.getFirstName().removeObserver(firstNameObserver);
        viewModel.getLastName().removeObserver(lastNameObserver);
        viewModel.getCategories().removeObserver(categoriesObserver);
        viewModel.getSaveUserResult().removeObserver(saveUserResultObserver);
        viewModel.getIsLoading().removeObserver(isLoadingObserver);
        viewModel.getUserDescription().removeObserver(userDescriptionObserver);

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

    @OnClick(R.id.bt_add_interests)
    void addInterests() {
        selectCategoriesDialog.show(getSupportFragmentManager(), "TAG1");
    }

    @OnClick(R.id.bt_submit)
    void saveChanges() {
        viewModel.saveUserData();
    }

    @OnClick(R.id.iv_edit_description)
    void editDescription() {
        activityBinding.layoutDescription.setVisibility(GONE);
        activityBinding.layoutEditDescription.setVisibility(View.VISIBLE);

        showKeyboard(activityBinding.etDescribeYourself);
    }

    @OnClick(R.id.iv_save_description)
    void applyDescription() {
        viewModel.setUserDescription(activityBinding.etDescribeYourself.getText().toString());

        hideKeyboard();
        activityBinding.layoutDescription.setVisibility(View.VISIBLE);
        activityBinding.layoutEditDescription.setVisibility(GONE);

    }

    @OnClick(R.id.iv_edit_first_name)
    void editFirstName() {
        activityBinding.layoutFirstName.setVisibility(GONE);
        activityBinding.layoutEditFirstName.setVisibility(View.VISIBLE);

        showKeyboard(activityBinding.etFirstName);
    }

    @OnClick(R.id.iv_save_first_name)
    void saveFirstName() {
        viewModel.setFirstName(activityBinding.etFirstName.getText().toString());
        hideKeyboard();

        activityBinding.layoutEditFirstName.setVisibility(GONE);
        activityBinding.layoutFirstName.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_edit_last_name)
    void editLastName() {
        activityBinding.layoutLastName.setVisibility(GONE);
        activityBinding.layoutEditLastName.setVisibility(View.VISIBLE);
        showKeyboard(activityBinding.etLastName);
    }

    @OnClick(R.id.iv_save_last_name)
    void saveLastName() {
        viewModel.setLastName(activityBinding.etLastName.getText().toString());
        hideKeyboard();
        activityBinding.layoutEditLastName.setVisibility(GONE);
        activityBinding.layoutLastName.setVisibility(View.VISIBLE);
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
