package com.ghteam.eventgo.data.task;


/**
 * Created by nikit on 04.01.2018.
 */

public interface TaskResultListener<R> {
    void onResult(R result);
}
