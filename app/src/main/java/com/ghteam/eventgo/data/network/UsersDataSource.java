package com.ghteam.eventgo.data.network;

import android.util.Log;

import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by nikit on 11.12.2017.
 */

public class UsersDataSource {
    public static final String REF_USERS = "users";
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private static UsersDataSource sInstance;

    private LiveDataList<User> mDownloadedUsers;

    public static final String TAG = UsersDataSource.class.getSimpleName();

    private UsersDataSource() {
        mDownloadedUsers = new LiveDataList<>();
    }

    public static UsersDataSource getInstance() {
        if (sInstance == null) {
            synchronized (UsersDataSource.class) {
                if (sInstance == null) {
                    sInstance = new UsersDataSource();
                }
            }
        }
        return sInstance;
    }

    public LiveDataList<User> getCurrentUsers() {
        return mDownloadedUsers;
    }


    public void loadUsers(final OnTaskStatusChangeListener listener) {

        listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS);

        firestore.collection(REF_USERS).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    if (documentSnapshots != null && documentSnapshots.size() > 0) {
                        mDownloadedUsers.setValue(documentSnapshots.toObjects(User.class));
                    }

                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);

                } else {
                    Log.w(TAG, "onEvent: ", e);
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.FAILED);
                }
            }
        });
    }
}
