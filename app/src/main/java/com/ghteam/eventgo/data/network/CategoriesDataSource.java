package com.ghteam.eventgo.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Category;
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

    private final Context mContext;

    private static CategoriesDataSource sInstance;

    private CategoriesDataSource(Context context) {
        mContext = context;

        mDownloadedCategories = new MutableLiveData<List<Category>>();
    }

    public LiveData<List<Category>> getCurrentCategories() {
        return mDownloadedCategories;
    }

    public static CategoriesDataSource getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CategoriesDataSource.class) {
                if (sInstance == null) {
                    sInstance = new CategoriesDataSource(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public static void fetchCategories() {
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
