package com.ghteam.eventgo.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.databinding.LayoutEventListItemV2Binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 24.12.2017.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.EventViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private List<Event> mItems;

    public EventsRecyclerAdapter(){
        mItems = new ArrayList<>();
    }

    public EventsRecyclerAdapter(List<Event> items){
        mItems = items;
    }



    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_event_list_item_v2, parent);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.getViewBinding().setVariable(BR.event, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        private LayoutEventListItemV2Binding viewBinding;

        public EventViewHolder(View itemView) {
            super(itemView);

            viewBinding = DataBindingUtil.bind(itemView);
            if(mOnItemClickListener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(mItems.get(getAdapterPosition()));
                    }
                });
            }

        }

        public LayoutEventListItemV2Binding getViewBinding() {
            return viewBinding;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
