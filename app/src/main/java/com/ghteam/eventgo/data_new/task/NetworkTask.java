package com.ghteam.eventgo.data_new.task;

/**
 * Created by nikit on 04.01.2018.
 */

public interface NetworkTask<P> {
    void execute(P... params);
}