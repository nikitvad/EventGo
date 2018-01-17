package com.ghteam.eventgo.ui.activity.singup;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data_new.network.FirebaseAccountManager;
import com.ghteam.eventgo.databinding.ActivitySignUpBinding;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.util.network.FirebaseUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private ActivitySignUpBinding mDataBinding;
    Calendar dateAndTime = Calendar.getInstance();

    private SignUpViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mDataBinding.setViewModel(mViewModel);

        ButterKnife.bind(this);

        setSupportActionBar(mDataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        Log.d(TAG, "onSubmitClick: " + mViewModel.toString());

        showProgressBar();

        FirebaseAccountManager.createNewAccount(mViewModel.getEmail(),
                mViewModel.getPassword(), mViewModel.getUserData(),
                new FirebaseAccountManager.OnResultListener() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                        startActivity(ProfileSettingsActivity.class);
                    }

                    @Override
                    public void onFailed() {
                        mDataBinding.mainContainer.setAlpha(1.0f);
                        mDataBinding.mainContainer.setClickable(true);

                        Toast.makeText(SignUpActivity.this, "Fail",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProgressBar() {
        mDataBinding.progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDataBinding.mainContainer.setAlpha(0.5f);
    }

    private void hideProgressBar() {
        mDataBinding.progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDataBinding.mainContainer.setAlpha(1f);
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

    @OnClick(R.id.tv_birthday)
    void selectBirthDay() {
        setDate();
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


    public void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                android.R.style.Theme_Holo_Light_Dialog, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setInitialDateTime() {
        mDataBinding.tvBirthday.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

            mViewModel.getUserData().setBirthday(dateAndTime.getTime());

        }
    };


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


    private void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}


