package com.ghteam.eventgo.data;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.CallbackManager;
import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.task.FirestoreCollectionLoader;
import com.ghteam.eventgo.data.task.LoadCurrentUser;
import com.ghteam.eventgo.data.task.LoadUserById;
import com.ghteam.eventgo.data.task.LogInByEmailAndPassword;
import com.ghteam.eventgo.data.task.LogInWithFacebook;
import com.ghteam.eventgo.data.task.PostEvent;
import com.ghteam.eventgo.data.task.TaskResultListener;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.data.task.TaskStatusListener;
import com.ghteam.eventgo.data.task.UpdateUser;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.network.FirestoreUtil;

import java.util.List;

import io.realm.Realm;

/**
 * Created by nikit on 04.01.2018.
 */

public class Repository {


    private static final String TAG = Repository.class.getSimpleName();

    private static Repository sInstance;

    private final AppExecutors appExecutors;


    private Repository(Context context, AppExecutors executors) {

        this.appExecutors = executors;

        events = new MutableLiveData<>();
        loadEventsTaskStatus = new MutableLiveData<>();

        categories = new MutableLiveData<>();
        loadCategoriesTaskStatus = new MutableLiveData<>();

        users = new MutableLiveData<>();
        loadUsersTaskStatus = new MutableLiveData<>();

        registerDataObserves();
    }

    public static Repository getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(context, executors);
                }
            }
        }
        return sInstance;
    }

    private static int repositoryRequestCount = 0;

    /*
     * Loading Events
     */

    private MutableLiveData<List<Event>> events;
    private MutableLiveData<TaskStatus> loadEventsTaskStatus;

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
                .load(limit);
    }

    private FirestoreCollectionLoader<Event> eventsLoader;

    public void loadNextEvents(int countLimit) {

        if (eventsLoader == null) {
            eventsLoader = new FirestoreCollectionLoader<>(FirestoreUtil.getReferenceToEvents(), Event.class);
            eventsLoader.addTaskResultListener(new TaskResultListener<List<Event>>() {
                @Override
                public void onResult(List<Event> result) {
                    events.setValue(result);
                }
            });

            eventsLoader.addTaskStatusListener(new TaskStatusListener() {
                @Override
                public void onStatusChanged(TaskStatus status) {
                    loadEventsTaskStatus.setValue(status);
                }
            });
        }
        eventsLoader.loadNext(countLimit);
    }

    public MutableLiveData<TaskStatus> getLoadEventsTaskStatus() {
        return loadEventsTaskStatus;
    }

    public MutableLiveData<List<Event>> initializeEvents() {
        return events;
    }

    /*
     * Loading eventInLocalDb categories
     */

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<TaskStatus> loadCategoriesTaskStatus;

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
                .load(null);
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

    /*
     *  Loading users
     */

    private MutableLiveData<List<User>> users;
    private MutableLiveData<TaskStatus> loadUsersTaskStatus;

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
                .load(limit);
    }

    public MutableLiveData<List<User>> initializeUsers() {
        return users;
    }

    public MutableLiveData<TaskStatus> getLoadUsersTaskStatus() {
        return loadUsersTaskStatus;
    }

    private MutableLiveData<TaskStatus> updateUserTaskStatus;

    /*
     * Update or insert new User data in remote DB
     */

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

    /*
     * Loading current user from remote DB
     */

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

    /*
     * Log ing with email and password
     */

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

    /*
     * Log in with Facebook
     */

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

    /*
     * Loading user by id
     */

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<TaskStatus> loadUserByIdTaskStatus = new MutableLiveData<>();

    public MutableLiveData<User> initializeUser() {
        return user;
    }

    public MutableLiveData<TaskStatus> getLoadUserTaskStatus() {
        return loadUserByIdTaskStatus;
    }

    public void loadUserById(String id) {
        new LoadUserById()
                .addTaskResultListener(new TaskResultListener<User>() {
                    @Override
                    public void onResult(User result) {
                        user.setValue(result);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        loadUserByIdTaskStatus.setValue(status);
                    }
                })
                .execute(id);
    }


    /*
     * Post eventInLocalDb to remote DB
     */


    private MutableLiveData<String> postedEventId;
    private MutableLiveData<TaskStatus> postEventTaskStatus = new MutableLiveData<>();

    public MutableLiveData<String> initializePostedEventId() {
        postedEventId = new MutableLiveData<>();
        return postedEventId;
    }

    public MutableLiveData<TaskStatus> getPostEventTaskStatus() {
        return postEventTaskStatus;
    }

    public void postNewEvent(Event event) {
        new PostEvent(event)
                .addTaskResultListener(new TaskResultListener<String>() {
                    @Override
                    public void onResult(String result) {
                        postedEventId.setValue(result);
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        postEventTaskStatus.setValue(status);
                    }
                })
                .execute();
    }


    private void registerDataObserves() {
        final Realm realm = Realm.getDefaultInstance();

        events.observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                Log.d(TAG, "onChanged: " + events.size());

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(events);
                    }
                });
            }
        });
    }

    /*
     * Getting event from local DB
     */


    public Event getEventFromLocalDb(String id) {
        Realm realm = Realm.getDefaultInstance();

        Event event = realm.where(Event.class)
                .equalTo("id", id)
                .findFirst();
        return event;
    }

}
