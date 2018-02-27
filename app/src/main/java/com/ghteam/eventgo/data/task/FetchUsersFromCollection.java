package com.ghteam.eventgo.data.task;

import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 22.02.2018.
 */

public class FetchUsersFromCollection extends BaseTask<CollectionReference, User> {

    private static final String TAG = FetchUsersFromCollection.class.getSimpleName();

    @Override
    public void execute(CollectionReference... params) {
        final CollectionReference collectionReference = FirestoreUtil.getReferenceToUsers();

        changeStatus(TaskStatus.IN_PROGRESS);
//        Query query = makeQuery(collectionReference, params);

        params[0].get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    String[] usersIds = new String[result.size()];

                    for (int i = 0; i < usersIds.length; i++) {
                        usersIds[i] = result.getDocuments().get(i).getId();
                    }

                    LoadUserById loadUserById = new LoadUserById();
                    loadUserById.addTaskResultListener(new TaskResultListener<User>() {
                        @Override
                        public void onResult(User result) {
                            publishResult(result);
                        }
                    });
                    loadUserById.addTaskStatusListener(new TaskStatusListener() {
                        @Override
                        public void onStatusChanged(TaskStatus status) {
                            changeStatus(status);
                        }
                    });
                    loadUserById.execute(usersIds);

                } else {
                    changeStatus(TaskStatus.ERROR);
                }
            }
        });

    }

    private Query makeQuery(CollectionReference collectionReference, List<String> ids) {
        Query result = collectionReference.whereEqualTo("id", "");

        for (int i = 0; i < ids.size(); i++) {
            result = result.whereEqualTo("id", ids.get(i));
        }
        return result;
    }
}
