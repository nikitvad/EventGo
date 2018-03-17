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
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.MapLiveData;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.network.FirebaseUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikit on 30.11.2017.
 */

public class CreateEventViewModel extends ViewModel {
    private Repository mRepository;

    private String mEventName;
    private String mEventDescription;
    private MutableLiveData<String> mEventAddress;
    private MutableLiveData<LatLng> mEventLocation;
    private LiveDataList<String> mImageSources;
    private MutableLiveData<Category> selectedCategory;
//    private Date mDate;

    private MutableLiveData<Calendar> eventDate;
    private MutableLiveData<Calendar> eventTime;

    private MutableLiveData<List<Category>> availableCategories;

    private MutableLiveData<String> postedEventId;
    private MutableLiveData<TaskStatus> postEventTaskStatus;

    private List<String> imageUrlsOnCloudStorage;

    private MapLiveData<String, Boolean> mUploadingImages;

    private FirebaseUser mFirebaseUser;

    private static final String TAG = CreateEventViewModel.class.getSimpleName();

    CreateEventViewModel(Repository repository, final FirebaseUser user) {

        mRepository = repository;
        mFirebaseUser = user;

        mEventAddress = new MutableLiveData<>();
        mEventLocation = new MutableLiveData<>();
        mImageSources = new LiveDataList<>(new ArrayList<String>());
        selectedCategory = new MutableLiveData<>();

        eventDate = new MutableLiveData<>();
        eventTime = new MutableLiveData<>();

        postedEventId = mRepository.initializePostedEventId();
        postEventTaskStatus = mRepository.getPostEventTaskStatus();

        availableCategories = mRepository.initializeCategories();

        mUploadingImages = new MapLiveData<>(new HashMap<String, Boolean>());

        imageUrlsOnCloudStorage = new ArrayList<>();

        mImageSources.addInsertObserver(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                uploadEventImage(Uri.parse(s), mFirebaseUser);
            }
        });
    }

    String getEventName() {
        return mEventName;
    }

    void setEventName(String name) {
        mEventName = name;
    }

    public MutableLiveData<Calendar> getEventDate() {
        return eventDate;
    }

    public MutableLiveData<Calendar> getEventTime() {
        return eventTime;
    }

    String getEventDescription() {
        return mEventDescription;
    }

    void setEventDescription(String description) {
        mEventDescription = description;
    }

    MutableLiveData<String> getEventAddress() {
        return mEventAddress;
    }

    void setEventAddress(String address) {
        mEventAddress.setValue(address);
    }

    MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
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

    void createEvent(OnSuccessListener<Void> successListener,
                     OnFailureListener failureListener) {

        mRepository.postNewEvent(getEventEntry());
    }

    public MutableLiveData<List<Category>> getAvailableCategories() {
        return availableCategories;
    }

    private Event getEventEntry() {
        Event event = new Event();
        event.setName(mEventName);
        event.setDescription(mEventDescription);

        event.setAddress(mEventAddress.getValue());
        event.setCategory(getSelectedCategory().getValue());

        event.setImages(imageUrlsOnCloudStorage);

        event.setOwnerId(mFirebaseUser.getUid());
        event.setOwnerName(PrefsUtil.getUserDisplayName());
        event.setOwnerProfilePicture(PrefsUtil.getUserProfilePicture());

        Calendar calendarFullDateOfEvent = Calendar.getInstance();

        Calendar calendarDateOfEvent = eventDate.getValue();
        Calendar calendarTimeOfEvent = eventTime.getValue();

        calendarFullDateOfEvent.set(calendarDateOfEvent.get(Calendar.YEAR),
                calendarDateOfEvent.get(Calendar.MONTH),
                calendarDateOfEvent.get(Calendar.DATE),
                calendarTimeOfEvent.get(Calendar.HOUR_OF_DAY),
                calendarTimeOfEvent.get(Calendar.MINUTE));

        event.setDate(calendarFullDateOfEvent.getTime());

        if (mEventLocation.getValue() != null) {
            event.setAppLocation(new AppLocation(mEventLocation.getValue().latitude,
                    mEventLocation.getValue().longitude));
        }

        return event;
    }

    public void postEvent() {
        mRepository.postNewEvent(getEventEntry());
    }

    public MutableLiveData<TaskStatus> getPostEventTaskStatus() {
        return postEventTaskStatus;
    }

    public MutableLiveData<String> getPostedEventId() {
        return postedEventId;
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

    public static class CreateEventViewModelFactory extends ViewModelProvider.NewInstanceFactory {
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
