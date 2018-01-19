package com.ghteam.eventgo.util;

import android.support.annotation.Nullable;

import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.util.network.AccountStatus;

/**
 * Created by nikit on 19.01.2018.
 */

public class AccountUtil {
    public static AccountStatus checkUserAccount(@Nullable User user) {
        if (user != null) {
            if (!user.getId().isEmpty() && !user.getFirstName().isEmpty()
                    && !user.getLastName().isEmpty() && user.getInterests().size() > 0) {
                return AccountStatus.OK;
            } else {
                return AccountStatus.REQUIRE_UPDATE_PROFILE;
            }
        } else {
            return AccountStatus.NULL;
        }
    }
}
