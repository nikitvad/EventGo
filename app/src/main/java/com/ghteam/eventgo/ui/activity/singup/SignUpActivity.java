package com.ghteam.eventgo.ui.activity.singup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.network.FirebaseAccountManager;
import com.ghteam.eventgo.databinding.ActivitySignUpBinding;
import com.ghteam.eventgo.util.network.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Validator validator;

    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        ActivitySignUpBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        dataBinding.setViewModel(viewModel);
        ButterKnife.bind(this);

        setSupportActionBar(dataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        validator = new Validator(dataBinding);
        validator.enableFormValidationMode();
        validator.validate();
    }


    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        if (validator.validate()) {
            Log.d(TAG, "onSubmitClick: " + viewModel.toString());

            FirebaseAccountManager.getInstance()
                    .createNewAccount(viewModel.getEmail(),
                            viewModel.getPassword(), viewModel.getUserData());

        }

    }

    @OnClick(R.id.iv_profile_photo)
    void getPhoto() {
        selectImageDialog();
    }

    private void selectImageDialog() {
        List<String> dialogOptions = Arrays.asList("Take Photo", "Choose from library", "Cancel");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add photo");
        builder.setItems((String[]) dialogOptions.toArray(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    dispatchTakePictureIntent();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (photoPath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                Log.d(TAG, "onActivityResult: photoSize:" + bitmap.getByteCount());
                FirebaseUtil.uploadPhoto(bitmap, null, new FirebaseUtil.OnUploadResultListener() {
                    @Override
                    public void onSuccess(String url) {
                        Log.d(TAG, "onSuccess: PhotoURL:" + url);
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }
        }
    }

    private String photoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ghteam.eventgo",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


}


