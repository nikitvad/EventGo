package com.ghteam.eventgo.ui.activity.createevent;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.databinding.ActivityCreateEventBinding;
import com.ghteam.eventgo.util.CameraUtil;
import com.ghteam.eventgo.util.PermissionUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class CreateEventActivity extends AppCompatActivity {


    private ActivityCreateEventBinding activityBinding;
    private CameraUtil mCameraUtil;
    private ImageRecyclerAdapter adapter;

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
                .CreateEventViewModelFactory(Repository.getInstance(this), mAuth.getCurrentUser()))
                .get(CreateEventViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        activityBinding.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
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

        activityBinding.btPublishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.createEvent();
                Log.d(TAG, "onClick: " + viewModel.getEventName().getValue());
            }
        });

        mCameraUtil = new CameraUtil(this);
        mCameraUtil.setRequestImageCapture(REQUEST_CAMERA);

        adapter = new ImageRecyclerAdapter();
        adapter.setAddItemClickListener(new ImageRecyclerAdapter.OnAddItemClickListener() {
            @Override
            public void onClick() {
//                mCameraUtil.takePhoto();
                selectImage();
            }
        });

        activityBinding.rvPhotos.setAdapter(adapter);
        activityBinding.rvPhotos.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        registerViewModelObservers();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                viewModel.getEventLocation().setValue(place.getLatLng());
                viewModel.getEventAddress().setValue(place.getAddress().toString());

                Toast.makeText(this, place.getAddress(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    private void registerViewModelObservers() {

        viewModel.getImageSources().addInsertObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                Log.d(TAG, "onChanged: " + s);
                if (s != null) {
                    adapter.addItem(s);
                }
            }
        });

        viewModel.getUploadingImages().addPutObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChanged: " + s + " " + viewModel.getUploadingImages().get(s));
                adapter.setIsLoadingForItem(viewModel.getUploadingImages().get(s), s);
            }
        });

        viewModel.getEventAddress().observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                activityBinding.tvEventAddress.setText(s);
            }
        });
    }

    public void removeViewModelObservers() {
        viewModel.getImageSources().deleteInsertObservers();
        viewModel.getUploadingImages().deletePutObservers();
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

}
