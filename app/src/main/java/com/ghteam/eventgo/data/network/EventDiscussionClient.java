package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.data.task.TaskStatusListener;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 05.02.2018.
 */

public class EventDiscussionClient {

    private String id;

    private List<DiscussionMessage> messages;
    private CollectionReference discussionReference;

    private TaskStatusListener taskStatusListener;

    private MessagesReceivedListener messagesReceivedListener;
    private DiscussionListener discussionListener;

    private DocumentSnapshot lastLoadedDocument;

    private Exception exception;

    private TaskStatus currentTaskStatus;

    private EventListener<QuerySnapshot> discussionMessagesEventListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

            if (documentSnapshots.getDocumentChanges().size() > 0) {

                List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                for (DocumentChange item : documentChanges) {
                    switch (item.getType()) {
                        case ADDED:
                            publishAddedMessage(item.getDocument().toObject(DiscussionMessage.class));
                            break;
                        case REMOVED:
                            publishRemovedMessage(item.getDocument().toObject(DiscussionMessage.class));
                    }
                }
            }
        }
    };

    public EventDiscussionClient(String id) {
        this.id = id;
        discussionReference = FirestoreUtil.getReferenceToEvents().document(id).collection("messages");
        discussionReference.addSnapshotListener(discussionMessagesEventListener);
    }

    public void loadDiscussion(int messageCountLimit) {

        changeTaskStatus(TaskStatus.IN_PROGRESS);

        discussionReference.limit(messageCountLimit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getException() == null) {

                            QuerySnapshot result = task.getResult();
                            if (result.size() > 0) {

                                lastLoadedDocument = result.getDocuments().get(result.size() - 1);
                                publishMessages(task.getResult().toObjects(DiscussionMessage.class));
                            }
                            changeTaskStatus(TaskStatus.SUCCESS);
                        } else {
                            changeTaskStatus(TaskStatus.ERROR);
                            exception = task.getException();
                        }
                    }
                });
    }

    public void loadNextMessages(int countLimit) {

        if (currentTaskStatus != TaskStatus.IN_PROGRESS) {
            changeTaskStatus(TaskStatus.IN_PROGRESS);
            if (lastLoadedDocument == null) {
                discussionReference.limit(countLimit).get().addOnCompleteListener(loadMessagesCompleteListener);
            } else {
                discussionReference.limit(countLimit).startAfter(lastLoadedDocument)
                        .get().addOnCompleteListener(loadMessagesCompleteListener);
            }
        }
    }

    public void sendMessage(DiscussionMessage discussionMessage) {
        DocumentReference documentReference = discussionReference.document();
        discussionMessage.setId(documentReference.getId());
        documentReference.set(discussionMessage);
    }

    private OnCompleteListener<QuerySnapshot> loadMessagesCompleteListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.getException() == null) {

                QuerySnapshot result = task.getResult();
                if (result.size() > 0) {

                    lastLoadedDocument = result.getDocuments().get(result.size() - 1);
                    publishMessages(result.toObjects(DiscussionMessage.class));
                }
                changeTaskStatus(TaskStatus.SUCCESS);
            } else {
                changeTaskStatus(TaskStatus.ERROR);
                exception = task.getException();
            }
        }
    };

    public void setTaskStatusListener(TaskStatusListener taskStatusListener) {
        this.taskStatusListener = taskStatusListener;
    }

    public void setDiscussionListener(DiscussionListener listener) {
        discussionListener = listener;
    }

    private void publishAddedMessage(DiscussionMessage message) {
        if (discussionListener != null) {
            discussionListener.onAddedMessages(message);
        }
    }

    private void publishRemovedMessage(DiscussionMessage removedMessage) {
        if (discussionListener != null) {
            discussionListener.onMessagesRemoved(removedMessage);
        }
    }

    private void publishMessages(List<DiscussionMessage> messages) {
        if (messagesReceivedListener != null) {
            messagesReceivedListener.onReceived(messages);
        }
    }

    private void changeTaskStatus(TaskStatus taskStatus) {
        currentTaskStatus = taskStatus;
        if (taskStatusListener != null) {
            taskStatusListener.onStatusChanged(taskStatus);
        }
    }

    public interface MessagesReceivedListener {
        void onReceived(List<DiscussionMessage> messages);
    }

    public interface DiscussionListener {
        void onAddedMessages(DiscussionMessage newMessages);

        void onMessagesRemoved(DiscussionMessage discussionMessage);
    }
}
