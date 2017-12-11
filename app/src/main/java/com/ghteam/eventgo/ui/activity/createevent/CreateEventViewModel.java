package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.Category;
import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.data.model.Location;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.MapLiveData;
import com.ghteam.eventgo.util.network.FirebaseUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikit on 30.11.2017.
 */

public class CreateEventViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<String> mEventName;
    private MutableLiveData<String> mEventDescription;
    private MutableLiveData<String> mEventAddress;
    private MutableLiveData<LatLng> mEventLocation;
    private LiveDataList<String> mImageSources;
    private LiveDataList<Category> mCategories;

    private List<String> imageUrlsOnCloudStorage;

    private MutableLiveData<Boolean> mIsLoading;

    private MapLiveData<String, Boolean> mUploadingImages;

    private FirebaseUser mFirebaseUser;

    private static final String TAG = CreateEventViewModel.class.getSimpleName();

    CreateEventViewModel(Repository repository, final FirebaseUser user) {

        mRepository = repository;
        mFirebaseUser = user;

        mEventName = new MutableLiveData<>();
        mEventDescription = new MutableLiveData<>();
        mEventAddress = new MutableLiveData<>();
        mEventLocation = new MutableLiveData<>();
        mImageSources = new LiveDataList<>(new ArrayList<String>());
        mCategories = new LiveDataList<>();

        mIsLoading = new MutableLiveData<>();

        mUploadingImages = new MapLiveData<>(new HashMap<String, Boolean>());

        imageUrlsOnCloudStorage = new ArrayList<>();

        mImageSources.addInsertObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                uploadEventImage(Uri.parse(s), mFirebaseUser);
            }
        });
    }

    MutableLiveData<String> getEventName() {
        return mEventName;
    }

    MutableLiveData<String> getEventDescription() {
        return mEventDescription;
    }

    MutableLiveData<String> getEventAddress() {
        return mEventAddress;
    }

    LiveDataList<Category> getCategories() {
        return mCategories;
    }

    MutableLiveData<LatLng> getEventLocation() {
        return mEventLocation;
    }

    LiveDataList<String> getImageSources() {
        return mImageSources;
    }

    MapLiveData<String, Boolean> getUploadingImages() {
        return mUploadingImages;
    }

    void createEvent(OnSuccessListener<DocumentReference> successListener,
                     OnFailureListener failureListener) {
        mRepository.pushNewEvent(getEventEntry(), successListener
                , failureListener);
    }

    private Event getEventEntry() {
        Event event = new Event();
        event.setName(mEventName.getValue());
        event.setDescription(mEventDescription.getValue());
        event.setAddress(mEventAddress.getValue());
        event.setCategory(getCategories().getValue().get(0));
        event.setImages(imageUrlsOnCloudStorage);
        event.setOwnerId(mFirebaseUser.getUid());

        if (mEventLocation.getValue() != null) {
            event.setLocation(new Location(mEventLocation.getValue().latitude,
                    mEventLocation.getValue().longitude));
        }

        return event;
    }

    private void uploadEventImage(final Uri imageUri, FirebaseUser user) {
        final File imageFile = new File(imageUri.getPath());

        mUploadingImages.put(imageUri.toString(), true);

        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());

        StorageReference ref = FirebaseStorage.getInstance()
                .getReference(user.getUid() + "/images/my_events/" + imageFile.getName());

        FirebaseUtil.uploadPhoto(bitmap, ref, new FirebaseUtil.OnUploadResultListener() {
            @Override
            public void onSuccess(String url) {
                imageUrlsOnCloudStorage.add(url);
                mUploadingImages.put(imageUri.toString(), false);
            }

            @Override
            public void onFailed(Exception e) {
                mUploadingImages.put(imageUri.toString(), false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    static class CreateEventViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private FirebaseUser mUser;
        private Repository mRepository;

        public CreateEventViewModelFactory(Repository repository, FirebaseUser user) {
            mUser = user;
            mRepository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CreateEventViewModel(mRepository, mUser);
        }
    }
}