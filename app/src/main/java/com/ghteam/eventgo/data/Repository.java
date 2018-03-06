package com.ghteam.eventgo.data;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.CallbackManager;
import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.network.NetworkEventManager;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.realm.Realm;

/**
 * Created by nikit on 04.01.2018.
 */

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();

    private static Repository sInstance;

    private final AppExecutors appExecutors;

    private NetworkEventManager networkEventManager;

    private FirebaseAuth firebaseAuth;


    private Repository(Context context, AppExecutors executors) {

        this.appExecutors = executors;

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            networkEventManager = new NetworkEventManager(firebaseAuth.getCurrentUser().getUid());
        }

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged: ");
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d(TAG, "onAuthStateChanged: ");
                    networkEventManager = new NetworkEventManager(firebaseAuth.getCurrentUser().getUid());

                    usersInterestedEventsLoader = new FirestoreCollectionLoader<Event>(
                            FirestoreUtil.getReferenceToUserInterestedEvents(firebaseAuth.getCurrentUser().getUid()),
                            Event.class);

                    usersInterestedEventsLoader.addTaskResultListener(new TaskResultListener<List<Event>>() {
                        @Override
                        public void onResult(List<Event> result) {
                            Log.d(TAG, "onResult: " + result);
                            usersInterestedEvents.setValue(result);
                        }
                    });

                    usersInterestedEventsLoader.addTaskStatusListener(new TaskStatusListener() {
                        @Override
                        public void onStatusChanged(TaskStatus status) {
                            Log.d(TAG, "onStatusChanged: " + status);
                            loadingUsersInterestedEventsTaskStatus.setValue(status);
                        }
                    });
                } else {
                    usersInterestedEventsLoader = null;
                }
            }
        });

        events = new MutableLiveData<>();
        loadEventsTaskStatus = new MutableLiveData<>();

        categories = new MutableLiveData<>();
        loadCategoriesTaskStatus = new MutableLiveData<>();

        users = new MutableLiveData<>();
        loadUsersTaskStatus = new MutableLiveData<>();

        usersInterestedEvents = new MutableLiveData<>();
        loadingUsersInterestedEventsTaskStatus = new MutableLiveData<>();

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

    private MutableLiveData<List<Event>> usersInterestedEvents;
    private MutableLiveData<TaskStatus> loadingUsersInterestedEventsTaskStatus;

    private FirestoreCollectionLoader<Event> usersInterestedEventsLoader;

    public MutableLiveData<TaskStatus> getLoadingUsersInterestedEventsTaskStatus() {
        return loadingUsersInterestedEventsTaskStatus;
    }

    public MutableLiveData<List<Event>> getUsersInterestedEvents() {
        return usersInterestedEvents;
    }

    public void loadNextUsersInterestedEvents(int countLimit) {
        if (usersInterestedEventsLoader != null) {
            Log.d(TAG, "loadNextUsersInterestedEvents: ");
            usersInterestedEventsLoader.loadNext(countLimit);
        }
    }

    private MutableLiveData<List<Event>> usersGoingEvents;
    private MutableLiveData<TaskStatus> loadingUsersGoingEventsTaskStatus;

    private FirestoreCollectionLoader<Event> usersGoingEventsLoader;

    public MutableLiveData<TaskStatus> getLoadingUsersGoingEventsTaskStatus() {
        return loadingUsersGoingEventsTaskStatus;
    }

    public MutableLiveData<List<Event>> getUsersGoingEvents() {
        return usersInterestedEvents;
    }

    public void loadNextUsersGoingEvents(int countLimit) {
        if (usersGoingEventsLoader != null) {
            Log.d(TAG, "loadNextUsersInterestedEvents: ");
            usersGoingEventsLoader.loadNext(countLimit);
        }
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

    private MutableLiveData<String> currentUserId = new MutableLiveData<>();

    private MutableLiveData<TaskStatus> logInTaskStatus = new MutableLiveData<>();

    /*
     * Log ing with email and password
     */

    public void loginWithEmail(String email, String password) {
        currentUserId.setValue("");
        new LogInByEmailAndPassword(email, password)
                .addTaskResultListener(new TaskResultListener<String>() {
                    @Override
                    public void onResult(String result) {
                        currentUserId.setValue(result);
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
        return currentUserId;
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
                        currentUserId.setValue(result);
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


    private MutableLiveData<String> postedEventId = new MutableLiveData<>();
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

        realm.close();
        return event;
    }

    /*
     * Loading discussion message
     */

    private MutableLiveData<List<DiscussionMessage>> discussionMessages = new MutableLiveData<>();
    private MutableLiveData<TaskStatus> loadDiscussionMessagesTaskStatus = new MutableLiveData<>();

    private FirestoreCollectionLoader<DiscussionMessage> discussionLoader;

    public MutableLiveData<List<DiscussionMessage>> initializeDiscussionMessages(String eventId) {

        discussionMessages = new MutableLiveData<>();

        discussionLoader = new FirestoreCollectionLoader<>(FirestoreUtil.getReferenceToEvents()
                .document(eventId).collection("messages"), DiscussionMessage.class);

        discussionLoader.addTaskStatusListener(new TaskStatusListener() {
            @Override
            public void onStatusChanged(TaskStatus status) {
                loadDiscussionMessagesTaskStatus.setValue(status);
            }
        });

        discussionLoader.addTaskResultListener(new TaskResultListener<List<DiscussionMessage>>() {
            @Override
            public void onResult(List<DiscussionMessage> result) {
                Log.d(TAG, "onResult: " + result);
                discussionMessages.setValue(result);
            }
        });

        return discussionMessages;
    }

    public MutableLiveData<TaskStatus> getLoadDiscussionMessagesTaskStatus() {
        return loadDiscussionMessagesTaskStatus;
    }

    public void loadNextDiscussionMessages(int limit) {
        discussionLoader.loadNext(limit);
    }


    /**
     * managing events
     */

    public void addEventToInterested(Event event, TaskResultListener<Boolean> resultListener) {
        networkEventManager.addEventToInterests(event, resultListener);
    }

    public void removeFromInterested(String eventId, TaskResultListener<Boolean> resultListener) {
        networkEventManager.removeEventFromInterested(eventId, resultListener);
    }

    public void addEventToGoing(Event event, TaskResultListener<Boolean> listener) {
        networkEventManager.addEventToGoing(event, listener);
    }

    public void removeEventFromGoing(String eventId, TaskResultListener<Boolean> resultListener) {
        networkEventManager.removeEventFromGoing(eventId, resultListener);
    }

    public void isUserInterestedEvent(String eventId, TaskResultListener<Boolean> taskResultListener) {
        networkEventManager.isUserInterestedEvent(eventId, taskResultListener);
    }

}
