package com.ghteam.eventgo.util;

import android.content.Context;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data_new.database.Database;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.ui.activity.login.LoginViewModel;
import com.ghteam.eventgo.ui.fragment.eventslist.EventsListViewModel;
import com.ghteam.eventgo.ui.fragment.searchevents.SearchEventsViewModel;

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

    public static LoginViewModel.LoginViewModelFactory provideLoginViewModelFactory(Context context) {
        return new LoginViewModel.LoginViewModelFactory(provideRepository(context));
    }

    public static EventsListViewModel.EventsListViewModelFactory provideEventsListViewModelFactory(Context context) {
        return new EventsListViewModel.EventsListViewModelFactory(com.ghteam.eventgo.data_new.Repository.getInstance(context));
    }

    public static SearchEventsViewModel.SearchEventsViewModelFactory provideSearchEventsViewModelFactory(Context context) {
        return new SearchEventsViewModel.SearchEventsViewModelFactory(provideRepository(context));
    }


}
