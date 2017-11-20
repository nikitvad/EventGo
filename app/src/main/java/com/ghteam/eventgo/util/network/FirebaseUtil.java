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

        ref.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (listener != null) {
                            listener.onSuccess(taskSnapshot.getDownloadUrl().toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onFailed(e);
                        }
                    }
                });

    }

    public interface OnUploadResultListener {
        void onSuccess(String url);

        void onFailed(Exception e);
    }

}
