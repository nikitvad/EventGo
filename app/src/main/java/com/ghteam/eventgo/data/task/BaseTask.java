package com.ghteam.eventgo.data.task;

import android.support.annotation.Nullable;

/**
 * Created by nikit on 08.01.2018.
 */

public abstract class BaseTask<P, R> implements NetworkTask<P>, TaskStatusInterface {

    private TaskResultListener<R> taskResultListener;
    private TaskStatusListener taskStatusListener;

    protected Exception exception;

    protected TaskStatus taskStatus = TaskStatus.NONE;

    protected void publishResult(R result) {
        if (taskResultListener != null) {
            taskResultListener.onResult(result);
        }
    }

    public BaseTask<P, R> addTaskStatusListener(TaskStatusListener listener) {
        taskStatusListener = listener;
        return this;
    }

    public BaseTask<P, R> addTaskResultListener(TaskResultListener<R> listener) {
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
