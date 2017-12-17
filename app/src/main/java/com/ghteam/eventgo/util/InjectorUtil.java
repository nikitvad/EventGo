package com.ghteam.eventgo.util;

import android.content.Context;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.database.Database;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.ui.activity.login.LoginViewModel;

/**
 * Created by nikit on 13.12.2017.
 */

public class InjectorUtil {

    public static Repository provideRepository(Context context) {
        Database db = Database.getInstance(context);
        Repository repository = Repository.getInstance(context,
                AppExecutors.getInstance(),
                db.eventDao(),
                db.categoryDao(),
                db.imageDao(),
                db.locationDao());

        return repository;
    }

    public static EventDetailsViewModelFactory provideEventDetailsViewModelFactory(Context context, String eventId) {
        return new EventDetailsViewModelFactory(provideRepository(context), eventId);
    }

    public static LoginViewModel.LoginViewModelFactory provideLoginViewModelFactory(Context context){
        return new LoginViewModel.LoginViewModelFactory(provideRepository(context));
    }


}
