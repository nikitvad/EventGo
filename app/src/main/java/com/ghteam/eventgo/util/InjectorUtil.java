package com.ghteam.eventgo.util;

import android.content.Context;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.ui.activity.login.LoginViewModel;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsViewModel;
import com.ghteam.eventgo.ui.activity.userslist.PeopleViewModel;
import com.ghteam.eventgo.ui.fragment.eventslist.EventsListViewModel;
import com.ghteam.eventgo.ui.fragment.searchevents.SearchEventsViewModel;

/**
 * Created by nikit on 13.12.2017.
 */

public class InjectorUtil {

    public static Repository provideRepository(Context context) {
        Repository repository = Repository.getInstance(context,
                AppExecutors.getInstance());

        return repository;
    }

    public static EventDetailsViewModelFactory provideEventDetailsViewModelFactory(Context context, String eventId) {
        return new EventDetailsViewModelFactory(provideRepository(context), eventId);
    }

    public static LoginViewModel.LoginViewModelFactory provideLoginViewModelFactory(Context context) {
        return new LoginViewModel.LoginViewModelFactory(provideRepository(context));
    }

    public static EventsListViewModel.EventsListViewModelFactory provideEventsListViewModelFactory(Context context) {
        return new EventsListViewModel.EventsListViewModelFactory(provideRepository(context));
    }

    public static SearchEventsViewModel.SearchEventsViewModelFactory provideSearchEventsViewModelFactory(Context context) {
        return new SearchEventsViewModel.SearchEventsViewModelFactory(provideRepository(context));
    }

    public static ProfileSettingsViewModel.ProfileSettingViewModelFactory profileSettingViewModelFactory(Context context) {
        return new ProfileSettingsViewModel.ProfileSettingViewModelFactory(provideRepository(context));
    }

    public static PeopleViewModel.UsersViewModelFactory provideUsersViewModelFactory(Context context) {
        return new PeopleViewModel.UsersViewModelFactory(provideRepository(context));
    }


}
