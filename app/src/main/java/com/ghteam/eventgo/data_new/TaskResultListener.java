package com.ghteam.eventgo.data_new;

import android.support.annotation.Nullable;

/**
 * Created by nikit on 04.01.2018.
 */

public interface TaskResultListener<R> {
    @Nullable
    void onResult(R result);
}
