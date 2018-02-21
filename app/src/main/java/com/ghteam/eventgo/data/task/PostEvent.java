package com.ghteam.eventgo.data.task;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikit on 25.01.2018.
 */

public class PostEvent extends BaseTask<Void, String> {

    private static final String TAG = PostEvent.class.getSimpleName();

    private Event mEvent;

    public PostEvent(Event event) {
        mEvent = event;
    }

    @Override
    public void execute(Void... params) {
        changeStatus(TaskStatus.IN_PROGRESS);

        DocumentReference documentReference = FirestoreUtil.getReferenceToEvents().document();
        CollectionReference userEventsReference = FirestoreUtil.getReferenceToUsersEvents(mEvent.getOwnerId());

        final String eventId = documentReference.getId();
        mEvent.setId(eventId);

        documentReference.set(mEvent.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.getException() == null && task.isSuccessful()) {
                    publishResult(eventId);
                    changeStatus(TaskStatus.SUCCESS);
                } else {
                    exception = task.getException();
                    changeStatus(TaskStatus.ERROR);
                    Log.w(TAG, "onComplete: ", task.getException());
                }
            }
        });

        Map<String, String> collectionPath =  new HashMap<>();
        collectionPath.put("path", documentReference.getPath());
        userEventsReference.document(eventId).set(collectionPath);
    }
}
