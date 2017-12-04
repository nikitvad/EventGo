package com.ghteam.eventgo.util.network;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseUtil {

    private static StorageReference defReference = FirebaseStorage.getInstance()
            .getReference("images");

    public static void uploadPhoto(Bitmap photo, @Nullable StorageReference storageReference,
                                   final OnUploadResultListener listener) {
        StorageReference ref;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (storageReference == null) {
            ref = defReference;
        } else {
            ref = storageReference;
        }

        if (listener != null) {
            ref.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            listener.onSuccess(taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onFailed(e);
                        }
                    });
        } else {
            ref.putBytes(baos.toByteArray());
        }
    }

    public interface OnUploadResultListener {
        void onSuccess(String url);

        void onFailed(Exception e);
    }

}
