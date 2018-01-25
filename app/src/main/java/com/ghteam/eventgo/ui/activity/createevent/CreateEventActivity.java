package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data_new.entity.Category;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.databinding.ActivityCreateEventBinding;
import com.ghteam.eventgo.ui.dialog.datetimepicker.DateAndTimePicker;
import com.ghteam.eventgo.ui.dialog.selectcategories.CategoriesDialog;
import com.ghteam.eventgo.util.CameraUtil;
import com.ghteam.eventgo.util.CustomTextWatcher;
import com.ghteam.eventgo.util.InjectorUtil;
import com.ghteam.eventgo.util.PermissionUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends LifecycleActivity {


    private ActivityCreateEventBinding activityBinding;
    private CameraUtil mCameraUtil;
    private ImageRecyclerAdapter imageAdapter;
    private CategoriesDialog categoriesDialog;

    private DateAndTimePicker dateAndTimePicker;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private CreateEventViewModel viewModel;

    public static final int REQUEST_CAMERA = 1000;

    public static final int PLACE_PICKER_REQUEST_CODE = 1001;

    private static final int REQUEST_GALLERY = 1002;

    public static final String TAG = CreateEventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_event);

        viewModel = ViewModelProviders.of(this, new CreateEventViewModel
                .CreateEventViewModelFactory(InjectorUtil.provideRepository(this), mAuth.getCurrentUser()))
                .get(CreateEventViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        bindOnClickListeners();
        bindTextChangedListeners();

        categoriesDialog = getCategoriesDialog();
        dateAndTimePicker = getDateAndTimePicker();

        mCameraUtil = new CameraUtil(this);
        mCameraUtil.setRequestImageCapture(REQUEST_CAMERA);

        imageAdapter = new ImageRecyclerAdapter();
        imageAdapter.setAddItemClickListener(new ImageRecyclerAdapter.OnAddItemClickListener() {
            @Override
            public void onClick() {
                selectImage();
            }
        });

        activityBinding.btSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesDialog.show(getSupportFragmentManager(), TAG);
            }
        });

        activityBinding.rvPhotos.setAdapter(imageAdapter);
        activityBinding.rvPhotos.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        registerViewModelObservers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                viewModel.getEventLocation().setValue(place.getLatLng());
                viewModel.getEventAddress().setValue(place.getAddress().toString());
            }
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (mCameraUtil.getPhotoPath() != null) {

                Uri photoUri = Uri.fromFile(new File(mCameraUtil.getPhotoPath()));
                viewModel.getImageSources().addItem(photoUri.toString());
            }
        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imagePath = cursor.getString(columnIndex);

                    Uri photoUri = Uri.fromFile(new File(imagePath));
                    viewModel.getImageSources().addItem(photoUri.toString());
                }
                cursor.close();


            } catch (Exception e) {
                Log.w(TAG, "onActivityResult: ", e);
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    private void bindTextChangedListeners() {

        activityBinding.etEventName.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEventName(s.toString());
            }
        });

        activityBinding.etEventDescription.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEventDescription(s.toString());
            }
        });
    }

    private void bindOnClickListeners() {

        activityBinding.btSetAddressOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(CreateEventActivity.this), PLACE_PICKER_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        activityBinding.tvEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateAndTimePicker.show(getFragmentManager(), TAG);
            }
        });

        activityBinding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.postEvent();
            }
        });
    }

    private DateAndTimePicker getDateAndTimePicker() {

        DateAndTimePicker dateAndTimePicker = new DateAndTimePicker();
        dateAndTimePicker.setOnOkClickListener(new DateAndTimePicker.OnOkClickListener() {
            @Override
            public void onOkClick(Date date) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd EEEE hh:mm", Locale.ENGLISH);
                Log.d(TAG, "onOkClick: " + simpleDateFormat.format(date));
                activityBinding.tvEventDate.setText(simpleDateFormat.format(date));
                viewModel.setDate(date);

            }
        });

        return dateAndTimePicker;
    }

    private CategoriesDialog getCategoriesDialog() {
        CategoriesDialog dialog = new CategoriesDialog();
        dialog.setSelectionType(CategoriesDialog.SINGLE_SELECT);

        dialog.setOnConfirmListener(new CategoriesDialog.OnConfirmChoiceListener() {
            @Override
            public void onConfirm(List<Category> categories) {
                viewModel.getCategories().setValue(categories);
            }
        });
        return dialog;
    }

    private void registerViewModelObservers() {

        viewModel.getCategories().observeForever(new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories.size() > 0) {
                    activityBinding.btSelectCategory.setText(categories.get(0).getName());
                }
            }
        });

        viewModel.getImageSources().addInsertObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onStatusChanged: " + s);
                if (s != null) {
                    imageAdapter.addItem(s);
                }
            }
        });

        viewModel.getUploadingImages().addInsertObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onStatusChanged: " + s + " " + viewModel.getUploadingImages().get(s));
                imageAdapter.setIsLoadingForItem(viewModel.getUploadingImages().get(s), s);
            }
        });

        viewModel.getEventAddress().observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                activityBinding.tvEventAddress.setText(s);
            }
        });
        viewModel.getPostEventTaskStatus().observe(this, new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                if (taskStatus == TaskStatus.IN_PROGRESS) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean isPermissionGranted = PermissionUtil.checkPermission(CreateEventActivity.this);
                if (items[item].equals("Take Photo")) {
                    if (isPermissionGranted)
                        mCameraUtil.takePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    if (isPermissionGranted) {
                        loadImageFromGallery();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void loadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }


    private void showProgressBar() {
        activityBinding.progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.mainContainer.setAlpha(0.5f);
    }


    private void hideProgressBar() {
        activityBinding.progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.mainContainer.setAlpha(1f);
    }

}
