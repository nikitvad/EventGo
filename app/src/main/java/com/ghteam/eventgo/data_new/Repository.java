package com.ghteam.eventgo.data_new;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.ghteam.eventgo.data_new.entity.Category;
import com.ghteam.eventgo.data_new.entity.Event;
import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.data_new.task.FirestoreCollectionLoader;
import com.ghteam.eventgo.data_new.task.LoadCurrentUser;
import com.ghteam.eventgo.data_new.task.LogInByEmailAndPassword;
import com.ghteam.eventgo.data_new.task.LogInWithFacebook;
import com.ghteam.eventgo.data_new.task.TaskResultListener;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.data_new.task.TaskStatusListener;
import com.ghteam.eventgo.data_new.task.UpdateUser;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.network.FirestoreUtil;

import java.util.List;

/**
 * Created by nikit on 04.01.2018.
 */

public class Repository {


    private static final String TAG = Repository.class.getSimpleName();

    private static Repository sInstance;


    private MutableLiveData<List<Event>> events;
    private MutableLiveData<TaskStatus> loadEventsTaskStatus;

    private MutableLiveData<List<User>> users;
    private MutableLiveData<TaskStatus> loadUsersTaskStatus;


    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<TaskStatus> loadCategoriesTaskStatus;

    private Repository() {

        events = new MutableLiveData<>();
        loadEventsTaskStatus = new MutableLiveData<>();

        categories = new MutableLiveData<>();
        loadCategoriesTaskStatus = new MutableLiveData<>();

        users = new MutableLiveData<>();
        loadUsersTaskStatus = new MutableLiveData<>();
    }

    public static Repository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository();
                }
            }
        }
        return sInstance;
    }

    private static int repositoryRequestCount = 0;

    public void loadEvents(int limit) {

        Log.d(TAG, "loadEvents: " + repositoryRequestCount);
        new FirestoreCollectionLoader<Event>(FirestoreUtil.getReferenceToEvents(), Event.class)
                .addTaskResultListener(new TaskResultListener<List<Event>>() {
                    @Nullable
                    @Override
                    public void onResult(List<Event> result) {
                        events.setValue(result);
                        Log.d(TAG, "onResult: " + result.size());
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        Log.d(TAG, "onStatusChanged: " + status.toString());
                        loadEventsTaskStatus.setValue(status);
                    }
                })
                .execute(limit);
    }

    public MutableLiveData<TaskStatus> getLoadEventsTaskStatus() {
        return loadEventsTaskStatus;
    }

    public MutableLiveData<List<Event>> initializeEvents() {
        return events;
    }


    private void loadCategories() {

        new FirestoreCollectionLoader<Category>(FirestoreUtil.getReferenceToCategories(), Category.class)
                .addTaskResultListener(new TaskResultListener<List<Category>>() {
                    @Nullable
                    @Override
                    public void onResult(List<Category> result) {
                        categories.setValue(result);
                        Log.d(TAG, "onResult: " + result.toString());
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        loadCategoriesTaskStatus.setValue(status);
                        Log.d(TAG, "onStatusChanged: " + status);
                    }
                })
                .execute();
    }

    public MutableLiveData<List<Category>> initializeCategories() {
        if ((categories.getValue() == null || categories.getValue().size() < 1)
                && loadCategoriesTaskStatus.getValue() != TaskStatus.IN_PROGRESS) {
            loadCategories();
        }

        return categories;
    }

    public MutableLiveData<TaskStatus> getLoadCategoriesTaskStatus() {
        return loadCategoriesTaskStatus;
    }


    public void loadUsers(Integer limit) {
        new FirestoreCollectionLoader<User>(FirestoreUtil.getReferenceToUsers(), User.class)
                .addTaskResultListener(new TaskResultListener<List<User>>() {
                    @Nullable
                    @Override
                    public void onResult(List<User> result) {
                        users.setValue(result);
                        Log.d(TAG, "onResult: " + result);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        loadUsersTaskStatus.setValue(status);
                        Log.d(TAG, "onStatusChanged: " + status);
                    }
                })
                .execute(limit);
    }

    public MutableLiveData<List<User>> initializeUsers() {
        return users;
    }

    public MutableLiveData<TaskStatus> getLoadUsersTaskStatus() {
        return loadUsersTaskStatus;
    }

    private MutableLiveData<TaskStatus> updateUserTaskStatus;

    public void updateUser(User user, String uid) {
        if (user.getId().isEmpty() || !user.getId().equals(uid)) {
            user.setId(uid);
        }

        new UpdateUser(user, uid)
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        updateUserTaskStatus.setValue(status);
                    }
                }).execute();
    }

    public MutableLiveData<TaskStatus> getUpdateUserTaskStatus() {
        if (updateUserTaskStatus == null) {
            updateUserTaskStatus = new MutableLiveData<>();
        }
        return updateUserTaskStatus;
    }

    private MutableLiveData<User> currentUser;
    private MutableLiveData<TaskStatus> loadCurrentUserTaskStatus = new MutableLiveData<>();

    public void loadCurrentUser() {
        new LoadCurrentUser()
                .addTaskResultListener(new TaskResultListener<User>() {
                    @Nullable
                    @Override
                    public void onResult(User result) {
                        currentUser.setValue(result);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        loadCurrentUserTaskStatus.setValue(status);
                    }
                }).execute();
    }

    public MutableLiveData<User> initializeCurrentUser() {
        currentUser = new MutableLiveData<>();
        return currentUser;
    }

    public MutableLiveData<TaskStatus> getLoadCurrentUserTaskStatus() {
        loadCurrentUserTaskStatus = new MutableLiveData<>();
        return loadCurrentUserTaskStatus;
    }


    private MutableLiveData<String> userId = new MutableLiveData<>();
    private MutableLiveData<TaskStatus> logInTaskStatus = new MutableLiveData<>();

    public void loginWithEmail(String email, String password) {
        userId.setValue("");
        new LogInByEmailAndPassword(email, password)
                .addTaskResultListener(new TaskResultListener<String>() {
                    @Override
                    public void onResult(String result) {
                        userId.setValue(result);
                        PrefsUtil.setLoggedType(PrefsUtil.LOGGED_TYPE_EMAIL);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        logInTaskStatus.setValue(status);
                    }
                })
                .execute();
    }

    public MutableLiveData<String> getCurrentUserId() {
        return userId;
    }

    public MutableLiveData<TaskStatus> getLogInTaskStatus() {
        logInTaskStatus = new MutableLiveData<>();
        return logInTaskStatus;
    }

    public void logInWithFacebook(Activity activity, CallbackManager callbackManager) {
        new LogInWithFacebook(activity, callbackManager)
                .addTaskResultListener(new TaskResultListener<String>() {
                    @Override
                    public void onResult(String result) {
                        userId.setValue(result);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        logInTaskStatus.setValue(status);
                    }
                })
                .execute();
    }


}
