package com.ghteam.eventgo.data_new.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ghteam.eventgo.data_new.entity.Category;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class CategoriesDataSource {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final String FIRESTORE_EVENT_CATEGORIES = "event_categories";
    public static final String TAG = CategoriesDataSource.class.getSimpleName();

    private static MutableLiveData<List<Category>> mDownloadedCategories;

    private static CategoriesDataSource sInstance;

    private CategoriesDataSource() {
        mDownloadedCategories = new MutableLiveData<List<Category>>();
    }

    public LiveData<List<Category>> getCurrentCategories() {
        return mDownloadedCategories;
    }

    public static CategoriesDataSource getInstance() {
        if (sInstance == null) {
            synchronized (CategoriesDataSource.class) {
                if (sInstance == null) {
                    sInstance = new CategoriesDataSource();
                }
            }
        }
        return sInstance;
    }

    public static void loadCategories() {
        firestore.collection(FIRESTORE_EVENT_CATEGORIES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots != null && documentSnapshots.size() > 0) {
                    Log.d(TAG, "onEvent documents count: " + documentSnapshots.size());
                    List<Category> categoryItems = documentSnapshots.toObjects(Category.class);

                    Log.d(TAG, "onEvent categories: " + categoryItems.toString());

                    mDownloadedCategories.setValue(categoryItems);
                }
            }
        });
    }
}
