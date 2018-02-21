package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.data.task.TaskStatusListener;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 05.02.2018.
 */

public class EventDiscussionClient {

    private String id;

    private CollectionReference discussionReference;

    private TaskStatusListener taskStatusListener;

    private DiscussionListener discussionListener;

    private Exception exception;


    private EventListener<QuerySnapshot> discussionMessagesEventListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

            if (e == null) {
                if (documentSnapshots.getDocumentChanges().size() > 0) {
                    publishChanges(documentSnapshots.getDocumentChanges());
                }

                changeTaskStatus(TaskStatus.SUCCESS);

            } else {
                exception = e;
                changeTaskStatus(TaskStatus.ERROR);
            }

        }
    };

    public EventDiscussionClient(String id) {
        this.id = id;
        discussionReference = FirestoreUtil.getReferenceToEvents().document(id).collection("messages");
        discussionReference.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(discussionMessagesEventListener);
    }

    private void publishChanges(List<DocumentChange> documentChanges) {

        List<DiscussionMessage> addedMessages = new ArrayList<>();

        for (DocumentChange item : documentChanges) {


            switch (item.getType()) {
                case ADDED:
//                    publishAddedMessage(item.getDocument().toObject(DiscussionMessage.class));
                    addedMessages.add(item.getDocument().toObject(DiscussionMessage.class));
                    break;
                case REMOVED:
                    publishRemovedMessage(item.getDocument().toObject(DiscussionMessage.class));
            }
        }

        publishAddedMessage(addedMessages);
    }

    public void sendMessage(DiscussionMessage discussionMessage) {
        DocumentReference documentReference = discussionReference.document();
        discussionMessage.setId(documentReference.getId());
        documentReference.set(discussionMessage);
    }

    public void sendMessage(String message, final TaskStatusListener taskStatusListener) {

        DiscussionMessage newMessage = new DiscussionMessage();
        newMessage.setOwnerName(PrefsUtil.getUserDisplayName());
        newMessage.setOwnerProfileImage(PrefsUtil.getUserProfilePicture());
        newMessage.setMessage(message);
        newMessage.setDate(new Date());
        DocumentReference documentReference = discussionReference.document();
        newMessage.setId(documentReference.getId());

        taskStatusListener.onStatusChanged(TaskStatus.IN_PROGRESS);

        documentReference.set(newMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    taskStatusListener.onStatusChanged(TaskStatus.SUCCESS);
                } else {
                    taskStatusListener.onStatusChanged(TaskStatus.ERROR);
                }
            }
        });

    }

    public void setTaskStatusListener(TaskStatusListener taskStatusListener) {
        this.taskStatusListener = taskStatusListener;
    }

    public void setDiscussionListener(DiscussionListener listener) {
        discussionListener = listener;
    }

    private void publishAddedMessage(List<DiscussionMessage> message) {
        if (discussionListener != null) {
            discussionListener.onAddedMessages(message);
        }
    }

    private void publishRemovedMessage(DiscussionMessage removedMessage) {
        if (discussionListener != null) {
            discussionListener.onMessagesRemoved(removedMessage);
        }
    }

    private void changeTaskStatus(TaskStatus taskStatus) {
        if (taskStatusListener != null) {
            taskStatusListener.onStatusChanged(taskStatus);
        }
    }

    public interface MessagesReceivedListener {
        void onReceived(List<DiscussionMessage> messages);
    }

    public interface DiscussionListener {
        void onAddedMessages(List<DiscussionMessage> newMessages);

        void onMessagesRemoved(DiscussionMessage discussionMessage);
    }
}
