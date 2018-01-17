package com.ghteam.eventgo.data_new.task;

import android.support.annotation.Nullable;

/**
 * Created by nikit on 08.01.2018.
 */

public abstract class BaseNetworkTask<P, R> implements NetworkTask<P>, TaskStatusInterface {

    protected TaskResultListener<R> taskResultListener;
    protected TaskStatusListener taskStatusListener;

    protected Exception exception;

    protected TaskStatus taskStatus = TaskStatus.NONE;


    protected void publishResult(R result) {
        if (taskResultListener != null) {
            taskResultListener.onResult(result);
        }
    }

    public BaseNetworkTask<P, R> addTaskStatusListener(TaskStatusListener listener) {
        taskStatusListener = listener;
        return this;
    }

    public BaseNetworkTask<P, R> addTaskResultListener(TaskResultListener<R> listener) {
        taskResultListener = listener;
        return this;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

    protected void changeStatus(TaskStatus status) {
        if (taskStatusListener != null) {
            taskStatusListener.onStatusChanged(status);
        }
        taskStatus = status;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
