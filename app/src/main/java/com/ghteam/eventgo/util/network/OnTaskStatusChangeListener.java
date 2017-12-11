package com.ghteam.eventgo.util.network;

/**
 * Created by nikit on 11.12.2017.
 */

public interface OnTaskStatusChangeListener {

    void onChanged(TaskStatus status);

    enum TaskStatus {
        SUCCESS,
        FAILED,
        PAUSED,
        IN_PROGRESS,
        CANCELED
    }
}
