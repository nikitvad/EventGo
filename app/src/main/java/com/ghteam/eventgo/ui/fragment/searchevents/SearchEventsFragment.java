package com.ghteam.eventgo.ui.fragment.searchevents;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.databinding.FragmentSearchEventsBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsActivity;
import com.ghteam.eventgo.util.InjectorUtil;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentSearchEventsBinding fragmentBinding;

    private static final String TAG = SearchEventsFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SearchEventsViewModel viewModel;

    private RecyclerBindingAdapter<Event> recyclerAdapter;

    public SearchEventsFragment() {
        // Required empty public constructor
    }


    public static SearchEventsFragment newInstance() {
        return new SearchEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_events, container, false);

        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchEventsViewModel.SearchEventsViewModelFactory viewModelFactory = InjectorUtil
                .provideSearchEventsViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchEventsViewModel.class);

        recyclerAdapter = new RecyclerBindingAdapter<>(R.layout.layout_event_list_item_v2,
                BR.event, new ArrayList<Event>());

        recyclerAdapter.setOnItemClickListener(new RecyclerBindingAdapter.OnItemClickListener<Event>() {
            @Override
            public void onItemClick(int position, Event item) {
                Intent intentEventDetails = new Intent(getContext(), EventDetailsActivity.class);
                intentEventDetails.putExtra("eventId", item.getId());
                startActivity(intentEventDetails);
            }
        });

        fragmentBinding.rvEventsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1)) {
                    if (viewModel.getTaskStatus().getValue() != OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS) {
                        viewModel.loadNext();
                        Log.d(TAG, "onScrollStateChanged: ");
                    }
                }
            }
        });

        fragmentBinding.rvEventsList.setAdapter(recyclerAdapter);

        fragmentBinding.rvEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        registerViewModelObservers();
//        viewModel.loadNext();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void registerViewModelObservers() {
        viewModel.getEventsList().observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                recyclerAdapter.addItems(events);
            }
        });

        viewModel.getTaskStatus().observeForever(new Observer<OnTaskStatusChangeListener.TaskStatus>() {
            @Override
            public void onChanged(@Nullable OnTaskStatusChangeListener.TaskStatus taskStatus) {
                switch (taskStatus) {
                    case IN_PROGRESS:
                        showProgressBar();
                        return;
                    default:
                        hideProgressBar();
                        return;
                }
            }
        });
    }

    private void showProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        fragmentBinding.rvEventsList.setAlpha(0.5f);
    }


    private void hideProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        fragmentBinding.rvEventsList.setAlpha(1f);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
